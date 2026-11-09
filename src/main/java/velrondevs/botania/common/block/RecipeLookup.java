package velrondevs.botania.common.block;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public final class RecipeLookup {
	private RecipeLookup() {}

	@Nullable
	public static RecipeManager manager(@Nullable Level level) {
		if (level instanceof ServerLevel serverLevel) {
			return serverLevel.recipeAccess();
		}
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		return server != null ? server.getRecipeManager() : null;
	}

	public static <I extends RecipeInput, T extends Recipe<I>> Optional<RecipeHolder<T>> getRecipeFor(Level level, RecipeType<T> type, I input) {
		RecipeManager manager = manager(level);
		return manager == null ? Optional.empty() : manager.getRecipeFor(type, input, level);
	}

	public static <I extends RecipeInput, T extends Recipe<I>> Collection<RecipeHolder<T>> getAllRecipesFor(@Nullable Level level, RecipeType<T> type) {
		RecipeManager manager = manager(level);
		return manager == null ? List.of() : manager.recipeMap().byType(type);
	}

	public static <I extends RecipeInput, T extends Recipe<I>> Optional<RecipeHolder<T>> getRecipeFor(@Nullable Level lookup, RecipeType<T> type, I input, Level level) {
		RecipeManager manager = manager(lookup);
		return manager == null ? Optional.empty() : manager.getRecipeFor(type, input, level);
	}

	public static Optional<RecipeHolder<?>> byKey(@Nullable Level level, ResourceKey<Recipe<?>> key) {
		RecipeManager manager = manager(level);
		return manager == null ? Optional.empty() : manager.byKey(key);
	}
}
