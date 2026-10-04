package velrondevs.botania.common.block.decor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.block.BotaniaFlowerBlock;

public class GlimmeringFlowerBlock extends BotaniaFlowerBlock {

	public GlimmeringFlowerBlock(DyeColor color, Properties builder) {
		super(color, builder);
	}

	@Override
	public boolean isValidBonemealTarget(@NotNull LevelReader world, @NotNull BlockPos pos, @NotNull BlockState state) {
		return false;
	}

}
