package velrondevs.botania.client.integration.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

public final class JEIRecipeIds {
	private static RecipeManager cachedManager;
	private static Map<Recipe<?>, ResourceLocation> cachedIds = new IdentityHashMap<>();

	private JEIRecipeIds() {}

	@Nullable
	public static synchronized ResourceLocation idOf(Recipe<?> recipe) {
		var level = Minecraft.getInstance().level;
		if (level == null) {
			return null;
		}
		RecipeManager manager = level.getRecipeManager();
		if (manager != cachedManager) {
			Map<Recipe<?>, ResourceLocation> ids = new IdentityHashMap<>();
			for (RecipeHolder<?> holder : manager.getRecipes()) {
				ids.put(holder.value(), holder.id());
			}
			cachedIds = ids;
			cachedManager = manager;
		}
		return cachedIds.get(recipe);
	}
}
