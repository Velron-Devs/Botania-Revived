package velrondevs.botania.common.block.block_entity;

import net.minecraft.server.level.ServerLevel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.ServerLevelData;

import velrondevs.botania.registry.BotaniaBlockEntities;

public class TeruTeruBozuBlockEntity extends BotaniaBlockEntity {
	private boolean wasRaining = false;

	public TeruTeruBozuBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.TERU_TERU_BOZU, pos, state);
	}

	public static void serverTick(Level level, BlockPos worldPosition, BlockState state, TeruTeruBozuBlockEntity self) {
		boolean isRaining = level.isRaining();
		if (isRaining && level.getRandom().nextInt(9600) == 0) {
			setRaining(level, false);
			resetRainTime(level);
		}

		if (self.wasRaining != isRaining) {
			level.updateNeighbourForOutputSignal(worldPosition, state.getBlock());
		}
		self.wasRaining = isRaining;
	}

	public static void setRaining(Level w, boolean raining) {
		if (w instanceof ServerLevel serverLevel) {
			serverLevel.getWeatherData().setRaining(raining);
		}
	}

	public static void resetRainTime(Level w) {
		if (w instanceof ServerLevel serverLevel) {
			var data = serverLevel.getWeatherData();
			data.setRainTime(w.getRandom().nextInt(data.isRaining() ? 12000 : 168000) + 12000);
		}
	}
}
