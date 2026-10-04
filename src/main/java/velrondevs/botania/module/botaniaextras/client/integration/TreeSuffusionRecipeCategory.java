package velrondevs.botania.module.botaniaextras.client.integration;

import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.client.gui.HUDHandler;
import velrondevs.botania.client.integration.jei.PetalApothecaryRecipeCategory;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;
import velrondevs.botania.module.botaniaextras.crafting.TreeSuffusionRecipe;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class TreeSuffusionRecipeCategory implements IRecipeCategory<TreeSuffusionRecipe> {
	public static final RecipeType<TreeSuffusionRecipe> TYPE =
			RecipeType.create(LibMisc.MOD_ID, "tree_suffusion", TreeSuffusionRecipe.class);
	private final Component localizedName;
	private final IDrawable overlay;
	private final IDrawable icon;

	public TreeSuffusionRecipeCategory(IGuiHelper guiHelper) {
		localizedName = Component.translatable("botania.nei.treeSuffusion");
		overlay = guiHelper.createDrawable(prefix("textures/gui/petal_overlay.png"), 17, 11, 114, 82);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BotaniaExtrasBlocks.terrasteelItemPlatform));
	}

	@NotNull
	@Override
	public RecipeType<TreeSuffusionRecipe> getRecipeType() {
		return TYPE;
	}

	@NotNull
	@Override
	public Component getTitle() {
		return localizedName;
	}

	@Override
	public int getWidth() {
		return 114;
	}

	@Override
	public int getHeight() {
		return 104;
	}

	@NotNull
	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void draw(TreeSuffusionRecipe recipe, @NotNull IRecipeSlotsView slotsView, @NotNull GuiGraphics gui, double mouseX, double mouseY) {
		RenderSystem.enableBlend();
		overlay.draw(gui, 0, 4);
		HUDHandler.renderManaBar(gui, 6, 98, 0x0000FF, 0.75F, recipe.getMana(), ManaPoolBlockEntity.MAX_MANA / 10);
		RenderSystem.disableBlend();
	}

	@Override
	public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull TreeSuffusionRecipe recipe, @NotNull IFocusGroup focusGroup) {
		PetalApothecaryRecipeCategory.setRecipeLayout(builder, recipe.getIngredients(), BotaniaExtrasWoods.sapling,
				recipe.getResultItem(RegistryAccess.EMPTY));
	}
}
