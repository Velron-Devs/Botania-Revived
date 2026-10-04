package velrondevs.botania.common.block.flower.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.IntSupplier;

import velrondevs.botania.api.block_entity.GeneratingFlowerBlockEntity;
import velrondevs.botania.api.block_entity.RadiusDescriptor;
import velrondevs.botania.api.mana.ManaCollector;
import velrondevs.botania.common.helper.DelayHelper;
import velrondevs.botania.common.helper.EntityHelper;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.xplat.BotaniaConfig;

public class WitherManaRoseBlockEntity extends GeneratingFlowerBlockEntity {
	public static final int DEFAULT_MANA_PER_STAR = 200000;
	public static final int DEFAULT_GENERATION_INTERVAL = 1;
	public static final int DEFAULT_GENERATION_PER_CYCLE = 850;
	public static final int DEFAULT_BUFFER_SIZE = 8500;
	public static final int DEFAULT_OUTPUT_PER_TICK = 850;
	public static final int DEFAULT_COOLDOWN = 1600;
	public static final int RANGE = 1;

	private static final String TAG_STAR_MANA = "starMana";
	private static final String TAG_COOLDOWN = "cooldown";
	private static final int ABSORB_EVENT = 0;

	private int starMana = 0;
	private int cooldown = 0;

	public WitherManaRoseBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.WITHER_MANA_ROSE, pos, state);
	}

	private static int setting(IntSupplier supplier, int fallback) {
		try {
			return supplier.getAsInt();
		} catch (RuntimeException e) {
			return fallback;
		}
	}

	public static boolean isEnabled() {
		try {
			return BotaniaConfig.common().witherManaRoseEnabled();
		} catch (RuntimeException e) {
			return true;
		}
	}

	public static int manaPerStar() {
		return setting(() -> BotaniaConfig.common().witherManaRoseManaPerStar(), DEFAULT_MANA_PER_STAR);
	}

	public static int generationInterval() {
		return setting(() -> BotaniaConfig.common().witherManaRoseGenerationInterval(), DEFAULT_GENERATION_INTERVAL);
	}

	public static int generationPerCycle() {
		return setting(() -> BotaniaConfig.common().witherManaRoseGenerationPerCycle(), DEFAULT_GENERATION_PER_CYCLE);
	}

	public static int bufferSize() {
		return setting(() -> BotaniaConfig.common().witherManaRoseBufferSize(), DEFAULT_BUFFER_SIZE);
	}

	public static int outputPerTick() {
		return setting(() -> BotaniaConfig.common().witherManaRoseOutputPerTick(), DEFAULT_OUTPUT_PER_TICK);
	}

	public static int cooldownTicks() {
		return setting(() -> BotaniaConfig.common().witherManaRoseCooldown(), DEFAULT_COOLDOWN);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide) {
			if (starMana > 0 && getLevel().random.nextInt(6) == 0) {
				emitParticle(ParticleTypes.SMOKE, 0.4 + Math.random() * 0.2, 0.7, 0.4 + Math.random() * 0.2, 0.0D, 0.0D, 0.0D);
			}
			return;
		}

		if (!isEnabled()) {
			return;
		}

		if (starMana > 0) {
			if (ticksExisted % generationInterval() != 0) {
				return;
			}
			int generated = Math.min(generationPerCycle(), Math.min(starMana, getMaxMana() - getMana()));
			if (generated > 0) {
				starMana -= generated;
				addMana(generated);
				if (starMana == 0) {
					cooldown = cooldownTicks();
					getLevel().gameEvent(null, GameEvent.BLOCK_DEACTIVATE, getBlockPos());
					sync();
				}
			}
			return;
		}

		if (cooldown > 0) {
			cooldown--;
			if (cooldown == 0) {
				setChanged();
				sync();
			}
			return;
		}

		if (getMana() < getMaxMana()) {
			for (ItemEntity item : getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(Vec3.atLowerCornerOf(getEffectivePos().offset(-RANGE, -RANGE, -RANGE)), Vec3.atLowerCornerOf(getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1))))) {
				if (!DelayHelper.canInteractWith(this, item)) {
					continue;
				}
				ItemStack stack = item.getItem();
				if (stack.is(Items.NETHER_STAR) && !stack.isEmpty()) {
					starMana = manaPerStar();
					EntityHelper.shrinkItem(item);
					getLevel().playSound(null, getEffectivePos(), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 0.6F, 0.5F);
					getLevel().blockEvent(getBlockPos(), getBlockState().getBlock(), ABSORB_EVENT, item.getId());
					getLevel().gameEvent(null, GameEvent.BLOCK_ACTIVATE, getBlockPos());
					setChanged();
					sync();
					return;
				}
			}
		}
	}

	@Override
	public void emptyManaIntoCollector() {
		ManaCollector collector = findBoundTile();
		if (collector != null && !collector.isFull() && getMana() > 0) {
			int manaval = Math.min(outputPerTick(), Math.min(getMana(), collector.getMaxMana() - collector.getCurrentMana()));
			addMana(-manaval);
			collector.receiveMana(manaval);
			sync();
		}
	}

	@Override
	public boolean triggerEvent(int event, int param) {
		if (event == ABSORB_EVENT) {
			Entity e = getLevel().getEntity(param);
			if (e != null) {
				for (int i = 0; i < 8; i++) {
					e.level().addParticle(ParticleTypes.SOUL, e.getX(), e.getY() + 0.2, e.getZ(), (Math.random() - 0.5) * 0.1, 0.05 + Math.random() * 0.05, (Math.random() - 0.5) * 0.1);
				}
				e.level().addParticle(ParticleTypes.LARGE_SMOKE, e.getX(), e.getY() + 0.1, e.getZ(), 0.0D, 0.0D, 0.0D);
			}
			return true;
		} else {
			return super.triggerEvent(event, param);
		}
	}

	public int getStarMana() {
		return starMana;
	}

	public int getCooldown() {
		return cooldown;
	}

	@Override
	public int getMaxMana() {
		return bufferSize();
	}

	@Override
	public int getColor() {
		return 0x333333;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.writeToPacketNBT(cmp, registries);

		cmp.putInt(TAG_STAR_MANA, starMana);
		cmp.putInt(TAG_COOLDOWN, cooldown);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.readFromPacketNBT(cmp, registries);

		starMana = cmp.getInt(TAG_STAR_MANA);
		cooldown = cmp.getInt(TAG_COOLDOWN);
	}
}
