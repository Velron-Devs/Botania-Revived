package velrondevs.botania.module.botaniaextras.client.integration;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeHolder;

import velrondevs.botania.client.integration.rei.BotaniaReiDisplay;
import velrondevs.botania.module.BotaniaModules;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasModule;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasRecipeTypes;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;
import velrondevs.botania.module.botaniaextras.crafting.TreeSuffusionRecipe;

import java.util.Comparator;
import java.util.List;

@REIPluginClient
public class BotaniaExtrasReiPlugin implements REIClientPlugin {
	private static boolean enabled() {
		return BotaniaModules.isEnabled(BotaniaExtrasModule.ID);
	}

	@Override
	public String getPluginProviderName() {
		return "Botania Extras";
	}

	@Override
	public void registerCategories(CategoryRegistry registry) {
		if (!enabled()) {
			return;
		}
		registry.add(new TreeSuffusionReiCategory());
		registry.addWorkstations(TreeSuffusionReiCategory.ID,
				EntryStacks.of(BotaniaExtrasBlocks.manasteelItemPlatform),
				EntryStacks.of(BotaniaExtrasBlocks.terrasteelItemPlatform),
				EntryStacks.of(BotaniaExtrasBlocks.elementiumItemPlatform),
				EntryStacks.of(BotaniaExtrasWoods.sapling));
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		if (!enabled()) {
			return;
		}
		List<RecipeHolder<TreeSuffusionRecipe>> holders = registry.getRecipeManager()
				.getAllRecipesFor(BotaniaExtrasRecipeTypes.TREE_SUFFUSION_TYPE).stream()
				.sorted(Comparator.comparing(RecipeHolder::id))
				.toList();
		for (RecipeHolder<TreeSuffusionRecipe> holder : holders) {
			TreeSuffusionRecipe recipe = holder.value();
			registry.add(new BotaniaReiDisplay(TreeSuffusionReiCategory.ID,
					EntryIngredients.ofIngredients(recipe.getIngredients()),
					List.of(EntryIngredients.of(BotaniaExtrasWoods.sapling)),
					List.<EntryIngredient>of(EntryIngredients.of(recipe.getResultItem(RegistryAccess.EMPTY))),
					holder.id(), recipe.getMana(), null));
		}
	}
}
