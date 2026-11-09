package velrondevs.botania.api.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;

import java.util.List;
import java.util.Optional;

public interface ElvenTradeRecipe extends BotaniaRecipe {
	Identifier TYPE_ID = Identifier.fromNamespaceAndPath(BotaniaAPI.MODID, "elven_trade");

	Optional<List<ItemStack>> match(List<ItemStack> stacks);

	boolean containsItem(ItemStack stack);

	@NotNull
	@Override
	NonNullList<Ingredient> getIngredients();

	List<ItemStack> getOutputs();

	List<ItemStack> getOutputs(List<ItemStack> inputs);

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	default RecipeType<? extends ElvenTradeRecipe> getType() {
		return (RecipeType<? extends ElvenTradeRecipe>) BuiltInRegistries.RECIPE_TYPE.getValue(TYPE_ID);
	}

	default boolean isReturnRecipe() {
		return this.getOutputs().size() == 1
				&& this.getIngredients().size() == 1
				&& this.containsItem(this.getOutputs().get(0));
	}
}
