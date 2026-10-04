package velrondevs.botania.common.block.red_string;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.block.block_entity.red_string.RedStringBlockEntity;
import velrondevs.botania.common.block.block_entity.red_string.RedStringContainerBlockEntity;
import velrondevs.botania.registry.BotaniaBlockEntities;
import velrondevs.botania.xplat.XplatAbstractions;

public class RedStringContainerBlock extends RedStringBlock {

	public RedStringContainerBlock(BlockBehaviour.Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.FACING, Direction.DOWN));
	}

	@NotNull
	@Override
	public RedStringBlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return XplatAbstractions.INSTANCE.newRedStringContainer(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, BotaniaBlockEntities.RED_STRING_CONTAINER, RedStringContainerBlockEntity::commonTick);
	}

}
