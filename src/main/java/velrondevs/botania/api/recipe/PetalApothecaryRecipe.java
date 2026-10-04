package velrondevs.botania.api.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;

public interface PetalApothecaryRecipe extends RecipeWithReagent {
	ResourceLocation TYPE_ID = ResourceLocation.fromNamespaceAndPath(BotaniaAPI.MODID, "petal_apothecary");

	@NotNull
	@Override
	default RecipeType<?> getType() {
		return BuiltInRegistries.RECIPE_TYPE.get(TYPE_ID);
	}
}
