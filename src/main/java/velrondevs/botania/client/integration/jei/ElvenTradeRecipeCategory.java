package velrondevs.botania.client.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.resources.Identifier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.recipe.ElvenTradeRecipe;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.registry.BotaniaBlocks;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class ElvenTradeRecipeCategory implements IRecipeCategory<ElvenTradeRecipe> {

	public static final RecipeType<ElvenTradeRecipe> TYPE = RecipeType.create(LibMisc.MOD_ID, "elven_trade", ElvenTradeRecipe.class);
	private final Component localizedName;
	private final IDrawable overlay;
	private final IDrawable icon;

	public ElvenTradeRecipeCategory(IGuiHelper guiHelper) {
		localizedName = Component.translatable("botania.nei.elvenTrade");
		overlay = guiHelper.createDrawable(prefix("textures/gui/elven_trade_overlay.png"), 0, 15, 140, 90);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BotaniaBlocks.alfPortal));
	}

	@NotNull
	@Override
	public RecipeType<ElvenTradeRecipe> getRecipeType() {
		return TYPE;
	}

	@NotNull
	@Override
	public Component getTitle() {
		return localizedName;
	}

	@Override
	public int getWidth() {
		return 145;
	}

	@Override
	public int getHeight() {
		return 95;
	}

	@NotNull
	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void draw(@NotNull ElvenTradeRecipe recipe, @NotNull IRecipeSlotsView slotsView, @NotNull GuiGraphicsExtractor gui, double mouseX, double mouseY) {
		overlay.draw(gui, 0, 4);
		TextureAtlasSprite sprite = RenderHelper.blockSprite(prefix("block/alfheim_portal_swirl"));
		gui.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, 22, 25, 48, 48);
	}

	@Override
	public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull ElvenTradeRecipe recipe, @NotNull IFocusGroup focusGroup) {
		int posX = 42;
		for (var ingr : recipe.getIngredients()) {
			builder.addSlot(RecipeIngredientRole.INPUT, posX, 0)
					.addIngredients(ingr);
			posX += 18;
		}

		int outIdx = 0;
		for (var stack : recipe.getOutputs()) {
			builder.addSlot(RecipeIngredientRole.OUTPUT, 93 + outIdx % 2 * 20, 41 + outIdx / 2 * 20)
					.addItemStack(stack);
			outIdx++;
		}
	}

	@Override
	public Identifier getIdentifier(@NotNull ElvenTradeRecipe recipe) {
		return JEIRecipeIds.idOf(recipe);
	}
}
