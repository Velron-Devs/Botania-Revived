package velrondevs.botania.client.integration.jei.orechid;

import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.recipe.OrechidRecipe;
import velrondevs.botania.client.integration.shared.OrechidUIHelper;

import java.util.stream.Stream;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public abstract class OrechidRecipeCategoryBase<T extends OrechidRecipe> implements IRecipeCategory<T> {

	private final Component localizedName;
	private final IDrawableStatic overlay;
	private final IDrawable icon;
	private final ItemStack iconStack;

	public OrechidRecipeCategoryBase(IGuiHelper guiHelper, ItemStack iconStack, Component localizedName) {
		overlay = guiHelper.createDrawable(prefix("textures/gui/pure_daisy_overlay.png"),
				0, 0, 64, 44);
		this.localizedName = localizedName;
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, iconStack);
		this.iconStack = iconStack;
	}

	@NotNull
	@Override
	public Component getTitle() {
		return localizedName;
	}

	@Override
	public int getWidth() {
		return 96;
	}

	@Override
	public int getHeight() {
		return 44;
	}

	@NotNull
	@Override
	public IDrawable getIcon() {
		return icon;
	}

	protected abstract RecipeType<T> recipeType();

	@Override
	public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull OrechidRecipe recipe, @NotNull IFocusGroup focusGroup) {

		builder.addSlot(RecipeIngredientRole.INPUT, 9, 12)
				.addItemStacks(recipe.getInput().getDisplayedStacks());
		builder.addSlot(RecipeIngredientRole.CATALYST, 39, 12).addItemStack(iconStack);

		builder.addSlot(RecipeIngredientRole.OUTPUT, 68, 12)
				.addItemStacks(recipe.getOutput().getDisplayedStacks())
				.addRichTooltipCallback((view, tooltip) -> tooltip.addAll(recipe.getOutput().descriptionTooltip()));
	}

	@Override
	public void draw(@NotNull OrechidRecipe recipe, @NotNull IRecipeSlotsView view, @NotNull GuiGraphics gui, double mouseX, double mouseY) {
		final Double chance = getChance(recipe);
		if (chance != null) {
			final Component chanceComponent = OrechidUIHelper.getPercentageComponent(chance);
			Font font = Minecraft.getInstance().font;
			int xOffset = 90 - font.width(chanceComponent);
			gui.drawString(font, chanceComponent, xOffset, 1, 0x888888, false);
		}
		RenderSystem.enableBlend();
		overlay.draw(gui, 17, 0);
		RenderSystem.disableBlend();
	}

	@Override
	public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull OrechidRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if (mouseX > 0.6 * getWidth() && mouseX < 90 && mouseY < 12) {
			final Double chance = getChance(recipe);
			if (chance != null) {
				tooltip.addAll(getChanceTooltipComponents(chance, recipe).toList());
			}
		}
	}

	@NotNull
	protected Stream<Component> getChanceTooltipComponents(double chance, @NotNull OrechidRecipe recipe) {
		final var ratio = OrechidUIHelper.getRatioForChance(chance);
		return Stream.of(OrechidUIHelper.getRatioTooltipComponent(ratio));
	}

	@Nullable
	protected Double getChance(@NotNull OrechidRecipe recipe) {
		return OrechidUIHelper.getChance(recipe, null);
	}
}
