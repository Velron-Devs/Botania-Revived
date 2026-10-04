package velrondevs.botania.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.client.fx.SparkleParticleData;
import velrondevs.botania.common.block.decor.BotaniaGlassBlock;

public class PermanentBifrostBlock extends BotaniaGlassBlock implements BeaconBeamBlock {
	public static final MapCodec<PermanentBifrostBlock> CODEC = simpleCodec(PermanentBifrostBlock::new);

	public PermanentBifrostBlock(Properties builder) {
		super(builder);
	}

	@Override
	protected MapCodec<? extends PermanentBifrostBlock> codec() {
		return CODEC;
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		if (rand.nextBoolean()) {
			SparkleParticleData data = SparkleParticleData.sparkle(0.45F + 0.2F * (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 6);
			world.addParticle(data, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0);
		}
	}

	@Override
	public DyeColor getColor() {
		return DyeColor.WHITE;
	}

	@Override
	public Integer getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
		int rgb = Mth.hsvToRgb(((Level) level).getGameTime() * 5 % 360 / 360F, 0.4F, 0.9F);
		return 0xFF000000 | rgb;
	}
}
