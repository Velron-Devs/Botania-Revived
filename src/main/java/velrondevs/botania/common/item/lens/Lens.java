package velrondevs.botania.common.item.lens;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;

import velrondevs.botania.api.internal.ManaBurst;
import velrondevs.botania.api.mana.BurstProperties;
import velrondevs.botania.api.mana.ManaReceiver;
import velrondevs.botania.api.mana.ManaSpreader;

public class Lens {

	public void apply(ItemStack stack, BurstProperties props) {}

	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		return shouldKill;
	}

	public void updateBurst(ManaBurst burst, ItemStack stack) {}

	public boolean allowBurstShooting(ItemStack stack, ManaSpreader spreader, boolean redstone) {
		return true;
	}

	public void onControlledSpreaderTick(ItemStack stack, ManaSpreader spreader, boolean redstone) {}

	public void onControlledSpreaderPulse(ItemStack stack, ManaSpreader spreader) {}

	public int getManaToTransfer(ManaBurst burst, ItemStack stack, ManaReceiver receiver) {
		return burst.getMana();
	}

}
