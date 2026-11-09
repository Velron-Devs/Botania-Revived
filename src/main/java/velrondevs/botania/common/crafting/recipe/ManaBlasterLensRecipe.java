package velrondevs.botania.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.mana.BasicLensItem;
import velrondevs.botania.common.item.ManaBlasterItem;

public class ManaBlasterLensRecipe extends CustomRecipe {
	public static final RecipeSerializer<ManaBlasterLensRecipe> SERIALIZER = RecipeUtils.simpleSerializer(ManaBlasterLensRecipe::new);

	public ManaBlasterLensRecipe() {}

	@Override
	public boolean matches(@NotNull CraftingInput inv, @NotNull Level world) {
		int foundLens = 0;
		int foundGun = 0;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ManaBlasterItem && ManaBlasterItem.getLens(stack).isEmpty()) {
					foundGun++;
				} else if (ManaBlasterItem.isValidLens(stack)) {
					foundLens++;
				} else {
					return false;
				}
			}
		}

		return foundLens == 1 && foundGun == 1;
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv) {
		ItemStack lens = ItemStack.EMPTY;
		ItemStack gun = ItemStack.EMPTY;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ManaBlasterItem) {
					gun = stack;
				} else if (stack.getItem() instanceof BasicLensItem) {
					lens = stack.copyWithCount(1);
				}
			}
		}

		if (lens.isEmpty() || gun.isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack gunCopy = gun.copy();
		ManaBlasterItem.setLens(gunCopy, lens);

		return gunCopy;
	}

	@NotNull
	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return SERIALIZER;
	}
}
