package velrondevs.botania.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import org.jetbrains.annotations.NotNull;

public class ShapelessManaUpgradeRecipe extends ShapelessRecipe {
	public ShapelessManaUpgradeRecipe(ShapelessRecipe compose) {
		super(RecipeUtils.commonInfo(compose), new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.EQUIPMENT, compose.group()),
				RecipeUtils.result(compose), RecipeUtils.ingredients(compose));
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv) {
		return ManaUpgradeRecipe.output(super.assemble(inv), inv);
	}

	@NotNull
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RecipeSerializer<ShapelessRecipe> getSerializer() {
		return (RecipeSerializer) SERIALIZER;
	}

	public static final RecipeSerializer<ShapelessManaUpgradeRecipe> SERIALIZER = RecipeUtils.shapelessWrapper(ShapelessManaUpgradeRecipe::new);
}
