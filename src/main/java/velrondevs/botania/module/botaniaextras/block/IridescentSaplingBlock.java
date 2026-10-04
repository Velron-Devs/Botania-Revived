package velrondevs.botania.module.botaniaextras.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;

public class IridescentSaplingBlock extends BushBlock implements BonemealableBlock {
	public static final MapCodec<IridescentSaplingBlock> CODEC = simpleCodec(IridescentSaplingBlock::new);
	public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
	private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0);

	public IridescentSaplingBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(STAGE, 0));
	}

	@Override
	protected MapCodec<? extends BushBlock> codec() {
		return CODEC;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return super.mayPlaceOn(state, level, pos) || BotaniaExtrasWoods.isSoil(state.getBlock());
	}

	private static boolean canGrowOn(BlockGetter level, BlockPos pos) {
		return BotaniaExtrasWoods.isSoil(level.getBlockState(pos.below()).getBlock());
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (level.getMaxLocalRawBrightness(pos.above()) >= 9 && random.nextInt(7) == 0) {
			advance(level, pos, state, random);
		}
	}

	private void advance(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
		if (!canGrowOn(level, pos)) {
			return;
		}
		if (state.getValue(STAGE) == 0) {
			level.setBlock(pos, state.setValue(STAGE, 1), Block.UPDATE_NONE);
		} else {
			IridescentTreeGrower.grow(level, pos, random);
		}
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		return canGrowOn(level, pos);
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
		return level.random.nextFloat() < 0.45F;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		advance(level, pos, state, random);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(STAGE);
	}
}
