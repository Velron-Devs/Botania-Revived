package velrondevs.botania.api.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.BotaniaAPI;

public interface ManaInfusionRecipe extends BotaniaRecipe {
	Identifier TYPE_ID = Identifier.fromNamespaceAndPath(BotaniaAPI.MODID, "mana_infusion");

	boolean matches(ItemStack stack);

	@NotNull
	@Override
	ItemStack getResultItem(@NotNull HolderLookup.Provider registries);

	@NotNull
	default ItemStack getRecipeOutput(@NotNull HolderLookup.Provider registries, @NotNull ItemStack input) {
		return getResultItem(registries).copy();
	}

	@Nullable
	StateIngredient getRecipeCatalyst();

	int getManaToConsume();

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	default RecipeType<? extends ManaInfusionRecipe> getType() {
		return (RecipeType<? extends ManaInfusionRecipe>) BuiltInRegistries.RECIPE_TYPE.getValue(TYPE_ID);
	}
}
