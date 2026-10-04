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

import velrondevs.botania.api.item.CosmeticAttachable;
import velrondevs.botania.api.item.CosmeticBauble;

public class CosmeticAttachRecipe extends CustomRecipe {
	public static final RecipeSerializer<CosmeticAttachRecipe> SERIALIZER = new SimpleCraftingRecipeSerializer<>(CosmeticAttachRecipe::new);

	public CosmeticAttachRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(@NotNull CraftingInput inv, @NotNull Level world) {
		boolean foundCosmetic = false;
		boolean foundAttachable = false;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof CosmeticBauble && !foundCosmetic) {
					foundCosmetic = true;
				} else if (!foundAttachable) {
					if (stack.getItem() instanceof CosmeticAttachable attachable && !(stack.getItem() instanceof CosmeticBauble) && attachable.getCosmeticItem(stack).isEmpty()) {
						foundAttachable = true;
					} else {
						return false;
					}
				}
			}
		}

		return foundCosmetic && foundAttachable;
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv, @NotNull HolderLookup.Provider registries) {
		ItemStack cosmeticItem = ItemStack.EMPTY;
		ItemStack attachableItem = ItemStack.EMPTY;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof CosmeticBauble && cosmeticItem.isEmpty()) {
					cosmeticItem = stack;
				} else {
					attachableItem = stack;
				}
			}
		}

		if (!(attachableItem.getItem() instanceof CosmeticAttachable attachable)) {
			return ItemStack.EMPTY;
		}

		if (!attachable.getCosmeticItem(attachableItem).isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack copy = attachableItem.copy();
		attachable.setCosmeticItem(copy, cosmeticItem);
		return copy;
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
