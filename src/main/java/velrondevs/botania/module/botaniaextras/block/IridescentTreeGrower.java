package velrondevs.botania.module.botaniaextras.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;

public final class IridescentTreeGrower {
	private static final int MIN_HEIGHT = 5;

	private IridescentTreeGrower() {}

	private static boolean canReplace(BlockState state) {
		return state.isAir()
				|| state.is(BlockTags.LEAVES)
				|| state.is(BlockTags.LOGS)
				|| state.is(BlockTags.REPLACEABLE_BY_TREES)
				|| state.getBlock() instanceof IridescentSaplingBlock
				|| state.getBlock() instanceof MagicSaplingBlock;
	}

	public static boolean grow(ServerLevel level, BlockPos pos, RandomSource random) {
		BotaniaExtrasWoods.WoodSet set = BotaniaExtrasWoods.forSoil(level.getBlockState(pos.below()).getBlock());
		if (set == null) {
			return false;
		}
		return grow(level, pos, random, set.log(), set.leaves());
	}

	public static boolean grow(ServerLevel level, BlockPos pos, RandomSource random, Block logBlock, Block leavesBlock) {
		int height = random.nextInt(3) + MIN_HEIGHT;
		if (pos.getY() < level.getMinBuildHeight() + 1 || pos.getY() + height + 1 > level.getMaxBuildHeight()) {
			return false;
		}
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		for (int y = pos.getY(); y <= pos.getY() + 1 + height; y++) {
			int radius = 1;
			if (y == pos.getY()) {
				radius = 0;
			}
			if (y >= pos.getY() + 1 + height - 2) {
				radius = 2;
			}
			for (int dx = -radius; dx <= radius; dx++) {
				for (int dz = -radius; dz <= radius; dz++) {
					cursor.set(pos.getX() + dx, y, pos.getZ() + dz);
					if (!canReplace(level.getBlockState(cursor))) {
						return false;
					}
				}
			}
		}

		BlockState leaves = leavesBlock.defaultBlockState().setValue(LeavesBlock.DISTANCE, 1);
		BlockState log = logBlock.defaultBlockState();
		for (int y = pos.getY() - 3 + height; y <= pos.getY() + height; y++) {
			int layer = y - (pos.getY() + height);
			int radius = 1 - layer / 2;
			for (int dx = -radius; dx <= radius; dx++) {
				for (int dz = -radius; dz <= radius; dz++) {
					if (Math.abs(dx) != radius || Math.abs(dz) != radius || random.nextInt(2) != 0 && layer != 0) {
						cursor.set(pos.getX() + dx, y, pos.getZ() + dz);
						BlockState existing = level.getBlockState(cursor);
						if (existing.isAir() || existing.is(BlockTags.LEAVES) || existing.is(BlockTags.REPLACEABLE_BY_TREES)) {
							level.setBlock(cursor, leaves, Block.UPDATE_ALL);
							level.scheduleTick(cursor.immutable(), leavesBlock, 1);
						}
					}
				}
			}
		}
		for (int k = 0; k < height; k++) {
			BlockPos trunk = pos.above(k);
			if (canReplace(level.getBlockState(trunk))) {
				level.setBlock(trunk, log, Block.UPDATE_ALL);
			}
		}
		return true;
	}
}
