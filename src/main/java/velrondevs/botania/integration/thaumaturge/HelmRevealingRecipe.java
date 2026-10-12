package velrondevs.botania.integration.thaumaturge;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.crafting.recipe.RecipeUtils;
import velrondevs.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;

public class HelmRevealingRecipe extends ShapelessRecipe {
	public HelmRevealingRecipe(ShapelessRecipe compose) {
		super(compose.getGroup(), CraftingBookCategory.EQUIPMENT,
				compose.getResultItem(RegistryAccess.EMPTY),
				compose.getIngredients());
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv, @NotNull HolderLookup.Provider registries) {
		ItemStack out = super.assemble(inv, registries);
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (stack.getItem() instanceof ManasteelArmorItem && !stack.getComponentsPatch().isEmpty()) {
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

	public static final RecipeSerializer<HelmRevealingRecipe> SERIALIZER = RecipeUtils.shapelessWrapper(HelmRevealingRecipe::new);
}
