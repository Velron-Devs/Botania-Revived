package velrondevs.botania.api.recipe;

import net.minecraft.commands.CacheableFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;

import java.util.Optional;

public interface OrechidRecipe extends BotaniaRecipe {
	Identifier TYPE_ID = Identifier.fromNamespaceAndPath(BotaniaAPI.MODID, "orechid");
	Identifier IGNEM_TYPE_ID = Identifier.fromNamespaceAndPath(BotaniaAPI.MODID, "orechid_ignem");
	Identifier MARIMORPHOSIS_TYPE_ID = Identifier.fromNamespaceAndPath(BotaniaAPI.MODID, "marimorphosis");

	StateIngredient getInput();

	StateIngredient getOutput();

	@NotNull
	@Override
	RecipeType<? extends OrechidRecipe> getType();

	default StateIngredient getOutput(@NotNull Level level, @NotNull BlockPos pos) {
		return getOutput();
	}

	int getWeight();

	default int getWeight(@NotNull Level level, @NotNull BlockPos pos) {
		return getWeight();
	}

	Optional<CacheableFunction> getSuccessFunction();
}
