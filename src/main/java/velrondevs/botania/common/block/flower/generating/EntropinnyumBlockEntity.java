package velrondevs.botania.common.block.flower.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.api.block_entity.GeneratingFlowerBlockEntity;
import velrondevs.botania.api.block_entity.RadiusDescriptor;
import velrondevs.botania.api.mana.ManaReceiver;
import velrondevs.botania.client.fx.SparkleParticleData;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaSounds;
import velrondevs.botania.xplat.BotaniaConfig;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.List;

public class EntropinnyumBlockEntity extends GeneratingFlowerBlockEntity implements ManaReceiver {
	private static final int RANGE = 12;
	private static final int EXPLODE_EFFECT_EVENT = 0;
	private static final int ANGRY_EFFECT_EVENT = 1;
	private static final String TAG_OVERFLOW_MANA = "overflowMana";

	private int overflowMana = 0;

	public EntropinnyumBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.ENTROPINNYUM, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getLevel().isClientSide() && getMana() == 0) {
			List<PrimedTnt> tnts = getLevel().getEntitiesOfClass(PrimedTnt.class, new AABB(Vec3.atLowerCornerOf(getEffectivePos().offset(-RANGE, -RANGE, -RANGE)), Vec3.atLowerCornerOf(getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1))));
			for (PrimedTnt tnt : tnts) {
				FluidState fluid = getLevel().getFluidState(tnt.blockPosition());
				if (tnt.getFuse() == 1 && tnt.isAlive() && fluid.isEmpty()) {
					boolean unethical = XplatAbstractions.INSTANCE.ethicalComponent(tnt).isUnethical();
					tnt.playSound(unethical ? BotaniaSounds.entropinnyumAngry : BotaniaSounds.entropinnyumHappy, 1F, (1F + (getLevel().getRandom().nextFloat() - getLevel().getRandom().nextFloat()) * 0.2F) * 0.7F);
					tnt.discard();
					addMana(unethical ? 3 : getMaxMana());
					sync();

					getLevel().blockEvent(getBlockPos(), getBlockState().getBlock(), unethical ? ANGRY_EFFECT_EVENT : EXPLODE_EFFECT_EVENT, tnt.getId());
					break;
				}
			}
		}
	}

	@Override
	public boolean triggerEvent(int event, int param) {
		if (event == EXPLODE_EFFECT_EVENT) {
			if (getLevel().isClientSide() && getLevel().getEntity(param) instanceof PrimedTnt) {
				Entity e = getLevel().getEntity(param);

				for (int i = 0; i < 50; i++) {
					SparkleParticleData data = SparkleParticleData.sparkle((float) (Math.random() * 0.65F + 1.25F), 1F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 12);
					level.addParticle(data, e.getX() + Math.random() * 4 - 2, e.getY() + Math.random() * 4 - 2, e.getZ() + Math.random() * 4 - 2, 0, 0, 0);
				}

				getLevel().addParticle(ParticleTypes.EXPLOSION_EMITTER, e.getX(), e.getY(), e.getZ(), 1D, 0D, 0D);
			}
			return true;
		} else if (event == ANGRY_EFFECT_EVENT) {
			if (getLevel().isClientSide() && getLevel().getEntity(param) instanceof PrimedTnt) {
				Entity e = getLevel().getEntity(param);

				for (int i = 0; i < 50; i++) {
					level.addParticle(ParticleTypes.ANGRY_VILLAGER, e.getX() + Math.random() * 4 - 2, e.getY() + Math.random() * 4 - 2, e.getZ() + Math.random() * 4 - 2, 0, 0, 0);
				}
			}

			return true;
		} else {
			return super.triggerEvent(event, param);
		}
	}

	public boolean acceptsOverflow() {
		var config = BotaniaConfig.common();
		return config == null || config.entropinnyumAcceptOverflow();
	}

	@Override
	public int getMana() {
		return (int) Math.min(Integer.MAX_VALUE, (long) super.getMana() + overflowMana);
	}

	@Override
	public void addMana(int mana) {
		if (mana < 0 && overflowMana > 0) {
			int fromOverflow = Math.min(overflowMana, -mana);
			overflowMana -= fromOverflow;
			mana += fromOverflow;
		}
		super.addMana(mana);
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
		return getMana();
	}

	@Override
	public boolean isFull() {
		return !acceptsOverflow() || getMana() >= Integer.MAX_VALUE - getMaxMana();
	}

	@Override
	public void receiveMana(int mana) {
		if (mana == 0 || mana > 0 && !acceptsOverflow()) {
			return;
		}
		if (mana < 0) {
			addMana(mana);
		} else {
			long limit = (long) Integer.MAX_VALUE - getMaxMana() - super.getMana();
			overflowMana = (int) Math.max(0, Math.min(limit, (long) overflowMana + mana));
			setChanged();
		}
		sync();
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return acceptsOverflow();
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.readFromPacketNBT(cmp, registries);
		overflowMana = Math.max(0, cmp.getIntOr(TAG_OVERFLOW_MANA, 0));
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.writeToPacketNBT(cmp, registries);
		if (overflowMana > 0) {
			cmp.putInt(TAG_OVERFLOW_MANA, overflowMana);
		}
	}

	@Override
	public int getColor() {
		return 0xcb0000;
	}

	@Override
	public int getMaxMana() {
		return 6500;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

}
