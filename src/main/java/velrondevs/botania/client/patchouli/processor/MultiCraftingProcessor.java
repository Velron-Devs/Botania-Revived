package velrondevs.botania.client.patchouli.processor;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.client.patchouli.PatchouliUtils;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiCraftingProcessor implements IComponentProcessor {
	private List<CraftingRecipe> recipes;
	private boolean shapeless = true;
	private int longestIngredientSize = 0;
	private boolean hasCustomHeading;

	@Override
	public void setup(Level level, IVariableProvider variables) {
		List<String> names = variables.get("recipes", level.registryAccess()).asStream(level.registryAccess()).map(IVariable::asString).collect(Collectors.toList());
		this.recipes = new ArrayList<>();
		for (String name : names) {
			CraftingRecipe recipe = PatchouliUtils.getRecipe(level, RecipeType.CRAFTING, Identifier.parse(name));
			if (recipe != null) {
				recipes.add(recipe);
				if (shapeless) {
					shapeless = !(recipe instanceof ShapedRecipe);
				}
				for (Ingredient ingredient : PatchouliUtils.getCraftingIngredients(recipe)) {
					int size = PatchouliUtils.getItems(ingredient).length;
					if (longestIngredientSize < size) {
						longestIngredientSize = size;
					}
				}
			} else {
				BotaniaAPI.LOGGER.warn("Missing crafting recipe " + name);
			}
		}
		this.hasCustomHeading = variables.has("heading");
	}

	@Override
	public IVariable process(Level level, String key) {
		if (recipes.isEmpty()) {
			return null;
		}
		if (key.equals("heading")) {
			if (!hasCustomHeading) {
				return IVariable.from(PatchouliUtils.getDisplayResult(recipes.get(0), level).getHoverName(), level.registryAccess());
			}
			return null;
		}
		if (key.startsWith("input")) {
			int index = Integer.parseInt(key.substring(5)) - 1;
			int shapedX = index % 3;
			int shapedY = index / 3;
			List<Ingredient> ingredients = new ArrayList<>();
			for (CraftingRecipe recipe : recipes) {
				if (recipe instanceof ShapedRecipe shaped) {
					if (shaped.getWidth() < shapedX + 1) {
						ingredients.add(null);
					} else {
						int realIndex = index - (shapedY * (3 - shaped.getWidth()));
						List<Ingredient> list = PatchouliUtils.getCraftingIngredients(recipe);
						ingredients.add(list.size() > realIndex ? list.get(realIndex) : null);
					}

				} else {
					List<Ingredient> list = PatchouliUtils.getCraftingIngredients(recipe);
					ingredients.add(list.size() > index ? list.get(index) : null);
				}
			}
			return PatchouliUtils.interweaveIngredients(ingredients, longestIngredientSize, level.registryAccess());
		}
		if (key.equals("output")) {
			return IVariable.wrapList(recipes.stream()
					.map(r -> IVariable.from(PatchouliUtils.getDisplayResult(r, level), level.registryAccess()))
					.collect(Collectors.toList()), level.registryAccess());
		}
		if (key.equals("shapeless")) {
			return IVariable.wrap(shapeless);
		}
		return null;
	}
}
