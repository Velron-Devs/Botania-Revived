package velrondevs.botania.common.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.recipe.ElvenTradeRecipe;
import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.item.LexicaBotaniaItem;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LexiconElvenTradeRecipe implements ElvenTradeRecipe {
	public LexiconElvenTradeRecipe() {}

	@Override
	public boolean containsItem(ItemStack stack) {
		return stack.is(BotaniaItems.lexicon) && !ItemNBTHelper.getBoolean(stack, LexicaBotaniaItem.TAG_ELVEN_UNLOCK, false);
	}

	@NotNull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.withSize(1, Ingredient.of(BotaniaItems.lexicon));
	}

	@NotNull
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(BotaniaBlocks.alfPortal);
	}

	@Override
	public List<ItemStack> getOutputs() {
		ItemStack stack = new ItemStack(BotaniaItems.lexicon);
		ItemNBTHelper.setBoolean(stack, LexicaBotaniaItem.TAG_ELVEN_UNLOCK, true);
		return Collections.singletonList(stack);
	}

	@Override
	public Optional<List<ItemStack>> match(List<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			if (containsItem(stack)) {
				return Optional.of(Collections.singletonList(stack));
			}
		}
		return Optional.empty();
	}

	@Override
	public List<ItemStack> getOutputs(List<ItemStack> inputs) {
		ItemStack stack = inputs.get(0).copy();
		ItemNBTHelper.setBoolean(stack, LexicaBotaniaItem.TAG_ELVEN_UNLOCK, true);
		return Collections.singletonList(stack);
	}

	@NotNull
	@Override
	public RecipeSerializer<LexiconElvenTradeRecipe> getSerializer() {
		return BotaniaRecipeTypes.LEXICON_ELVEN_TRADE_SERIALIZER;
	}
}
