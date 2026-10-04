package velrondevs.botania.api.item;

import net.minecraft.world.item.ItemStack;

public interface PhantomInkable {

	boolean hasPhantomInk(ItemStack stack);

	void setPhantomInk(ItemStack stack, boolean ink);

}
