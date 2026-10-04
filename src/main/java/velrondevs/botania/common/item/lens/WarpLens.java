package velrondevs.botania.common.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import velrondevs.botania.api.internal.ManaBurst;
import velrondevs.botania.common.block.ForceRelayBlock;
import velrondevs.botania.common.helper.EntityHelper;
import velrondevs.botania.registry.BotaniaBlocks;

public class WarpLens extends Lens {

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();
		Level world = entity.level();

		if (world.isClientSide || pos.getType() != HitResult.Type.BLOCK) {

			return shouldKill;
		}

		BlockPos hit = ((BlockHitResult) pos).getBlockPos();
		if (world.getBlockState(hit).is(BotaniaBlocks.pistonRelay)) {
			ForceRelayBlock.WorldData data = ForceRelayBlock.WorldData.get(world);
			BlockPos dest = data.mapping.get(hit);

			if (dest != null) {
				BlockPos sourcePos = entity.blockPosition();
				entity.setPos(dest.getCenter());
				EntityHelper.addTeleportTicketIfFarAway(entity, sourcePos);
				burst.setCollidedAt(dest);

				burst.setWarped(true);

				return false;
			}
		}
		return shouldKill;
	}
}
