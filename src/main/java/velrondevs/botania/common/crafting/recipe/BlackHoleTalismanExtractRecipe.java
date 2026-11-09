package velrondevs.botania.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.item.BlackHoleTalismanItem;
import velrondevs.botania.registry.BotaniaItems;

public class BlackHoleTalismanExtractRecipe extends CustomRecipe {
	public static final RecipeSerializer<BlackHoleTalismanExtractRecipe> SERIALIZER = RecipeUtils.simpleSerializer(BlackHoleTalismanExtractRecipe::new);

	public BlackHoleTalismanExtractRecipe() {}

	@Override
	public boolean matches(@NotNull CraftingInput inv, @NotNull Level world) {
		boolean foundTalisman = false;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.is(BotaniaItems.blackHoleTalisman) && !foundTalisman) {

					int count = BlackHoleTalismanItem.getBlockCount(stack);
					if (count <= 0) {
						return false;
					}

					foundTalisman = true;
				} else {
					return false;
				}
			}
		}

		return foundTalisman;
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv) {
		ItemStack talisman = ItemStack.EMPTY;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				talisman = stack;
			}
		}

		int count = BlackHoleTalismanItem.getBlockCount(talisman);
		if (count > 0) {
			Block block = BlackHoleTalismanItem.getBlock(talisman);
			if (block != null) {
				return new ItemStack(block, Math.min(64, count));
			}
		}

		return ItemStack.EMPTY;
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
			if (s.is(BotaniaItems.blackHoleTalisman)) {
				int count = BlackHoleTalismanItem.getBlockCount(s);
				if (count == 0) {
					return ItemStack.EMPTY;
				}

				int extract = Math.min(64, count);
				ItemStack copy = s.copyWithCount(1);
				BlackHoleTalismanItem.remove(copy, extract);
				ItemNBTHelper.setBoolean(copy, BlackHoleTalismanItem.TAG_ACTIVE, false);

				return copy;
			}
			return null;
		});
	}
}
