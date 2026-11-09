package velrondevs.botania.common.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.xplat.XplatAbstractions;

public class ManaUpgradeRecipe extends ShapedRecipe {
	public ManaUpgradeRecipe(ShapedRecipe compose) {
		super(RecipeUtils.commonInfo(compose), RecipeUtils.bookInfo(compose), compose.pattern, RecipeUtils.result(compose));
	}

	public static ItemStack output(ItemStack output, RecipeInput inv) {
		ItemStack out = output.copy();
		var outItem = XplatAbstractions.INSTANCE.findManaItem(out);
		if (outItem == null) {
			return out;
		}
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			var item = XplatAbstractions.INSTANCE.findManaItem(stack);
			if (!stack.isEmpty() && item != null) {
				outItem.addMana(item.getMana());
			}
		}
		return out;
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput inv) {
		return output(super.assemble(inv), inv);
	}

	@NotNull
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RecipeSerializer<ShapedRecipe> getSerializer() {
		return (RecipeSerializer) SERIALIZER;
	}

	public static final RecipeSerializer<ManaUpgradeRecipe> SERIALIZER = RecipeUtils.shapedWrapper(ManaUpgradeRecipe::new);
}
