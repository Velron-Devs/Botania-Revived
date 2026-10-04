package velrondevs.botania.api.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.BotaniaAPI;

public interface ManaInfusionRecipe extends Recipe<RecipeInput> {
	ResourceLocation TYPE_ID = ResourceLocation.fromNamespaceAndPath(BotaniaAPI.MODID, "mana_infusion");

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

	@NotNull
	@Override
	default RecipeType<?> getType() {
		return BuiltInRegistries.RECIPE_TYPE.get(TYPE_ID);
	}

	@NotNull
	@Override
	default ItemStack assemble(@NotNull RecipeInput inv, @NotNull HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean matches(@NotNull RecipeInput inv, @NotNull Level world) {
		return false;
	}

	@Override
	default boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Override
	default boolean isSpecial() {
		return true;
	}
}
