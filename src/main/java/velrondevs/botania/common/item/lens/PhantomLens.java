package velrondevs.botania.common.item.lens;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;

import velrondevs.botania.api.internal.ManaBurst;

public class PhantomLens extends Lens {

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		if (!isManaBlock) {
			shouldKill = false;
			burst.setMinManaLoss(Math.max(0, burst.getMinManaLoss() - 4));
		}

		return shouldKill;
	}

}
