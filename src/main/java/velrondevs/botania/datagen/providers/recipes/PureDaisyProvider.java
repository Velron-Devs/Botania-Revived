package velrondevs.botania.datagen.providers.recipes;

import net.minecraft.commands.CacheableFunction;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.recipe.StateIngredient;
import velrondevs.botania.common.crafting.PureDaisyRecipe;
import velrondevs.botania.common.crafting.StateIngredientHelper;
import velrondevs.botania.common.crafting.recipe.StateCopyingPureDaisyRecipe;
import velrondevs.botania.registry.BotaniaBlocks;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class PureDaisyProvider extends BotaniaRecipeProvider {
	public static final int DEFAULT_TIME = 150;

	public PureDaisyProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutput consumer) {
		pureDaisy(consumer, id("livingrock"), StateIngredientHelper.of(Blocks.STONE), BotaniaBlocks.livingrock.defaultBlockState());
		consumer.accept(id("livingwood"), new StateCopyingPureDaisyRecipe(
				StateIngredientHelper.of(BlockTags.LOGS),
				BotaniaBlocks.livingwoodLog, DEFAULT_TIME), null);
		pureDaisy(consumer, id("cobblestone"), StateIngredientHelper.of(Blocks.NETHERRACK), Blocks.COBBLESTONE.defaultBlockState());
		pureDaisy(consumer, id("end_stone_to_cobbled_deepslate"), StateIngredientHelper.of(Blocks.END_STONE), Blocks.COBBLED_DEEPSLATE.defaultBlockState(), DEFAULT_TIME, prefix("ender_air_release"));
		pureDaisy(consumer, id("sand"), StateIngredientHelper.of(Blocks.SOUL_SAND), Blocks.SAND.defaultBlockState());
		pureDaisy(consumer, id("packed_ice"), StateIngredientHelper.of(Blocks.ICE), Blocks.PACKED_ICE.defaultBlockState());
		pureDaisy(consumer, id("blue_ice"), StateIngredientHelper.of(Blocks.PACKED_ICE), Blocks.BLUE_ICE.defaultBlockState());
		pureDaisy(consumer, id("obsidian"), StateIngredientHelper.of(BotaniaBlocks.blazeBlock), Blocks.OBSIDIAN.defaultBlockState());
		pureDaisy(consumer, id("snow_block"), StateIngredientHelper.of(Blocks.WATER), Blocks.SNOW_BLOCK.defaultBlockState());
	}

	@Override
	public String getName() {
		return "Botania Pure Daisy recipes";
	}

	private static ResourceLocation id(String path) {
		return prefix("pure_daisy/" + path);
	}

	protected static void pureDaisy(RecipeOutput consumer, ResourceLocation id, StateIngredient input, BlockState state) {
		pureDaisy(consumer, id, input, state, DEFAULT_TIME, null);
	}

	protected static void pureDaisy(RecipeOutput consumer, ResourceLocation id, StateIngredient input, BlockState state, int time, @Nullable ResourceLocation function) {
		consumer.accept(id, new PureDaisyRecipe(input, state, time, Optional.ofNullable(function).map(CacheableFunction::new)), null);
	}
}
