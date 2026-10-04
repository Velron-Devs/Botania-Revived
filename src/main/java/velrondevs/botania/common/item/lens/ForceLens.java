package velrondevs.botania.common.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import velrondevs.botania.api.internal.ManaBurst;
import velrondevs.botania.common.helper.ForcePushHelper;
import velrondevs.botania.mixin.PistonBaseBlockAccessor;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaItems;

public class ForceLens extends Lens {

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();
		if (pos.getType() == HitResult.Type.BLOCK
				&& !burst.isFake()
				&& !isManaBlock) {
			BlockHitResult rtr = (BlockHitResult) pos;
			BlockPos blockPos = rtr.getBlockPos();
			BlockState state = entity.level().getBlockState(blockPos);
			ItemStack sourceLens = burst.getSourceLens();
			boolean isWarp = sourceLens.is(BotaniaItems.lensWarp);
			if (isWarp && state.is(BotaniaBlocks.pistonRelay)) {

				return false;
			}
			if (entity.mayInteract(entity.level(), blockPos)) {

				moveBlocks(entity.level(), blockPos.relative(rtr.getDirection()), rtr.getDirection().getOpposite(),
						ManaBurst.NO_SOURCE);
			}
		}

		return shouldKill;
	}

	@SuppressWarnings("try")
	public static boolean moveBlocks(Level level, BlockPos impliedPistonPos, Direction direction, BlockPos pushSourcePos) {
		try (var ignored = new ForcePushHelper(pushSourcePos)) {
			return ((PistonBaseBlockAccessor) Blocks.PISTON).botania_moveBlocks(level, impliedPistonPos, direction, true);
		}
	}

}
