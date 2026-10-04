package velrondevs.botania.api.recipe;

import net.minecraft.commands.CacheableFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;

import java.util.Optional;

public interface OrechidRecipe extends Recipe<RecipeInput> {
	ResourceLocation TYPE_ID = ResourceLocation.fromNamespaceAndPath(BotaniaAPI.MODID, "orechid");
	ResourceLocation IGNEM_TYPE_ID = ResourceLocation.fromNamespaceAndPath(BotaniaAPI.MODID, "orechid_ignem");
	ResourceLocation MARIMORPHOSIS_TYPE_ID = ResourceLocation.fromNamespaceAndPath(BotaniaAPI.MODID, "marimorphosis");

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

	@Override
	default boolean matches(RecipeInput c, Level l) {
		return false;
	}

	@Override
	default ItemStack assemble(RecipeInput c, @NotNull HolderLookup.Provider registries) {
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
