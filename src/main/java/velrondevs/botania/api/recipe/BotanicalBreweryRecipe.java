package velrondevs.botania.api.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.brew.Brew;

public interface BotanicalBreweryRecipe extends BotaniaRecipe {
	Identifier TYPE_ID = Identifier.fromNamespaceAndPath(BotaniaAPI.MODID, "brew");

	Brew getBrew();

	int getManaUsage();

	ItemStack getOutput(ItemStack container);

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	default RecipeType<? extends BotanicalBreweryRecipe> getType() {
		return (RecipeType<? extends BotanicalBreweryRecipe>) BuiltInRegistries.RECIPE_TYPE.getValue(TYPE_ID);
	}
}
