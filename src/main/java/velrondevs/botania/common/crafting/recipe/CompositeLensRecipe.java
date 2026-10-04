package velrondevs.botania.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.mana.CompositableLensItem;
import velrondevs.botania.common.lib.BotaniaTags;

public class CompositeLensRecipe extends CustomRecipe {
	public static final RecipeSerializer<CompositeLensRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(CompositeLensRecipe::new);

	public CompositeLensRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(@NotNull CraftingInput inv, @NotNull Level world) {
		boolean foundLens = false;
		boolean foundSecondLens = false;
		boolean foundGlue = false;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof CompositableLensItem && !foundSecondLens) {
					if (foundLens) {
						foundSecondLens = true;
					} else {
						foundLens = true;
					}
				} else if (stack.is(BotaniaTags.Items.LENS_GLUE) && !foundGlue) {
					foundGlue = true;
				} else {
					return false;
				}
			}
		}

		return foundSecondLens && foundGlue;
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv, @NotNull HolderLookup.Provider registries) {
		ItemStack lens = ItemStack.EMPTY;
		ItemStack secondLens = ItemStack.EMPTY;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof CompositableLensItem) {
					if (lens.isEmpty()) {
						lens = stack;
					} else {
						secondLens = stack;
					}
				}
			}
		}

		if (lens.getItem() instanceof CompositableLensItem lensItem) {
			if (secondLens.isEmpty() || !lensItem.canCombineLenses(lens, secondLens) || !lensItem.getCompositeLens(lens).isEmpty() || !lensItem.getCompositeLens(secondLens).isEmpty()) {
				return ItemStack.EMPTY;
			}

			ItemStack lensCopy = lens.copyWithCount(1);
			ItemStack secondCopy = secondLens.copyWithCount(1);
			lensItem.setCompositeLens(lensCopy, secondCopy);

			return lensCopy;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 3;
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
