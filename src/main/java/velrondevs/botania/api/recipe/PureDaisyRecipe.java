package velrondevs.botania.api.recipe;

import net.minecraft.commands.CacheableFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.block_entity.SpecialFlowerBlockEntity;

import java.util.Optional;

public interface PureDaisyRecipe extends BotaniaRecipe {
	Identifier TYPE_ID = Identifier.fromNamespaceAndPath(BotaniaAPI.MODID, "pure_daisy");

	boolean matches(Level world, BlockPos pos, SpecialFlowerBlockEntity pureDaisy, BlockState state);

	boolean set(Level world, BlockPos pos, SpecialFlowerBlockEntity pureDaisy);

	StateIngredient getInput();

	BlockState getOutputState();

	Optional<CacheableFunction> getSuccessFunction();

	int getTime();

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	default RecipeType<? extends PureDaisyRecipe> getType() {
		return (RecipeType<? extends PureDaisyRecipe>) BuiltInRegistries.RECIPE_TYPE.getValue(TYPE_ID);
	}
}
