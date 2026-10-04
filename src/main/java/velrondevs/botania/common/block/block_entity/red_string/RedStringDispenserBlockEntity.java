package velrondevs.botania.common.block.block_entity.red_string;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.registry.BotaniaBlockEntities;

public class RedStringDispenserBlockEntity extends RedStringContainerBlockEntity {
	public RedStringDispenserBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.RED_STRING_DISPENSER, pos, state);
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return level.getBlockEntity(pos) instanceof DispenserBlockEntity;
	}

	public void tickDispenser() {
		BlockPos bind = getBinding();
		if (bind != null) {
			BlockEntity tile = level.getBlockEntity(bind);
			if (tile instanceof DispenserBlockEntity) {
				level.scheduleTick(bind, tile.getBlockState().getBlock(), 4);
			}
		}
	}

}
