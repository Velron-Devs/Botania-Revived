package velrondevs.botania.module.botaniaextras.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import velrondevs.botania.module.botaniaextras.BotaniaExtrasMagicWoods;

public class MagicSaplingBlock extends BushBlock implements BonemealableBlock {
	public static final MapCodec<MagicSaplingBlock> CODEC = simpleCodec(p -> new MagicSaplingBlock(p, BotaniaExtrasMagicWoods.Type.THUNDEROUS));
	public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
	private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0);

	private final BotaniaExtrasMagicWoods.Type type;

	public MagicSaplingBlock(Properties properties, BotaniaExtrasMagicWoods.Type type) {
		super(properties);
		this.type = type;
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

	private static boolean canGrowOn(BlockGetter level, BlockPos pos) {
		BlockState below = level.getBlockState(pos.below());
		return below.is(BlockTags.DIRT) || below.is(Blocks.FARMLAND);
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
			BotaniaExtrasMagicWoods.MagicSet set = BotaniaExtrasMagicWoods.get(type);
			IridescentTreeGrower.grow(level, pos, random, set.log(), set.leaves());
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
