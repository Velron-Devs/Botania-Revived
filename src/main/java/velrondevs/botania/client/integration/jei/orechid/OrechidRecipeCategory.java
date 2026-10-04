package velrondevs.botania.client.integration.jei.orechid;

import mezz.jei.api.helpers.IGuiHelper;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.crafting.OrechidRecipe;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaRecipeTypes;

public class OrechidRecipeCategory extends OrechidRecipeCategoryBase<OrechidRecipe> {
	public static final mezz.jei.api.recipe.RecipeType<OrechidRecipe> TYPE =
			mezz.jei.api.recipe.RecipeType.create(LibMisc.MOD_ID, "orechid", OrechidRecipe.class);

	public OrechidRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, new ItemStack(BotaniaFlowerBlocks.orechid), Component.translatable("botania.nei.orechid"));
	}

	@NotNull
	@Override
	public mezz.jei.api.recipe.RecipeType<OrechidRecipe> getRecipeType() {
		return TYPE;
	}

	@Override
	protected RecipeType<OrechidRecipe> recipeType() {
		return BotaniaRecipeTypes.ORECHID_TYPE;
	}
}
