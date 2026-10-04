package velrondevs.botania.client.patchouli.processor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import velrondevs.botania.api.recipe.RunicAltarRecipe;
import velrondevs.botania.client.patchouli.PatchouliUtils;
import velrondevs.botania.registry.BotaniaRecipeTypes;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class RunicAltarProcessor extends ReagentRecipeProcessor {
	@Override
	public void setup(Level level, IVariableProvider variables) {
		ResourceLocation id = ResourceLocation.parse(variables.get("recipe", level.registryAccess()).asString());
		var holder = PatchouliUtils.getRecipeHolder(level, BotaniaRecipeTypes.RUNE_TYPE, id);
		this.recipe = holder == null ? null : holder.value();
		this.recipeId = holder == null ? null : holder.id();
	}

	@Override
	public IVariable process(Level level, String key) {
		if (recipe == null) {
			return super.process(level, key);
		}
		if (key.equals("mana")) {
			return IVariable.wrap(((RunicAltarRecipe) recipe).getManaUsage());
		}
		return super.process(level, key);
	}
}
