package velrondevs.botania.datagen.providers.recipes;

import com.google.gson.JsonObject;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class BotaniaRecipeProvider implements DataProvider {
	private final PackOutput.PathProvider recipePathProvider;
	private final PackOutput.PathProvider advancementPathProvider;
	private final CompletableFuture<HolderLookup.Provider> registries;

	public BotaniaRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		this.recipePathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "recipe");
		this.advancementPathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "advancement");
		this.registries = registries;
	}

	@Override
	public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
		return registries.thenCompose(lookup -> run(cache, lookup));
	}

	private CompletableFuture<?> run(CachedOutput cache, HolderLookup.Provider lookup) {
		Set<ResourceLocation> checkDuplicates = new HashSet<>();
		List<CompletableFuture<?>> output = new ArrayList<>();
		buildRecipes(new JsonRecipeOutput() {
			@Override
			public void accept(@NotNull ResourceLocation id, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancement, ICondition @NotNull... conditions) {
				checkId(id);
				output.add(DataProvider.saveStable(cache, lookup, Recipe.CONDITIONAL_CODEC, Optional.of(new WithConditions<>(recipe, conditions)), recipePathProvider.json(id)));
				saveAdvancement(advancement, conditions);
			}

			@Override
			public void acceptJson(ResourceLocation id, JsonObject json, @Nullable AdvancementHolder advancement) {
				checkId(id);
				output.add(DataProvider.saveStable(cache, json, recipePathProvider.json(id)));
				saveAdvancement(advancement);
			}

			@Override
			public HolderLookup.Provider registries() {
				return lookup;
			}

			@Override
			public Advancement.@NotNull Builder advancement() {
				return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
			}

			private void checkId(ResourceLocation id) {
				if (!checkDuplicates.add(id)) {
					throw new IllegalStateException("Duplicate recipe " + id);
				}
			}

			private void saveAdvancement(@Nullable AdvancementHolder advancement, ICondition... conditions) {
				if (advancement != null) {
					output.add(DataProvider.saveStable(cache, lookup, Advancement.CONDITIONAL_CODEC, Optional.of(new WithConditions<>(advancement.value(), conditions)), advancementPathProvider.json(advancement.id())));
				}
			}
		});
		return CompletableFuture.allOf(output.toArray(CompletableFuture[]::new));
	}

	protected abstract void buildRecipes(RecipeOutput output);

	public interface JsonRecipeOutput extends RecipeOutput {
		void acceptJson(ResourceLocation id, JsonObject json, @Nullable AdvancementHolder advancement);

		HolderLookup.Provider registries();
	}
}
