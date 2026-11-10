package velrondevs.botania.common.block.flower.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.api.block_entity.GeneratingFlowerBlockEntity;
import velrondevs.botania.api.block_entity.RadiusDescriptor;
import velrondevs.botania.api.mana.ManaReceiver;
import velrondevs.botania.api.mana.spark.ManaSpark;
import velrondevs.botania.api.mana.spark.SparkAttachable;
import velrondevs.botania.client.fx.SparkleParticleData;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaSounds;
import velrondevs.botania.xplat.BotaniaConfig;
import velrondevs.botania.xplat.XplatAbstractions;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EntropinnyumBlockEntity extends GeneratingFlowerBlockEntity {
	private static final int RANGE = 12;
	private static final int EXPLODE_EFFECT_EVENT = 0;
	private static final int ANGRY_EFFECT_EVENT = 1;

	public EntropinnyumBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.ENTROPINNYUM, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getLevel().isClientSide && getMana() == 0) {
			List<PrimedTnt> tnts = getLevel().getEntitiesOfClass(PrimedTnt.class, new AABB(Vec3.atLowerCornerOf(getEffectivePos().offset(-RANGE, -RANGE, -RANGE)), Vec3.atLowerCornerOf(getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1))));
			for (PrimedTnt tnt : tnts) {
				FluidState fluid = getLevel().getFluidState(tnt.blockPosition());
				if (tnt.getFuse() == 1 && tnt.isAlive() && fluid.isEmpty()) {
					boolean unethical = XplatAbstractions.INSTANCE.ethicalComponent(tnt).isUnethical();
					tnt.playSound(unethical ? BotaniaSounds.entropinnyumAngry : BotaniaSounds.entropinnyumHappy, 1F, (1F + (getLevel().random.nextFloat() - getLevel().random.nextFloat()) * 0.2F) * 0.7F);
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
			if (getLevel().isClientSide && getLevel().getEntity(param) instanceof PrimedTnt) {
				Entity e = getLevel().getEntity(param);

				for (int i = 0; i < 50; i++) {
					SparkleParticleData data = SparkleParticleData.sparkle((float) (Math.random() * 0.65F + 1.25F), 1F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 12);
					level.addParticle(data, e.getX() + Math.random() * 4 - 2, e.getY() + Math.random() * 4 - 2, e.getZ() + Math.random() * 4 - 2, 0, 0, 0);
				}

				getLevel().addParticle(ParticleTypes.EXPLOSION_EMITTER, e.getX(), e.getY(), e.getZ(), 1D, 0D, 0D);
			}
			return true;
		} else if (event == ANGRY_EFFECT_EVENT) {
			if (getLevel().isClientSide && getLevel().getEntity(param) instanceof PrimedTnt) {
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

	@Override
	public void addMana(int mana) {
		if (mana < 0 && getMana() > getMaxMana()) {
			addManaUncapped(mana);
		} else {
			super.addMana(mana);
		}
	}

	public static boolean acceptsOverflow() {
		return BotaniaConfig.common().entropinnyumAcceptOverflow();
	}

	@Nullable
	public OverflowReceiver getOverflowReceiver() {
		return acceptsOverflow() ? new OverflowReceiver(this) : null;
	}

	public record OverflowReceiver(EntropinnyumBlockEntity flower) implements ManaReceiver, SparkAttachable {
		@Override
		public Level getManaReceiverLevel() {
			return flower.getLevel();
		}

		@Override
		public BlockPos getManaReceiverPos() {
			return flower.getBlockPos();
		}

		@Override
		public int getCurrentMana() {
			return flower.getMana();
		}

		@Override
		public boolean isFull() {
			return !acceptsOverflow() || flower.getMana() == Integer.MAX_VALUE;
		}

		@Override
		public void receiveMana(int mana) {
			if (mana > 0 && !acceptsOverflow()) {
				return;
			}
			flower.addManaUncapped(mana);
			flower.sync();
		}

		@Override
		public boolean canReceiveManaFromBursts() {
			return acceptsOverflow();
		}

		@Override
		public boolean canAttachSpark(ItemStack stack) {
			return acceptsOverflow();
		}

		@Override
		public int getAvailableSpaceForMana() {
			return acceptsOverflow() ? Integer.MAX_VALUE - flower.getMana() : 0;
		}

		@Override
		public ManaSpark getAttachedSpark() {
			Level level = flower.getLevel();
			if (level == null) {
				return null;
			}
			BlockPos above = flower.getBlockPos().above();
			List<Entity> sparks = level.getEntitiesOfClass(Entity.class, new AABB(Vec3.atLowerCornerOf(above), Vec3.atLowerCornerOf(above.offset(1, 1, 1))), e -> e instanceof ManaSpark);
			return sparks.size() == 1 ? (ManaSpark) sparks.get(0) : null;
		}

		@Override
		public boolean areIncomingTranfersDone() {
			return false;
		}
	}

}
