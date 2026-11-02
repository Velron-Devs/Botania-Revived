package velrondevs.botania.client.integration.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

public final class JEIRecipeIds {
	private static RecipeManager cachedManager;
	private static Recipe<?> cachedSample;
	private static Map<Recipe<?>, ResourceLocation> cachedIds = new IdentityHashMap<>();

	private JEIRecipeIds() {}

	@Nullable
	public static synchronized ResourceLocation idOf(Recipe<?> recipe) {
		var level = Minecraft.getInstance().level;
		if (level == null) {
			return null;
		}
		RecipeManager manager = level.getRecipeManager();
		Collection<RecipeHolder<?>> holders = manager.getRecipes();
		Recipe<?> sample = holders.isEmpty() ? null : holders.iterator().next().value();
		if (manager != cachedManager || sample != cachedSample) {
			Map<Recipe<?>, ResourceLocation> ids = new IdentityHashMap<>();
			for (RecipeHolder<?> holder : holders) {
				ids.put(holder.value(), holder.id());
			}
			cachedIds = ids;
			cachedManager = manager;
			cachedSample = sample;
		}
		return cachedIds.get(recipe);
	}
}
