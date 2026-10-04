package velrondevs.botania.api.mana;

import net.minecraft.world.item.ItemStack;

public interface ControlLensItem extends BasicLensItem {

	boolean isControlLens(ItemStack stack);

	boolean allowBurstShooting(ItemStack stack, ManaSpreader spreader, boolean redstone);

	void onControlledSpreaderTick(ItemStack stack, ManaSpreader spreader, boolean redstone);

	void onControlledSpreaderPulse(ItemStack stack, ManaSpreader spreader);

}
