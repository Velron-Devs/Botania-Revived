package velrondevs.botania.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.item.ManaBlasterItem;
import velrondevs.botania.registry.BotaniaItems;

public class ManaBlasterRemoveLensRecipe extends CustomRecipe {
	public static final RecipeSerializer<ManaBlasterRemoveLensRecipe> SERIALIZER = RecipeUtils.simpleSerializer(ManaBlasterRemoveLensRecipe::new);

	public ManaBlasterRemoveLensRecipe() {}

	@Override
	public boolean matches(@NotNull CraftingInput inv, @NotNull Level world) {
		boolean foundGun = false;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ManaBlasterItem
						&& !ManaBlasterItem.getLens(stack).isEmpty() && !foundGun) {
					foundGun = true;
				} else {
					return false;
				}
			}
		}

		return foundGun;
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv) {
		ItemStack gun = ItemStack.EMPTY;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ManaBlasterItem) {
					gun = stack;
				}
			}
		}

		ItemStack gunCopy = gun.copyWithCount(1);
		ManaBlasterItem.setLens(gunCopy, ItemStack.EMPTY);

		return gunCopy;
	}

	@NotNull
	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return SERIALIZER;
	}

	@NotNull
	@Override
	public NonNullList<ItemStack> getRemainingItems(@NotNull CraftingInput inv) {
		return RecipeUtils.getRemainingItemsSub(inv, s -> {
			if (s.is(BotaniaItems.manaGun)) {
				ItemStack stack = ManaBlasterItem.getLens(s);
				stack.setCount(1);
				return stack;
			}
			return null;
		});
	}
}
