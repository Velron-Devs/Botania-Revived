package velrondevs.botania.client.integration.jei;


import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.recipe.RunicAltarRecipe;
import velrondevs.botania.client.gui.HUDHandler;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.registry.BotaniaBlocks;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class RunicAltarRecipeCategory implements IRecipeCategory<RunicAltarRecipe> {

	public static final RecipeType<RunicAltarRecipe> TYPE =
			RecipeType.create(LibMisc.MOD_ID, "runic_altar", RunicAltarRecipe.class);
	private final Component localizedName;
	private final IDrawable overlay;
	private final IDrawable icon;
	private final Ingredient LIVINGROCK = Ingredient.of(BotaniaBlocks.livingrock);

	public RunicAltarRecipeCategory(IGuiHelper guiHelper) {
		localizedName = Component.translatable("botania.nei.runicAltar");
		overlay = guiHelper.createDrawable(prefix("textures/gui/petal_overlay.png"),
				17, 11, 114, 82);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BotaniaBlocks.runeAltar));
	}

	@NotNull
	@Override
	public RecipeType<RunicAltarRecipe> getRecipeType() {
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
	public void draw(RunicAltarRecipe recipe, @NotNull IRecipeSlotsView slotsView, @NotNull GuiGraphicsExtractor gui, double mouseX, double mouseY) {
		overlay.draw(gui, 0, 4);
		HUDHandler.renderManaBar(gui, 6, 98, 0x0000FF, 0.75F, recipe.getManaUsage(), ManaPoolBlockEntity.MAX_MANA / 10);
	}

	@Override
	public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RunicAltarRecipe recipe, @NotNull IFocusGroup focusGroup) {
		var output = PetalApothecaryRecipeCategory.setRecipeLayout(builder, recipe.getIngredients(), BotaniaBlocks.runeAltar,
				recipe.getResultItem(RegistryAccess.EMPTY), LIVINGROCK);
		if (!recipe.returnsRunes()) {
			output.addRichTooltipCallback((view, tooltip) -> tooltip.add(
					Component.translatable("botania.nei.runicAltar.consumesRunes").withStyle(ChatFormatting.RED)));
		}
	}

	@Override
	public Identifier getIdentifier(@NotNull RunicAltarRecipe recipe) {
		return JEIRecipeIds.idOf(recipe);
	}
}
