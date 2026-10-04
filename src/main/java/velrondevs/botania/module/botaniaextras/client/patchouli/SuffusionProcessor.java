package velrondevs.botania.module.botaniaextras.client.patchouli;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import velrondevs.botania.client.patchouli.PatchouliUtils;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasRecipeTypes;
import velrondevs.botania.module.botaniaextras.crafting.TreeSuffusionRecipe;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class SuffusionProcessor implements IComponentProcessor {
	private TreeSuffusionRecipe recipe;
	private ResourceLocation recipeId;

	@Override
	public void setup(Level level, IVariableProvider variables) {
		ResourceLocation id = ResourceLocation.parse(variables.get("recipe", level.registryAccess()).asString());
		RecipeHolder<TreeSuffusionRecipe> holder = PatchouliUtils.getRecipeHolder(level, BotaniaExtrasRecipeTypes.TREE_SUFFUSION_TYPE, id);
		this.recipe = holder == null ? null : holder.value();
		this.recipeId = holder == null ? null : holder.id();
	}

	@Override
	public IVariable process(Level level, String key) {
		if (recipe == null) {
			return null;
		}
		ItemStack output = recipe.getResultItem(level.registryAccess());
		return switch (key) {
			case "recipe" -> IVariable.wrap(recipeId.toString());
			case "output" -> IVariable.from(output, level.registryAccess());
			case "heading" -> IVariable.from(output.getHoverName(), level.registryAccess());
			case "mana" -> IVariable.wrap(recipe.getMana());
			default -> null;
		};
	}
}
