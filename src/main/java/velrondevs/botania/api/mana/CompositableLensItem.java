package velrondevs.botania.api.mana;

import net.minecraft.world.item.ItemStack;

public interface CompositableLensItem extends BasicLensItem {

	int getProps(ItemStack stack);

	boolean isCombinable(ItemStack stack);

}
