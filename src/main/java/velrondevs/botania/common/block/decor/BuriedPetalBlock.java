package velrondevs.botania.common.block.decor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.client.fx.SparkleParticleData;
import velrondevs.botania.common.item.material.MysticalPetalItem;
import velrondevs.botania.registry.BotaniaBlocks;

public class BuriedPetalBlock extends BushBlock implements BonemealableBlock {

	private static final VoxelShape SHAPE = box(0, 0, 0, 16, 1.6, 16);

	public final DyeColor color;
	public static final MapCodec<BuriedPetalBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			DyeColor.CODEC.fieldOf("color").forGetter(b -> b.color),
			propertiesCodec()
	).apply(instance, BuriedPetalBlock::new));

	public BuriedPetalBlock(DyeColor color, Properties builder) {
		super(builder);
		this.color = color;
	}

	@NotNull
	@Override
	protected MapCodec<BuriedPetalBlock> codec() {
		return CODEC;
	}

	@NotNull
	@Override
	public VoxelShape getShape(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		int hex = MysticalPetalItem.getPetalLikeColor(color);
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;

		SparkleParticleData data = SparkleParticleData.noClip(rand.nextFloat(), r / 255F, g / 255F, b / 255F, 5);
		world.addParticle(data, pos.getX() + 0.3 + rand.nextFloat() * 0.5, pos.getY() + 0.1 + rand.nextFloat() * 0.1, pos.getZ() + 0.3 + rand.nextFloat() * 0.5, 0, 0, 0);
	}

	@NotNull
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public boolean isValidBonemealTarget(@NotNull LevelReader world, @NotNull BlockPos pos, @NotNull BlockState state) {
		return world.getBlockState(pos.above()).isAir();
	}

	@Override
	public boolean isBonemealSuccess(@NotNull Level world, @NotNull RandomSource rand, @NotNull BlockPos pos, @NotNull BlockState state) {
		return isValidBonemealTarget(world, pos, state);
	}

	@Override
	public void performBonemeal(@NotNull ServerLevel world, @NotNull RandomSource rand, @NotNull BlockPos pos, @NotNull BlockState state) {
		Block block = BotaniaBlocks.getDoubleFlower(color);
		if (block instanceof DoublePlantBlock) {
			DoublePlantBlock.placeAt(world, block.defaultBlockState(), pos, 3);
		}
	}
}
