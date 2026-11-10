package velrondevs.botania.api.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.registry.BotaniaBlocks;

public interface RunicAltarRecipe extends RecipeWithReagent {
	Identifier TYPE_ID = Identifier.fromNamespaceAndPath(BotaniaAPI.MODID, "runic_altar");

	@Override
	default Ingredient getReagent() {
		return Ingredient.of(BotaniaBlocks.livingrock);
	}

	int getManaUsage();

	default boolean returnsRunes() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	default RecipeType<? extends RunicAltarRecipe> getType() {
		return (RecipeType<? extends RunicAltarRecipe>) BuiltInRegistries.RECIPE_TYPE.getValue(TYPE_ID);
	}
}
