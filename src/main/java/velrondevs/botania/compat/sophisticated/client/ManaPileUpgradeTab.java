package velrondevs.botania.compat.sophisticated.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeSettingsTab;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Dimension;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;

import velrondevs.botania.compat.sophisticated.ManaPileUpgradeContainer;

public class ManaPileUpgradeTab extends UpgradeSettingsTab<ManaPileUpgradeContainer> {
	private static final int SLOT_X = 3;
	private static final int SLOT_Y = 24;
	private static final int TEXT_COLOR = 0xFF404040;

	public ManaPileUpgradeTab(ManaPileUpgradeContainer container, Position position, StorageScreenBase<?> screen) {
		super(container, position, screen,
				Component.translatable("gui.botania.sophisticated.mana_pile"),
				Component.translatable("gui.botania.sophisticated.mana_pile.tooltip"));
		openTabDimension = new Dimension(84, 48);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, Minecraft minecraft, int mouseX, int mouseY) {
		super.renderBg(graphics, minecraft, mouseX, mouseY);
		if (!getContainer().isOpen()) {
			return;
		}
		moveSlotsToTab();
		GuiHelper.renderSlotsBackground(graphics, x + SLOT_X, y + SLOT_Y, 1, 1);
		graphics.drawString(minecraft.font, compact(getContainer().getMana()), x + SLOT_X + 22, y + SLOT_Y + 1, TEXT_COLOR, false);
		graphics.drawString(minecraft.font, "/ " + compact(Math.max(1, getContainer().getCapacity())), x + SLOT_X + 22, y + SLOT_Y + 11, TEXT_COLOR, false);
	}

	@Override
	public void renderTooltip(Screen screen, GuiGraphics graphics, int mouseX, int mouseY) {
		super.renderTooltip(screen, graphics, mouseX, mouseY);
		if (!getContainer().isOpen()) {
			return;
		}
		int left = x + SLOT_X;
		int top = y + SLOT_Y;
		if (mouseX >= left && mouseX < left + 18 && mouseY >= top && mouseY < top + 18
				&& !getContainer().getSlots().isEmpty() && !getContainer().getSlots().get(0).hasItem()) {
			graphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("gui.botania.sophisticated.mana_pile.slot"), mouseX, mouseY);
		}
	}

	@Override
	protected void moveSlotsToTab() {
		if (getContainer().getSlots().isEmpty()) {
			return;
		}
		Slot slot = getContainer().getSlots().get(0);
		StationUpgradeTab.moveSlot(slot, x - screen.getGuiLeft() + SLOT_X + 1, y - screen.getGuiTop() + SLOT_Y + 1);
	}

	private static String compact(int value) {
		if (value >= 1_000_000_000) {
			return trim(value / 1_000_000_000.0) + "G";
		}
		if (value >= 1_000_000) {
			return trim(value / 1_000_000.0) + "M";
		}
		if (value >= 1_000) {
			return trim(value / 1_000.0) + "k";
		}
		return Integer.toString(value);
	}

	private static String trim(double value) {
		String text = String.format("%.2f", value);
		if (text.contains(".")) {
			text = text.replaceAll("0+$", "").replaceAll("\\.$", "");
		}
		return text;
	}
}
