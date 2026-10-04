package velrondevs.botania.common.item.material;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.recipe.CustomApothecaryColor;

public class RuneItem extends Item implements CustomApothecaryColor {

	public RuneItem(Item.Properties builder) {
		super(builder);
	}

	@Override
	public int getParticleColor(ItemStack stack) {
		return 0xA8A8A8;
	}

}
