package velrondevs.botania.common.world;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.block.BotaniaFlowerBlock;
import velrondevs.botania.registry.BotaniaBlocks;

public class MysticalFlowerFeature extends Feature<MysticalFlowerConfig> {
	public MysticalFlowerFeature(Codec<MysticalFlowerConfig> codec) {
		super(codec);
	}

	@Override
	public boolean place(@NotNull FeaturePlaceContext<MysticalFlowerConfig> ctx) {
		MysticalFlowerConfig config = ctx.config();
		WorldGenLevel level = ctx.level();
		BlockPos pos = ctx.origin();
		BlockState state = config.toPlace().getState(ctx.random(), pos);
		if (state.canSurvive(level, pos)) {
			if (state.getBlock().getClass() == BotaniaFlowerBlock.class
					&& ctx.random().nextFloat() < config.tallChance()) {
				if (!level.isEmptyBlock(pos.above())) {
					return false;
				}

				var color = ((BotaniaFlowerBlock) state.getBlock()).color;
				var doubleFlower = BotaniaBlocks.getDoubleFlower(color);
				DoublePlantBlock.placeAt(level, doubleFlower.defaultBlockState(), pos, Block.UPDATE_CLIENTS);
			} else {
				level.setBlock(pos, state, Block.UPDATE_CLIENTS);
			}

			return true;
		} else {
			return false;
		}
	}
}
