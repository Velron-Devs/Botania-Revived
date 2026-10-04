package velrondevs.botania.client.patchouli.processor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import velrondevs.botania.client.patchouli.PatchouliUtils;
import velrondevs.botania.registry.BotaniaRecipeTypes;
import vazkii.patchouli.api.IVariableProvider;

public class PetalApothecaryProcessor extends ReagentRecipeProcessor {
	@Override
	public void setup(Level level, IVariableProvider variables) {
		ResourceLocation id = ResourceLocation.parse(variables.get("recipe", level.registryAccess()).asString());
		var holder = PatchouliUtils.getRecipeHolder(level, BotaniaRecipeTypes.PETAL_TYPE, id);
		this.recipe = holder == null ? null : holder.value();
		this.recipeId = holder == null ? null : holder.id();
	}
}
