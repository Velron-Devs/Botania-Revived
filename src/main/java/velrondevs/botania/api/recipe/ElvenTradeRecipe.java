package velrondevs.botania.api.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;

import java.util.List;
import java.util.Optional;

public interface ElvenTradeRecipe extends Recipe<RecipeInput> {
	ResourceLocation TYPE_ID = ResourceLocation.fromNamespaceAndPath(BotaniaAPI.MODID, "elven_trade");

	Optional<List<ItemStack>> match(List<ItemStack> stacks);

	boolean containsItem(ItemStack stack);

	@NotNull
	@Override
	NonNullList<Ingredient> getIngredients();

	List<ItemStack> getOutputs();

	List<ItemStack> getOutputs(List<ItemStack> inputs);

	@NotNull
	@Override
	default RecipeType<?> getType() {
		return BuiltInRegistries.RECIPE_TYPE.get(TYPE_ID);
	}

	@Override
	default boolean matches(@NotNull RecipeInput inv, @NotNull Level world) {
		return false;
	}

	@NotNull
	@Override
	default ItemStack assemble(@NotNull RecipeInput inv, @NotNull HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Override
	default ItemStack getResultItem(@NotNull HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean isSpecial() {
		return true;
	}

	default boolean isReturnRecipe() {
		return this.getOutputs().size() == 1
				&& this.getIngredients().size() == 1
				&& this.containsItem(this.getOutputs().get(0));
	}
}
