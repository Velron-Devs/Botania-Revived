package velrondevs.botania.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import org.jetbrains.annotations.NotNull;

public class ShapelessManaUpgradeRecipe extends ShapelessRecipe {
	public ShapelessManaUpgradeRecipe(ShapelessRecipe compose) {
		super(compose.getGroup(), CraftingBookCategory.EQUIPMENT,
				compose.getResultItem(RegistryAccess.EMPTY),
				compose.getIngredients());
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv, @NotNull HolderLookup.Provider registries) {
		return ManaUpgradeRecipe.output(super.assemble(inv, registries), inv);
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static final RecipeSerializer<ShapelessManaUpgradeRecipe> SERIALIZER = RecipeUtils.shapelessWrapper(ShapelessManaUpgradeRecipe::new);
}
