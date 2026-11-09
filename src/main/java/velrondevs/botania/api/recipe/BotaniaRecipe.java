package velrondevs.botania.api.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

public interface BotaniaRecipe extends Recipe<RecipeInput> {
	@NotNull
	default ItemStack getResultItem(@NotNull HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	@NotNull
	default NonNullList<Ingredient> getIngredients() {
		return NonNullList.create();
	}

	@NotNull
	default ItemStack assemble(@NotNull RecipeInput input, @NotNull HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	@NotNull
	@Override
	default ItemStack assemble(@NotNull RecipeInput input) {
		return ItemStack.EMPTY;
	}

	@NotNull
	default ItemStack getToastSymbol() {
		return ItemStack.EMPTY;
	}

	default boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Override
	default boolean matches(@NotNull RecipeInput input, @NotNull Level level) {
		return false;
	}

	@Override
	default boolean isSpecial() {
		return true;
	}

	@Override
	default boolean showNotification() {
		return false;
	}

	@NotNull
	@Override
	default String group() {
		return "";
	}

	@NotNull
	@Override
	default PlacementInfo placementInfo() {
		return PlacementInfo.NOT_PLACEABLE;
	}

	@NotNull
	@Override
	default RecipeBookCategory recipeBookCategory() {
		return RecipeBookCategories.CRAFTING_MISC;
	}
}
