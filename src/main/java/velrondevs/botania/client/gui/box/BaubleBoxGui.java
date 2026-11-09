package velrondevs.botania.client.gui.box;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import velrondevs.botania.client.lib.ResourcesLib;

public class BaubleBoxGui extends AbstractContainerScreen<BaubleBoxContainer> {

	private static final Identifier texture = Identifier.parse(ResourcesLib.GUI_BAUBLE_BOX);

	public BaubleBoxGui(BaubleBoxContainer container, Inventory player, Component title) {
		super(container, player, title);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor gui, int x, int y) {

	}

	@Override
	public void extractBackground(GuiGraphicsExtractor gui, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(gui, mouseX, mouseY, partialTicks);
		gui.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
		InventoryScreen.extractEntityInInventoryFollowsMouse(gui, leftPos + 6, topPos + 8, leftPos + 55, topPos + 78, 30, 0.0625F, mouseX, mouseY, minecraft.player);
	}

}
