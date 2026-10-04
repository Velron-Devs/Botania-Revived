package velrondevs.botania.client.integration.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import velrondevs.botania.api.recipe.ElvenTradeRecipe;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.common.lib.ResourceLocationHelper;
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
	public void draw(@NotNull ElvenTradeRecipe recipe, @NotNull IRecipeSlotsView slotsView, @NotNull GuiGraphics gui, double mouseX, double mouseY) {
		PoseStack matrices = gui.pose();
		RenderSystem.enableBlend();
		overlay.draw(gui, 0, 4);
		RenderSystem.disableBlend();

		RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ResourceLocationHelper.prefix("block/alfheim_portal_swirl"));
		MultiBufferSource.BufferSource immediate = Minecraft.getInstance().renderBuffers().bufferSource();
		VertexConsumer v = immediate.getBuffer(RenderType.solid());
		int startX = 22;
		int startY = 25;
		int stopX = 70;
		int stopY = 73;
		Matrix4f mat = matrices.last().pose();
		PoseStack.Pose n = matrices.last();
		v.addVertex(mat, startX, startY, 0).setColor(1f, 1f, 1f, 1f).setUv(sprite.getU0(), sprite.getV0()).setLight(0xF000F0).setNormal(n, 1, 0, 0);
		v.addVertex(mat, startX, stopY, 0).setColor(1f, 1f, 1f, 1f).setUv(sprite.getU0(), sprite.getV1()).setLight(0xF000F0).setNormal(n, 1, 0, 0);
		v.addVertex(mat, stopX, stopY, 0).setColor(1f, 1f, 1f, 1f).setUv(sprite.getU1(), sprite.getV1()).setLight(0xF000F0).setNormal(n, 1, 0, 0);
		v.addVertex(mat, stopX, startY, 0).setColor(1f, 1f, 1f, 1f).setUv(sprite.getU1(), sprite.getV0()).setLight(0xF000F0).setNormal(n, 1, 0, 0);
		immediate.endBatch();

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
}
