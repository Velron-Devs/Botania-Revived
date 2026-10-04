package velrondevs.botania.common.block.block_entity.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.api.corporea.CorporeaHelper;
import velrondevs.botania.api.corporea.CorporeaSpark;
import velrondevs.botania.common.block.block_entity.BotaniaBlockEntity;

public abstract class BaseCorporeaBlockEntity extends BotaniaBlockEntity {

	protected BaseCorporeaBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public CorporeaSpark getSpark() {
		return CorporeaHelper.instance().getSparkForBlock(level, getBlockPos());
	}

}
