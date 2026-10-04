package velrondevs.botania.module.botaniaextras.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.util.TriState;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;

import java.util.List;

public class IridescentDirtBlock extends Block implements BonemealableBlock {
	public static final MapCodec<IridescentDirtBlock> CODEC = simpleCodec(p -> new IridescentDirtBlock(null, p));

	@Nullable
	private final DyeColor color;

	public IridescentDirtBlock(@Nullable DyeColor color, Properties properties) {
		super(properties);
		this.color = color;
	}

	@Nullable
	public DyeColor getColor() {
		return color;
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return CODEC;
	}

	@Override
	public TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos soilPosition, Direction facing, BlockState plant) {
		return facing == Direction.UP ? TriState.TRUE : TriState.DEFAULT;
	}

	@Override
	public boolean isFertile(BlockState state, BlockGetter level, BlockPos pos) {
		return true;
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		BlockPos start = pos.above();
		attempts:
		for (int attempt = 0; attempt < 128; attempt++) {
			BlockPos target = start;
			for (int step = 0; step < attempt / 16; step++) {
				target = target.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
				if (!(level.getBlockState(target.below()).getBlock() instanceof IridescentDirtBlock)
						|| level.getBlockState(target).isCollisionShapeFullBlock(level, target)) {
					continue attempts;
				}
			}
			if (!level.getBlockState(target).isAir()) {
				continue;
			}
			if (random.nextInt(8) != 0) {
				if (level.getBlockState(target.below()).getBlock() instanceof IridescentDirtBlock dirt) {
					BlockState grass = BotaniaExtrasBlocks.getGrass(dirt.getColor()).defaultBlockState();
					if (grass.canSurvive(level, target)) {
						level.setBlock(target, grass, Block.UPDATE_ALL);
					}
				}
			} else {
				List<ConfiguredFeature<?, ?>> flowers = level.getBiome(target).value().getGenerationSettings().getFlowerFeatures();
				if (!flowers.isEmpty()) {
					Holder<PlacedFeature> feature = ((RandomPatchConfiguration) flowers.get(0).config()).feature();
					feature.value().place(level, level.getChunkSource().getGenerator(), random, target);
				}
			}
		}
	}
}
