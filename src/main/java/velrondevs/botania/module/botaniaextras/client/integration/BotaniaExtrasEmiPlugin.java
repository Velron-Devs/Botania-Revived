package velrondevs.botania.module.botaniaextras.client.integration;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;

import net.minecraft.world.item.crafting.RecipeHolder;

import velrondevs.botania.module.BotaniaModules;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasModule;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasRecipeTypes;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;
import velrondevs.botania.module.botaniaextras.crafting.TreeSuffusionRecipe;

import java.util.Comparator;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

@EmiEntrypoint
public class BotaniaExtrasEmiPlugin implements EmiPlugin {
	@Override
	public void register(EmiRegistry registry) {
		if (!BotaniaModules.isEnabled(BotaniaExtrasModule.ID)) {
			return;
		}
		EmiStack icon = EmiStack.of(BotaniaExtrasBlocks.terrasteelItemPlatform);
		EmiRecipeCategory category = new EmiRecipeCategory(prefix("tree_suffusion"), icon, icon, Comparator.comparing(EmiRecipe::getId));
		registry.addCategory(category);
		registry.addWorkstation(category, EmiStack.of(BotaniaExtrasBlocks.manasteelItemPlatform));
		registry.addWorkstation(category, EmiStack.of(BotaniaExtrasBlocks.terrasteelItemPlatform));
		registry.addWorkstation(category, EmiStack.of(BotaniaExtrasBlocks.elementiumItemPlatform));
		registry.addWorkstation(category, EmiStack.of(BotaniaExtrasWoods.sapling));
		for (RecipeHolder<TreeSuffusionRecipe> holder : registry.getRecipeManager().getAllRecipesFor(BotaniaExtrasRecipeTypes.TREE_SUFFUSION_TYPE)) {
			registry.addRecipe(new TreeSuffusionEmiRecipe(category, holder));
		}
	}
}
