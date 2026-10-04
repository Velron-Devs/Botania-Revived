package velrondevs.botania.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.item.LaputaShardItem;
import velrondevs.botania.registry.BotaniaItems;

public class LaputaShardUpgradeRecipe extends CustomRecipe {
	public static final RecipeSerializer<LaputaShardUpgradeRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(LaputaShardUpgradeRecipe::new);

	public LaputaShardUpgradeRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(@NotNull CraftingInput inv, @NotNull Level worldIn) {
		boolean foundShard = false;
		boolean foundSpirit = false;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (stack.isEmpty()) {
				continue;
			}
			if (stack.is(BotaniaItems.laputaShard) && !foundShard
					&& LaputaShardItem.getShardLevel(stack) < 19) {
				foundShard = true;
			} else if (stack.is(BotaniaItems.lifeEssence) && !foundSpirit) {
				foundSpirit = true;
			} else {
				return false;
			}
		}
		return foundShard && foundSpirit;
	}

	@NotNull
	@Override
	public ItemStack getResultItem(@NotNull HolderLookup.Provider registries) {
		return new ItemStack(BotaniaItems.laputaShard);
	}

	@NotNull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY,
				Ingredient.of(BotaniaItems.laputaShard),
				Ingredient.of(BotaniaItems.lifeEssence));
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv, @NotNull HolderLookup.Provider registries) {
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (stack.is(BotaniaItems.laputaShard)) {
				ItemStack result = stack.copy();
				ItemNBTHelper.setInt(result, LaputaShardItem.TAG_LEVEL, LaputaShardItem.getShardLevel(stack) + 1);
				return result;
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
