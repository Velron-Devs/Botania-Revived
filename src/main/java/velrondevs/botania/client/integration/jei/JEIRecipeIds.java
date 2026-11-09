package velrondevs.botania.client.integration.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.IdentityHashMap;
import java.util.Map;

public final class JEIRecipeIds {
	private static RecipeMap cachedMap;
	private static Map<Recipe<?>, Identifier> cachedIds = new IdentityHashMap<>();

	private JEIRecipeIds() {}

	@Nullable
	public static synchronized Identifier idOf(Recipe<?> recipe) {
		RecipeMap map = BotaniaRecipeTypes.recipeMap(Minecraft.getInstance().level);
		if (map != cachedMap) {
			Map<Recipe<?>, Identifier> ids = new IdentityHashMap<>();
			for (RecipeHolder<?> holder : map.values()) {
				ids.put(holder.value(), holder.id().identifier());
			}
			cachedIds = ids;
			cachedMap = map;
		}
		return cachedIds.get(recipe);
	}
}
