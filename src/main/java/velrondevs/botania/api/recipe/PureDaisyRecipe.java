package velrondevs.botania.api.recipe;

import net.minecraft.commands.CacheableFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.block_entity.SpecialFlowerBlockEntity;

import java.util.Optional;

public interface PureDaisyRecipe extends Recipe<RecipeInput> {
	ResourceLocation TYPE_ID = ResourceLocation.fromNamespaceAndPath(BotaniaAPI.MODID, "pure_daisy");

	boolean matches(Level world, BlockPos pos, SpecialFlowerBlockEntity pureDaisy, BlockState state);

	boolean set(Level world, BlockPos pos, SpecialFlowerBlockEntity pureDaisy);

	StateIngredient getInput();

	BlockState getOutputState();

	Optional<CacheableFunction> getSuccessFunction();

	int getTime();

	@Override
	default RecipeType<?> getType() {
		return BuiltInRegistries.RECIPE_TYPE.get(TYPE_ID);
	}

	@Override
	default boolean matches(RecipeInput input, Level level) {
		return false;
	}

	@Override
	default ItemStack assemble(RecipeInput input, @NotNull HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Override
	default ItemStack getResultItem(@NotNull HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean isSpecial() {
		return true;
	}
}
