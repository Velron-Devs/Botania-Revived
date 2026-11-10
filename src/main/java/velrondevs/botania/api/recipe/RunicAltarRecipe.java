package velrondevs.botania.api.recipe;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.registry.BotaniaBlocks;

public interface RunicAltarRecipe extends RecipeWithReagent {
	ResourceLocation TYPE_ID = ResourceLocation.fromNamespaceAndPath(BotaniaAPI.MODID, "runic_altar");

	@Override
	default Ingredient getReagent() {
		return Ingredient.of(BotaniaBlocks.livingrock);
	}

	int getManaUsage();

	default boolean returnsRunes() {
		return true;
	}

	static Component consumesRunesNote() {
		return Component.translatableWithFallback("botania.nei.runicAltar.consumesRunes", "Runes are consumed").withStyle(ChatFormatting.RED);
	}

	@NotNull
	@Override
	default RecipeType<?> getType() {
		return BuiltInRegistries.RECIPE_TYPE.get(TYPE_ID);
	}
}
