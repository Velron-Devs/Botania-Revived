package velrondevs.botania.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import org.jetbrains.annotations.NotNull;

public class ArmorUpgradeRecipe extends ShapedRecipe {
	public ArmorUpgradeRecipe(ShapedRecipe compose) {
		super(RecipeUtils.commonInfo(compose), RecipeUtils.bookInfo(compose), compose.pattern, RecipeUtils.result(compose));
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv) {
		ItemStack out = super.assemble(inv);
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.getComponentsPatch().isEmpty() && stack.has(DataComponents.EQUIPPABLE)) {
				out.applyComponents(stack.getComponentsPatch());
				break;
			}
		}
		return out;
	}

	@NotNull
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RecipeSerializer<ShapedRecipe> getSerializer() {
		return (RecipeSerializer) SERIALIZER;
	}

	public static final RecipeSerializer<ArmorUpgradeRecipe> SERIALIZER = RecipeUtils.shapedWrapper(ArmorUpgradeRecipe::new);
}
