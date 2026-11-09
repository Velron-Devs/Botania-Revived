package velrondevs.botania.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import velrondevs.botania.registry.BotaniaItems;

public class TerraShattererTippingRecipe extends CustomRecipe {
	public static final RecipeSerializer<TerraShattererTippingRecipe> SERIALIZER = RecipeUtils.simpleSerializer(TerraShattererTippingRecipe::new);

	public TerraShattererTippingRecipe() {}

	@NotNull
	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public boolean matches(@NotNull CraftingInput inv, @NotNull Level world) {
		boolean foundTerraPick = false;
		boolean foundElementiumPick = false;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof TerraShattererItem
						&& !TerraShattererItem.isTipped(stack) && !foundTerraPick) {
					foundTerraPick = true;
				} else if (stack.is(BotaniaItems.elementiumPick) && !foundElementiumPick) {
					foundElementiumPick = true;
				} else {
					return false;
				}
			}
		}

		return foundTerraPick && foundElementiumPick;
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv) {
		ItemStack terraPick = ItemStack.EMPTY;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty() && stack.getItem() instanceof TerraShattererItem) {
				terraPick = stack;
			}
		}

		if (terraPick.isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack terraPickCopy = terraPick.copy();
		TerraShattererItem.setTipped(terraPickCopy);
		return terraPickCopy;
	}
}
