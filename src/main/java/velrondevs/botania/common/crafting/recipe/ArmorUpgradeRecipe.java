package velrondevs.botania.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import org.jetbrains.annotations.NotNull;

public class ArmorUpgradeRecipe extends ShapedRecipe {
	public ArmorUpgradeRecipe(ShapedRecipe compose) {
		super(compose.getGroup(), compose.category(), compose.pattern, compose.getResultItem(RegistryAccess.EMPTY), compose.showNotification());
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv, @NotNull HolderLookup.Provider registries) {
		ItemStack out = super.assemble(inv, registries);
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.getComponentsPatch().isEmpty() && stack.getItem() instanceof ArmorItem) {
				out.applyComponents(stack.getComponentsPatch());
				break;
			}
		}
		return out;
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static final RecipeSerializer<ArmorUpgradeRecipe> SERIALIZER = RecipeUtils.shapedWrapper(ArmorUpgradeRecipe::new);
}
