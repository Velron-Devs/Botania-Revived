package velrondevs.botania.api.brew;

import net.minecraft.world.item.ItemStack;

public interface BrewContainer {

	ItemStack getItemForBrew(Brew brew, ItemStack stack);

	int getManaCost(Brew brew, ItemStack stack);

}
