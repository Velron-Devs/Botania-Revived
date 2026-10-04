package velrondevs.botania.common.block.block_entity.red_string;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.registry.BotaniaBlockEntities;
import velrondevs.botania.xplat.XplatAbstractions;

public class RedStringContainerBlockEntity extends RedStringBlockEntity {
	public RedStringContainerBlockEntity(BlockPos pos, BlockState state) {
		this(BotaniaBlockEntities.RED_STRING_CONTAINER, pos, state);
	}

	protected RedStringContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		BlockEntity tile = level.getBlockEntity(pos);
		return tile != null && XplatAbstractions.INSTANCE.isRedStringContainerTarget(tile);
	}
}
