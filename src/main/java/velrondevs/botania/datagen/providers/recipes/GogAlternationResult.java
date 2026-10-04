package velrondevs.botania.datagen.providers.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;

import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.crafting.recipe.GogAlternationRecipe;

public class GogAlternationResult {
	public record Captured(ResourceLocation id, Recipe<?> recipe, @Nullable AdvancementHolder advancement) {}

	public static RecipeOutput capture(RecipeOutput parent, MutableObject<Captured> target) {
		return new RecipeOutput() {
			@Override
			public void accept(@NotNull ResourceLocation id, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancement, ICondition @NotNull... conditions) {
				target.setValue(new Captured(id, recipe, advancement));
			}

			@Override
			public Advancement.@NotNull Builder advancement() {
				return parent.advancement();
			}
		};
	}

	public static void accept(RecipeOutput output, Captured gog, Captured base) {
		BotaniaRecipeProvider.JsonRecipeOutput jsonOutput = (BotaniaRecipeProvider.JsonRecipeOutput) output;
		DynamicOps<JsonElement> ops = jsonOutput.registries().createSerializationContext(JsonOps.INSTANCE);
		JsonObject json = new JsonObject();
		json.addProperty("type", BuiltInRegistries.RECIPE_SERIALIZER.getKey(GogAlternationRecipe.SERIALIZER).toString());
		json.add("gog", Recipe.CODEC.encodeStart(ops, gog.recipe()).getOrThrow());
		json.add("base", Recipe.CODEC.encodeStart(ops, base.recipe()).getOrThrow());
		jsonOutput.acceptJson(base.id(), json, base.advancement());
	}
}
