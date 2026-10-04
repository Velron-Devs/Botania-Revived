package velrondevs.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.api.internal.ManaBurst;
import velrondevs.botania.api.mana.ManaTrigger;
import velrondevs.botania.common.entity.ManaStormEntity;
import velrondevs.botania.mixin.ProjectileAccessor;
import velrondevs.botania.registry.BotaniaEntities;

public class ManastormChargeBlock extends BotaniaBlock {

	public ManastormChargeBlock(Properties builder) {
		super(builder);
	}

	public static class ManaTriggerImpl implements ManaTrigger {
		private final Level world;
		private final BlockPos pos;
		private final BlockState state;

		public ManaTriggerImpl(Level world, BlockPos pos, BlockState state) {
			this.world = world;
			this.pos = pos;
			this.state = state;
		}

		@Override
		public void onBurstCollision(ManaBurst burst) {
			Projectile entity = burst.entity();
			if (!burst.isFake() && !world.isClientSide()
					&& (!(entity.getOwner() instanceof ServerPlayer player)
							|| !player.blockActionRestricted(world, pos, player.gameMode.getGameModeForPlayer()))
					&& world.destroyBlock(pos, false, entity)) {
				ManaStormEntity storm = BotaniaEntities.MANA_STORM.create(world);
				if (storm == null) {
					return;
				}
				storm.setOwner(entity.getOwner(), ((ProjectileAccessor) entity).botania_getOwnerUUID());
				storm.burstColor = burst.getColor();
				storm.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
				world.addFreshEntity(storm);
			}
		}
	}
}
