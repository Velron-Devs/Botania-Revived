package velrondevs.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.common.item.equipment.bauble.TinyPlanetItem;
import velrondevs.botania.registry.BotaniaBlockEntities;

public class TinyPlanetBlockEntity extends BotaniaBlockEntity {
	public TinyPlanetBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.TINY_PLANET, pos, state);
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, TinyPlanetBlockEntity self) {
		TinyPlanetItem.applyEffect(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5);
	}

}
