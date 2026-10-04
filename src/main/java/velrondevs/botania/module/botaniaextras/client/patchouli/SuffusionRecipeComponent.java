package velrondevs.botania.module.botaniaextras.client.patchouli;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;

import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import velrondevs.botania.client.patchouli.component.RotatingItemListComponentBase;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasRecipeTypes;
import velrondevs.botania.module.botaniaextras.crafting.TreeSuffusionRecipe;
import vazkii.patchouli.api.IVariable;

import java.util.List;
import java.util.function.UnaryOperator;

public class SuffusionRecipeComponent extends RotatingItemListComponentBase {
	@SerializedName("recipe_name")
	public String recipeName;

	@Override
	protected List<Ingredient> makeIngredients() {
		var level = Minecraft.getInstance().level;
		if (level == null) {
			return ImmutableList.of();
		}
		ResourceLocation id = ResourceLocation.parse(recipeName);
		for (RecipeHolder<TreeSuffusionRecipe> holder : level.getRecipeManager().getAllRecipesFor(BotaniaExtrasRecipeTypes.TREE_SUFFUSION_TYPE)) {
			if (holder.id().equals(id)) {
				return holder.value().getIngredients();
			}
		}
		return ImmutableList.of();
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup, HolderLookup.Provider registries) {
		recipeName = lookup.apply(IVariable.wrap(recipeName)).asString();
	}
}
