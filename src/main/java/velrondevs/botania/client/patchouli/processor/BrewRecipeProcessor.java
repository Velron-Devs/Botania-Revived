package velrondevs.botania.client.patchouli.processor;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import velrondevs.botania.api.recipe.BotanicalBreweryRecipe;
import velrondevs.botania.client.patchouli.PatchouliUtils;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.registry.BotaniaRecipeTypes;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BrewRecipeProcessor implements IComponentProcessor {
	private BotanicalBreweryRecipe recipe;

	@Override
	public void setup(Level level, IVariableProvider variables) {
		ResourceLocation id = ResourceLocation.parse(variables.get("recipe", level.registryAccess()).asString());
		this.recipe = PatchouliUtils.getRecipe(level, BotaniaRecipeTypes.BREW_TYPE, id);
	}

	@Override
	public IVariable process(Level level, String key) {
		if (recipe == null) {
			if (key.equals("is_offset")) {
				return IVariable.wrap(false);
			}
			return null;
		} else if (key.equals("heading")) {
			return IVariable.from(Component.translatable("botaniamisc.brewOf", Component.translatable(recipe.getBrew().getTranslationKey())), level.registryAccess());
		} else if (key.equals("vial")) {
			return IVariable.from(recipe.getOutput(new ItemStack(BotaniaItems.vial)), level.registryAccess());
		} else if (key.equals("flask")) {
			return IVariable.from(recipe.getOutput(new ItemStack(BotaniaItems.flask)), level.registryAccess());
		} else if (key.startsWith("input")) {
			int requestedIndex = Integer.parseInt(key.substring(5)) - 1;
			int indexOffset = (6 - recipe.getIngredients().size()) / 2;
			int index = requestedIndex - indexOffset;

			if (index < recipe.getIngredients().size() && index >= 0) {
				return IVariable.wrapList(Arrays.stream(recipe.getIngredients().get(index).getItems()).map(v -> IVariable.from(v, level.registryAccess())).collect(Collectors.toList()), level.registryAccess());
			} else {
				return null;
			}
		}
		if (key.equals("is_offset")) {
			return IVariable.wrap(recipe.getIngredients().size() % 2 == 0);
		}
		return null;
	}
}
