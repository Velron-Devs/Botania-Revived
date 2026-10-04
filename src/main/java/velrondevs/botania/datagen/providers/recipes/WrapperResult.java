package velrondevs.botania.datagen.providers.recipes;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.neoforged.neoforge.common.conditions.ICondition;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class WrapperResult {
	public static RecipeOutput shaped(Function<ShapedRecipe, ? extends Recipe<?>> factory, RecipeOutput parent) {
		return transform(parent, recipe -> factory.apply((ShapedRecipe) recipe));
	}

	public static RecipeOutput shapeless(Function<ShapelessRecipe, ? extends Recipe<?>> factory, RecipeOutput parent) {
		return transform(parent, recipe -> factory.apply((ShapelessRecipe) recipe));
	}

	public static RecipeOutput transform(RecipeOutput parent, UnaryOperator<Recipe<?>> transform) {
		return new RecipeOutput() {
			@Override
			public void accept(@NotNull ResourceLocation id, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancement, ICondition @NotNull... conditions) {
				parent.accept(id, transform.apply(recipe), advancement, conditions);
			}

			@Override
			public Advancement.@NotNull Builder advancement() {
				return parent.advancement();
			}
		};
	}
}
