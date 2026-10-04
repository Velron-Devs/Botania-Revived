package velrondevs.botania.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.block.decor.BotaniaMushroomBlock;
import velrondevs.botania.common.item.WandOfTheForestItem;
import velrondevs.botania.common.item.material.MysticalPetalItem;

public class WandOfTheForestRecipe extends ShapedRecipe {
	public static final RecipeSerializer<WandOfTheForestRecipe> SERIALIZER = RecipeUtils.shapedWrapper(WandOfTheForestRecipe::new);

	public WandOfTheForestRecipe(ShapedRecipe compose) {
		super(compose.getGroup(), compose.category(), compose.pattern, compose.getResultItem(RegistryAccess.EMPTY), compose.showNotification());
	}

	@NotNull
	@Override
	public ItemStack assemble(CraftingInput inv, @NotNull HolderLookup.Provider registries) {
		int first = -1;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			Item item = stack.getItem();

			int colorId;
			if (item instanceof MysticalPetalItem petal) {
				colorId = petal.color.getId();
			} else if (item instanceof BlockItem block && block.getBlock() instanceof BotaniaMushroomBlock mushroom) {
				colorId = mushroom.color.getId();
			} else {
				continue;
			}
			if (first == -1) {
				first = colorId;
			} else {
				return WandOfTheForestItem.setColors(getResultItem(registries).copy(), first, colorId);
			}
		}
		return WandOfTheForestItem.setColors(getResultItem(registries).copy(), first != -1 ? first : 0, 0);
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
