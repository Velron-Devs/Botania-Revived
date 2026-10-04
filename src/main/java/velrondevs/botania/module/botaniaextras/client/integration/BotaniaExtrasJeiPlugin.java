package velrondevs.botania.module.botaniaextras.client.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.module.BotaniaModules;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasModule;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasRecipeTypes;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;

import java.util.Comparator;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

@JeiPlugin
public class BotaniaExtrasJeiPlugin implements IModPlugin {
	private static final ResourceLocation ID = prefix("botania_extras");

	private static boolean enabled() {
		return BotaniaModules.isEnabled(BotaniaExtrasModule.ID);
	}

	@NotNull
	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		if (enabled()) {
			registry.addRecipeCategories(new TreeSuffusionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		}
	}

	@Override
	public void registerRecipes(@NotNull IRecipeRegistration registry) {
		if (enabled()) {
			registry.addRecipes(TreeSuffusionRecipeCategory.TYPE, Minecraft.getInstance().level.getRecipeManager()
					.getAllRecipesFor(BotaniaExtrasRecipeTypes.TREE_SUFFUSION_TYPE).stream()
					.sorted(Comparator.comparing(RecipeHolder::id))
					.map(RecipeHolder::value)
					.toList());
		}
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		if (enabled()) {
			registry.addRecipeCatalyst(new ItemStack(BotaniaExtrasBlocks.manasteelItemPlatform), TreeSuffusionRecipeCategory.TYPE);
			registry.addRecipeCatalyst(new ItemStack(BotaniaExtrasBlocks.terrasteelItemPlatform), TreeSuffusionRecipeCategory.TYPE);
			registry.addRecipeCatalyst(new ItemStack(BotaniaExtrasBlocks.elementiumItemPlatform), TreeSuffusionRecipeCategory.TYPE);
			registry.addRecipeCatalyst(new ItemStack(BotaniaExtrasWoods.sapling), TreeSuffusionRecipeCategory.TYPE);
		}
	}
}
