package velrondevs.botania.integration.create;

import com.google.common.base.Predicates;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.mana.ManaPool;
import velrondevs.botania.api.mana.ManaReceiver;
import velrondevs.botania.api.mana.spark.ManaSpark;
import velrondevs.botania.api.mana.spark.SparkAttachable;
import velrondevs.botania.api.mana.spark.SparkHelper;

import java.util.List;

public class ManaMotorBlockEntity extends GeneratingKineticBlockEntity implements ManaReceiver, SparkAttachable {
	private static final String TAG_MANA = "mana";
	private static final String TAG_ACTIVE = "manaActive";
	private static final String TAG_PENDING = "manaPending";
	private static final String TAG_COST = "manaCost";
	private static final int SYNC_INTERVAL = 10;
	private static final int SPARK_INTERVAL = 10;

	public ScrollValueBehaviour generatedSpeed;
	private int mana;
	private boolean active;
	private double pendingCost;
	private double lastCost;
	private int lastSyncedMana = -1;
	private int ticks;

	public ManaMotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		super.addBehaviours(behaviours);
		int max = ManaMotorConfig.maxRpm();
		generatedSpeed = new KineticScrollValueBehaviour(Component.translatable("create.kinetics.creative_motor.rotation_speed"), this, new ManaMotorValueBox());
		generatedSpeed.between(-max, max);
		generatedSpeed.value = Math.min(ManaMotorConfig.defaultRpm(), max);
		generatedSpeed.withCallback(i -> updateGeneratedRotation());
		behaviours.add(generatedSpeed);
	}

	@Override
	public void initialize() {
		super.initialize();
		if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed()) {
			updateGeneratedRotation();
		}
	}

	@Override
	public float getGeneratedSpeed() {
		if (!(getBlockState().getBlock() instanceof ManaMotorBlock) || !active) {
			return 0;
		}
		return convertToDirection(configuredSpeed(), getBlockState().getValue(ManaMotorBlock.FACING));
	}

	private int configuredSpeed() {
		int value = generatedSpeed == null ? 0 : generatedSpeed.getValue();
		if (value == 0) {
			return 0;
		}
		int abs = Math.max(ManaMotorConfig.minRpm(), Math.min(ManaMotorConfig.maxRpm(), Math.abs(value)));
		return value < 0 ? -abs : abs;
	}

	@Override
	public float calculateAddedStressCapacity() {
		float provided = (float) ManaMotorConfig.stressPerRpm();
		lastCapacityProvided = provided;
		return provided;
	}

	public double getConsumedStress() {
		float generated = Math.abs(getGeneratedSpeed());
		if (generated == 0 || capacity <= 0 || overStressed || !hasNetwork()) {
			return 0;
		}
		double own = ManaMotorConfig.stressPerRpm() * generated;
		double share = Math.min(1.0, own / capacity);
		return Math.max(0, Math.min(stress, capacity)) * share;
	}

	@Override
	public void tick() {
		super.tick();
		if (level == null || level.isClientSide) {
			return;
		}
		ticks++;
		boolean enabled = ManaMotorConfig.enabled();
		if (enabled && ticks % SPARK_INTERVAL == 0 && !isFull()) {
			pullFromSparks();
		}
		if (active) {
			if (!enabled) {
				setActive(false);
			} else {
				consumeMana();
			}
		} else {
			lastCost = 0;
			if (enabled && mana > 0 && mana >= restartThreshold()) {
				setActive(true);
			}
		}
		if (ticks % SYNC_INTERVAL == 0 && mana != lastSyncedMana) {
			lastSyncedMana = mana;
			sendData();
		}
	}

	private void consumeMana() {
		double cost = ManaMotorConfig.baseManaPerTick()
				+ getConsumedStress() / ManaMotorConfig.stressUnit() * ManaMotorConfig.manaPerStressUnit();
		lastCost = cost;
		pendingCost += cost;
		int whole = (int) pendingCost;
		if (whole <= 0) {
			return;
		}
		if (whole > mana) {
			mana = 0;
			pendingCost = 0;
			setChanged();
			setActive(false);
			return;
		}
		mana -= whole;
		pendingCost -= whole;
		setChanged();
	}

	private int restartThreshold() {
		double perSecond = (ManaMotorConfig.baseManaPerTick() + ManaMotorConfig.manaPerStressUnit()) * 20;
		return (int) Math.max(1, Math.min(getManaCapacity(), Math.ceil(perSecond)));
	}

	private void setActive(boolean value) {
		if (active == value) {
			return;
		}
		active = value;
		updateGeneratedRotation();
		setChanged();
		sendData();
	}

	private void pullFromSparks() {
		ManaSpark spark = getAttachedSpark();
		if (spark == null) {
			return;
		}
		BlockPos pos = getBlockPos();
		for (ManaSpark other : SparkHelper.getSparksAround(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, spark.getNetwork())) {
			if (other != spark && other.getAttachedManaReceiver() instanceof ManaPool) {
				other.registerTransfer(spark);
			}
		}
	}

	public int getManaCapacity() {
		return ManaMotorConfig.manaCapacity();
	}

	public boolean isRunning() {
		return active;
	}

	public double getLastManaCost() {
		return lastCost;
	}

	@Override
	protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
		super.write(tag, registries, clientPacket);
		tag.putInt(TAG_MANA, mana);
		tag.putBoolean(TAG_ACTIVE, active);
		tag.putDouble(TAG_PENDING, pendingCost);
		if (clientPacket) {
			tag.putDouble(TAG_COST, lastCost);
		}
	}

	@Override
	protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
		super.read(tag, registries, clientPacket);
		mana = tag.getInt(TAG_MANA);
		active = tag.getBoolean(TAG_ACTIVE);
		pendingCost = tag.getDouble(TAG_PENDING);
		if (clientPacket) {
			lastCost = tag.getDouble(TAG_COST);
		}
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		super.addToGoggleTooltip(tooltip, isPlayerSneaking);
		tooltip.add(line(Component.translatable("block.botania.mana_motor").withStyle(ChatFormatting.WHITE)));
		tooltip.add(line(Component.translatable("botania.goggles.mana_motor.mana",
				String.format("%,d", mana), String.format("%,d", getManaCapacity())).withStyle(ChatFormatting.AQUA)));
		tooltip.add(line(Component.translatable("botania.goggles.mana_motor.speed",
				String.valueOf(Math.round(Math.abs(getSpeed())))).withStyle(ChatFormatting.GRAY)));
		tooltip.add(line(Component.translatable("botania.goggles.mana_motor.stress",
				String.format("%,.0f", getConsumedStress()),
				String.format("%,.0f", ManaMotorConfig.stressPerRpm() * Math.abs(getGeneratedSpeed()))).withStyle(ChatFormatting.GRAY)));
		tooltip.add(line(Component.translatable("botania.goggles.mana_motor.cost",
				String.format("%.2f", lastCost)).withStyle(ChatFormatting.GRAY)));
		if (!ManaMotorConfig.enabled()) {
			tooltip.add(line(Component.translatable("botania.goggles.mana_motor.disabled").withStyle(ChatFormatting.RED)));
		} else if (!active) {
			tooltip.add(line(Component.translatable("botania.goggles.mana_motor.no_mana").withStyle(ChatFormatting.RED)));
		}
		return true;
	}

	private static Component line(Component content) {
		return Component.literal("    ").append(content);
	}

	@Override
	public Level getManaReceiverLevel() {
		return getLevel();
	}

	@Override
	public BlockPos getManaReceiverPos() {
		return getBlockPos();
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return !ManaMotorConfig.enabled() || mana >= getManaCapacity();
	}

	@Override
	public void receiveMana(int amount) {
		int updated = (int) Math.max(0, Math.min((long) getManaCapacity(), (long) mana + amount));
		if (updated != mana) {
			mana = updated;
			setChanged();
		}
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return ManaMotorConfig.enabled();
	}

	@Override
	public boolean canAttachSpark(ItemStack stack) {
		return true;
	}

	@Override
	public int getAvailableSpaceForMana() {
		return ManaMotorConfig.enabled() ? Math.max(0, getManaCapacity() - mana) : 0;
	}

	@Override
	@Nullable
	public ManaSpark getAttachedSpark() {
		if (level == null) {
			return null;
		}
		BlockPos above = getBlockPos().above();
		List<Entity> sparks = level.getEntitiesOfClass(Entity.class,
				new AABB(Vec3.atLowerCornerOf(above), Vec3.atLowerCornerOf(above.offset(1, 1, 1))),
				Predicates.instanceOf(ManaSpark.class));
		if (sparks.size() == 1) {
			return (ManaSpark) sparks.get(0);
		}
		return null;
	}

	@Override
	public boolean areIncomingTranfersDone() {
		return isFull();
	}
}
