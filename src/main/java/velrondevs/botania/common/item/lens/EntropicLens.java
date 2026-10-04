package velrondevs.botania.common.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import velrondevs.botania.api.internal.ManaBurst;
import velrondevs.botania.registry.BotaniaBlocks;

public class EntropicLens extends Lens {

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		ThrowableProjectile entity = burst.entity();
		if (pos.getType() == HitResult.Type.BLOCK) {

			BlockPos hit = ((BlockHitResult) pos).getBlockPos();
			if (entity.level().getBlockState(hit).is(BotaniaBlocks.pistonRelay)) {
				return shouldKill;
			}

			if (!entity.level().isClientSide && !burst.isFake() && !isManaBlock) {
				entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(),
						burst.getMana() / 50F, Level.ExplosionInteraction.TNT);
			}
			return true;
		}
		return shouldKill;
	}

}
