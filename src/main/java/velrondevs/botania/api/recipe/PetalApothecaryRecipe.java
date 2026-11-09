package velrondevs.botania.api.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;

public interface PetalApothecaryRecipe extends RecipeWithReagent {
	Identifier TYPE_ID = Identifier.fromNamespaceAndPath(BotaniaAPI.MODID, "petal_apothecary");

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	default RecipeType<? extends PetalApothecaryRecipe> getType() {
		return (RecipeType<? extends PetalApothecaryRecipe>) BuiltInRegistries.RECIPE_TYPE.getValue(TYPE_ID);
	}
}
