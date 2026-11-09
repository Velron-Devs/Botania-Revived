package velrondevs.botania.client.gui.bag;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.client.lib.ResourcesLib;
import velrondevs.botania.common.item.FlowerPouchItem;

public class FlowerPouchGui extends AbstractContainerScreen<FlowerPouchContainer> {

	private static final Identifier texture = Identifier.parse(ResourcesLib.GUI_FLOWER_BAG);

	public FlowerPouchGui(FlowerPouchContainer container, Inventory playerInv, Component title) {
		super(container, playerInv, title, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT + 36);

		inventoryLabelY = imageHeight - 94;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor gui, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(gui, mouseX, mouseY, partialTicks);
		Minecraft mc = Minecraft.getInstance();
		int k = (width - imageWidth) / 2;
		int l = (height - imageHeight) / 2;
		gui.blit(RenderPipelines.GUI_TEXTURED, texture, k, l, 0, 0, imageWidth, imageHeight, 256, 256);

		for (Slot slot : menu.slots) {
			if (slot.container == menu.flowerBagInv) {
				int x = this.leftPos + slot.x;
				int y = this.topPos + slot.y;
				if (!slot.hasItem()) {
					ItemStack missingFlower = new ItemStack(FlowerPouchItem.getFlowerForSlot(slot.index));
					RenderHelper.renderGuiItemAlpha(gui, missingFlower, x, y, 0x5F);
				}
			}
		}
	}

	@Override
	public void extractContents(GuiGraphicsExtractor gui, int mouseX, int mouseY, float partialTicks) {
		super.extractContents(gui, mouseX, mouseY, partialTicks);
		Minecraft mc = Minecraft.getInstance();
		for (Slot slot : menu.slots) {
			if (slot.container == menu.flowerBagInv && slot.hasItem() && slot.getItem().getCount() == 1) {
				int x = this.leftPos + slot.x;
				int y = this.topPos + slot.y;
				gui.nextStratum();
				RenderHelper.drawString(gui, mc.font, "1", x + 11, y + 9, 0xFFFFFF);
			}
		}
	}

}
