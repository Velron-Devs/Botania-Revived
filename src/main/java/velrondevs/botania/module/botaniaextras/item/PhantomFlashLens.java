package velrondevs.botania.module.botaniaextras.item;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import velrondevs.botania.api.internal.ManaBurst;
import velrondevs.botania.common.item.lens.Lens;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.block.ManaFlashBlockEntity;
import velrondevs.botania.registry.BotaniaBlocks;

public class PhantomFlashLens extends Lens {
	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();
		if (!entity.level().isClientSide && pos.getType() == HitResult.Type.BLOCK && !burst.isFake() && !isManaBlock) {
			BlockHitResult hit = (BlockHitResult) pos;
			BlockPos blockPos = hit.getBlockPos();
			BlockPos neighborPos = blockPos.relative(hit.getDirection());
			BlockState stateAt = entity.level().getBlockState(blockPos);
			BlockState neighbor = entity.level().getBlockState(neighborPos);

			if ((stateAt.is(BotaniaBlocks.manaFlame) || stateAt.is(BotaniaExtrasBlocks.phantomManaFlash)) && entity.mayInteract(entity.level(), blockPos)) {
				entity.level().destroyBlock(blockPos, false, entity);
			} else if ((neighbor.isAir() || neighbor.canBeReplaced()) && entity.mayInteract(entity.level(), neighborPos)) {
				var fluid = entity.level().getFluidState(neighborPos);
				boolean water = fluid.isSource() && fluid.is(FluidTags.WATER);
				entity.level().setBlockAndUpdate(neighborPos,
						BotaniaExtrasBlocks.phantomManaFlash.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, water));
				if (entity.level().getBlockEntity(neighborPos) instanceof ManaFlashBlockEntity flash) {
					flash.setColor(burst.getColor());
				}
			}
		}
		return shouldKill;
	}
}
