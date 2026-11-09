package velrondevs.botania.api.recipe;

import net.minecraft.world.item.crafting.Ingredient;

public interface RecipeWithReagent extends BotaniaRecipe {

	Ingredient getReagent();
}
