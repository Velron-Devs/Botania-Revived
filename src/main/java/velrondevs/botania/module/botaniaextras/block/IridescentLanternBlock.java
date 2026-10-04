package velrondevs.botania.module.botaniaextras.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import org.jetbrains.annotations.Nullable;

public class IridescentLanternBlock extends Block {
	public static final MapCodec<IridescentLanternBlock> CODEC = simpleCodec(IridescentLanternBlock::new);
	public static final IntegerProperty POWER = BlockStateProperties.POWER;

	public IridescentLanternBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(POWER, 0));
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWER);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(POWER, context.getLevel().getBestNeighborSignal(context.getClickedPos()));
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
		super.onPlace(state, level, pos, oldState, movedByPiston);
		update(state, level, pos);
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
		super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
		update(state, level, pos);
	}

	private static void update(BlockState state, Level level, BlockPos pos) {
		if (level.isClientSide) {
			return;
		}
		int power = level.getBestNeighborSignal(pos);
		if (state.getValue(POWER) != power) {
			level.setBlock(pos, state.setValue(POWER, power), Block.UPDATE_ALL);
		}
	}
}
