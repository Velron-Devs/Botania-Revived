package velrondevs.botania.common.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.registry.BotaniaEntities;

public final class SleepingHandler {

	private SleepingHandler() {}

	@Nullable
	public static Player.BedSleepingProblem trySleep(Player player, BlockPos sleepPos) {
		Level world = player.level();
		if (!world.isClientSide()) {
			var entities = ((ServerLevel) world).getEntities(BotaniaEntities.DOPPLEGANGER, EntitySelector.ENTITY_STILL_ALIVE);
			for (var entity : entities) {
				if (entity.getPlayersAround().contains(player)) {
					return Player.BedSleepingProblem.NOT_SAFE;
				}
			}
		}
		return null;
	}
}
