package velrondevs.botania.client.patchouli.processor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import velrondevs.botania.api.recipe.RecipeWithReagent;
import velrondevs.botania.client.patchouli.PatchouliUtils;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.List;

public abstract class ReagentRecipeProcessor implements IComponentProcessor {
	protected RecipeWithReagent recipe;
	protected ResourceLocation recipeId;

	@Override
	public abstract void setup(Level level, IVariableProvider variables);

	@Override
	public IVariable process(Level level, String key) {
		if (recipe == null) {
			return null;
		}
		return switch (key) {
			case "recipe" -> IVariable.wrap(recipeId.toString());
			case "reagent" -> PatchouliUtils.interweaveIngredients(List.of(recipe.getReagent()), level.registryAccess());
			case "output" -> IVariable.from(recipe.getResultItem(level.registryAccess()), level.registryAccess());
			case "heading" -> IVariable.from(recipe.getResultItem(level.registryAccess()).getHoverName(), level.registryAccess());
			default -> null;
		};
	}
}
