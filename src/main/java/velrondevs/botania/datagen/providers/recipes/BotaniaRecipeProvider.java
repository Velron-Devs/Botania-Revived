package velrondevs.botania.datagen.providers.recipes;

import com.google.gson.JsonObject;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ImpossibleTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
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
	private static HolderLookup.Provider currentRegistries;

	private final PackOutput.PathProvider recipePathProvider;
	private final PackOutput.PathProvider advancementPathProvider;
	private final CompletableFuture<HolderLookup.Provider> registries;

	public BotaniaRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		this.recipePathProvider = packOutput.createRegistryElementsPathProvider(Registries.RECIPE);
		this.advancementPathProvider = packOutput.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
		this.registries = registries;
	}

	@Override
	public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
		return registries.thenCompose(lookup -> run(cache, lookup));
	}

	@Override
	public String getName() {
		return "Botania recipes: " + getClass().getSimpleName();
	}

	private CompletableFuture<?> run(CachedOutput cache, HolderLookup.Provider lookup) {
		synchronized (BotaniaRecipeProvider.class) {
			currentRegistries = lookup;
			Set<Identifier> checkDuplicates = new HashSet<>();
			List<CompletableFuture<?>> output = new ArrayList<>();
			buildRecipes(new JsonRecipeOutput() {
				@Override
				public void accept(@NotNull ResourceKey<Recipe<?>> id, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancement, ICondition @NotNull... conditions) {
					checkId(id.identifier());
					output.add(DataProvider.saveStable(cache, lookup, Recipe.CONDITIONAL_CODEC, Optional.of(new WithConditions<>(recipe, conditions)), recipePathProvider.json(id.identifier())));
					saveAdvancement(advancement, conditions);
				}

				@Override
				public void acceptJson(Identifier id, JsonObject json, @Nullable AdvancementHolder advancement) {
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

				@Override
				public void includeRootAdvancement() {
					AdvancementHolder root = Advancement.Builder.recipeAdvancement()
							.addCriterion("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
							.build(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
					saveAdvancement(root);
				}

				private void checkId(Identifier id) {
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
	}

	protected abstract void buildRecipes(RecipeOutput output);

	public static HolderLookup.Provider lookup() {
		if (currentRegistries == null) {
			throw new IllegalStateException("Recipe registries are not available yet");
		}
		return currentRegistries;
	}

	public static HolderGetter<Item> items() {
		return lookup().lookupOrThrow(Registries.ITEM);
	}

	public static Ingredient tag(TagKey<Item> tag) {
		return Ingredient.of(items().getOrThrow(tag));
	}

	public static Item dyeItem(DyeColor color) {
		return switch (color) {
			case WHITE -> Items.WHITE_DYE;
			case ORANGE -> Items.ORANGE_DYE;
			case MAGENTA -> Items.MAGENTA_DYE;
			case LIGHT_BLUE -> Items.LIGHT_BLUE_DYE;
			case YELLOW -> Items.YELLOW_DYE;
			case LIME -> Items.LIME_DYE;
			case PINK -> Items.PINK_DYE;
			case GRAY -> Items.GRAY_DYE;
			case LIGHT_GRAY -> Items.LIGHT_GRAY_DYE;
			case CYAN -> Items.CYAN_DYE;
			case PURPLE -> Items.PURPLE_DYE;
			case BLUE -> Items.BLUE_DYE;
			case BROWN -> Items.BROWN_DYE;
			case GREEN -> Items.GREEN_DYE;
			case RED -> Items.RED_DYE;
			case BLACK -> Items.BLACK_DYE;
		};
	}

	public static ResourceKey<Recipe<?>> key(Identifier id) {
		return ResourceKey.create(Registries.RECIPE, id);
	}

	public static Criterion<InventoryChangeTrigger.TriggerInstance> conditionsFromItem(ItemLike item) {
		return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items(), item).build());
	}

	public static Criterion<InventoryChangeTrigger.TriggerInstance> conditionsFromItems(ItemLike... items) {
		ItemPredicate[] preds = new ItemPredicate[items.length];
		for (int i = 0; i < items.length; i++) {
			preds[i] = ItemPredicate.Builder.item().of(items(), items[i]).build();
		}
		return InventoryChangeTrigger.TriggerInstance.hasItems(preds);
	}

	public static Criterion<InventoryChangeTrigger.TriggerInstance> conditionsFromTag(TagKey<Item> tag) {
		return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items(), tag).build());
	}

	public interface JsonRecipeOutput extends RecipeOutput {
		void acceptJson(Identifier id, JsonObject json, @Nullable AdvancementHolder advancement);

		HolderLookup.Provider registries();
	}
}
