package velrondevs.botania.integration.thaumaturge;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.crafting.recipe.RecipeUtils;
import velrondevs.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;

public class HelmRevealingRecipe extends ShapelessRecipe {
	public HelmRevealingRecipe(ShapelessRecipe compose) {
		super(RecipeUtils.commonInfo(compose), new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.EQUIPMENT, compose.group()),
				RecipeUtils.result(compose), RecipeUtils.ingredients(compose));
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv) {
		ItemStack out = super.assemble(inv);
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RecipeSerializer<ShapelessRecipe> getSerializer() {
		return (RecipeSerializer) SERIALIZER;
	}

	public static final RecipeSerializer<HelmRevealingRecipe> SERIALIZER = RecipeUtils.shapelessWrapper(HelmRevealingRecipe::new);
}
