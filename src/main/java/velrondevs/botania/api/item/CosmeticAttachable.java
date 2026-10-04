package velrondevs.botania.api.item;

import net.minecraft.world.item.ItemStack;

public interface CosmeticAttachable {

	ItemStack getCosmeticItem(ItemStack stack);

	void setCosmeticItem(ItemStack stack, ItemStack cosmetic);

}
