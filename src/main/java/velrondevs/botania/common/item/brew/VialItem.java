package velrondevs.botania.common.item.brew;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.brew.Brew;
import velrondevs.botania.api.brew.BrewContainer;
import velrondevs.botania.registry.BotaniaItems;

public class VialItem extends Item implements BrewContainer {

	public VialItem(Properties builder) {
		super(builder);
	}

	@Override
	public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
		ItemStack brewStack = new ItemStack(stack.is(BotaniaItems.flask) ? BotaniaItems.brewFlask : BotaniaItems.brewVial);
		BaseBrewItem.setBrew(brewStack, brew);
		return brewStack;
	}

	@Override
	public int getManaCost(Brew brew, ItemStack stack) {
		if (stack.is(BotaniaItems.flask)) {
			return brew.getManaCost() * 2;
		} else {
			return brew.getManaCost();
		}
	}
}
