package velrondevs.botania.client.gui.box;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import velrondevs.botania.client.lib.ResourcesLib;

public class BaubleBoxGui extends AbstractContainerScreen<BaubleBoxContainer> {

	private static final ResourceLocation texture = ResourceLocation.parse(ResourcesLib.GUI_BAUBLE_BOX);

	public BaubleBoxGui(BaubleBoxContainer container, Inventory player, Component title) {
		super(container, player, title);
	}

	@Override
	public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		this.renderTooltip(gui, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(GuiGraphics gui, int x, int y) {

	}

	@Override
	protected void renderBg(GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		gui.blit(texture, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		InventoryScreen.renderEntityInInventoryFollowsMouse(gui, leftPos + 6, topPos + 8, leftPos + 55, topPos + 78, 30, 0.0625F, mouseX, mouseY, minecraft.player);
	}

}
