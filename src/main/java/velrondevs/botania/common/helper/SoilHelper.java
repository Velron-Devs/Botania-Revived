package velrondevs.botania.common.helper;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class SoilHelper {
	private SoilHelper() {}

	public static boolean isDirtLike(BlockState state) {
		return state.is(BlockTags.DIRT)
				|| state.is(BlockTags.GRASS_BLOCKS)
				|| state.is(Blocks.PODZOL)
				|| state.is(Blocks.MYCELIUM)
				|| state.is(Blocks.MOSS_BLOCK)
				|| state.is(Blocks.MUD)
				|| state.is(Blocks.MUDDY_MANGROVE_ROOTS);
	}
}
