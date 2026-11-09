package velrondevs.botania.compat.sophisticated.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeSettingsTab;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinition;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinitions;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ToggleButton;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Dimension;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.UV;

import velrondevs.botania.compat.sophisticated.ManaPileUpgradeContainer;
import velrondevs.botania.compat.sophisticated.ManaPileUpgradeItem;

import java.util.List;

public class ManaPileUpgradeTab extends UpgradeSettingsTab<ManaPileUpgradeContainer> {
	private static final int BUTTON_X = 3;
	private static final int BUTTON_Y = 24;
	private static final int INFUSION_X = 21;
	private static final int SLOT_X = 3;
	private static final int SLOT_Y = 46;
	private static final int CRAFT_Y = 68;
	private static final int ARROW_X = 25;
	private static final int OUTPUT_X = 39;
	private static final int CATALYST_Y = 90;
	private static final int TEXT_X = 43;
	private static final int LABEL_X = 25;
	private static final int MODE_Y = 114;
	private static final int TEXT_COLOR = 0xFF404040;
	private static final ButtonDefinition.Toggle<Boolean> MODE = ButtonDefinitions.createToggleButtonDefinition(
			ButtonDefinitions.getBooleanStateData(
					GuiHelper.getButtonStateData(new UV(160, 0), Dimension.SQUARE_16, new Position(1, 1), Component.translatable("gui.botania.sophisticated.mana_pile.mode.drain")),
					GuiHelper.getButtonStateData(new UV(144, 0), Dimension.SQUARE_16, new Position(1, 1), Component.translatable("gui.botania.sophisticated.mana_pile.mode.fill"))
			)
	);

	private static final ButtonDefinition.Toggle<Boolean> INFUSION = ButtonDefinitions.createToggleButtonDefinition(
			ButtonDefinitions.getBooleanStateData(
					GuiHelper.getButtonStateData(new UV(128, 64), Dimension.SQUARE_16, new Position(1, 1), Component.translatable("gui.botania.sophisticated.mana_pile.infusion.on"), Component.translatable("gui.botania.sophisticated.mana_pile.infusion.detail").withStyle(ChatFormatting.GRAY)),
					GuiHelper.getButtonStateData(new UV(240, 0), Dimension.SQUARE_16, new Position(1, 1), Component.translatable("gui.botania.sophisticated.mana_pile.infusion.off"), Component.translatable("gui.botania.sophisticated.mana_pile.infusion.detail").withStyle(ChatFormatting.GRAY))
			)
	);

	public ManaPileUpgradeTab(ManaPileUpgradeContainer container, Position position, StorageScreenBase<?> screen) {
		super(container, position, screen,
				Component.translatable("gui.botania.sophisticated.mana_pile"),
				Component.translatable("gui.botania.sophisticated.mana_pile.tooltip"));
		openTabDimension = new Dimension(100, 126);
		addHideableChild(new ToggleButton<>(new Position(x + BUTTON_X, y + BUTTON_Y), MODE,
				button -> getContainer().setDischarging(!getContainer().isDischarging()), getContainer()::isDischarging));
		addHideableChild(new ToggleButton<>(new Position(x + INFUSION_X, y + BUTTON_Y), INFUSION,
				button -> getContainer().setInfusing(!getContainer().isInfusing()), getContainer()::isInfusing));
	}

	@Override
	protected void renderBg(GuiGraphics graphics, Minecraft minecraft, int mouseX, int mouseY) {
		super.renderBg(graphics, minecraft, mouseX, mouseY);
		if (!getContainer().isOpen()) {
			return;
		}
		moveSlotsToTab();
		GuiHelper.renderSlotsBackground(graphics, x + SLOT_X, y + SLOT_Y, 1, 1);
		GuiHelper.renderSlotsBackground(graphics, x + SLOT_X, y + CRAFT_Y, 1, 1);
		graphics.drawString(minecraft.font, "→", x + ARROW_X + 2, y + CRAFT_Y + 5, TEXT_COLOR, false);
		GuiHelper.renderSlotsBackground(graphics, x + OUTPUT_X, y + CRAFT_Y, 1, 1);
		GuiHelper.renderSlotsBackground(graphics, x + SLOT_X, y + CATALYST_Y, 1, 1);
		graphics.drawString(minecraft.font, Component.translatable("gui.botania.sophisticated.mana_pile.catalyst.short"), x + LABEL_X, y + CATALYST_Y + 5, TEXT_COLOR, false);
		graphics.drawString(minecraft.font, compact(getContainer().getMana()), x + TEXT_X, y + BUTTON_Y + 2, TEXT_COLOR, false);
		graphics.drawString(minecraft.font, "/ " + compact(Math.max(1, getContainer().getCapacity())), x + TEXT_X, y + BUTTON_Y + 12, TEXT_COLOR, false);
		String mode = getContainer().isDischarging() ? "gui.botania.sophisticated.mana_pile.mode.drain.short" : "gui.botania.sophisticated.mana_pile.mode.fill.short";
		graphics.drawString(minecraft.font, Component.translatable(mode), x + BUTTON_X, y + MODE_Y, TEXT_COLOR, false);
	}

	@Override
	public void renderTooltip(Screen screen, GuiGraphics graphics, int mouseX, int mouseY) {
		super.renderTooltip(screen, graphics, mouseX, mouseY);
		if (!getContainer().isOpen()) {
			return;
		}
		emptySlotTooltip(graphics, ManaPileUpgradeItem.Wrapper.SOURCE_SLOT, SLOT_X, SLOT_Y, "gui.botania.sophisticated.mana_pile.slot", mouseX, mouseY);
		emptySlotTooltip(graphics, ManaPileUpgradeItem.Wrapper.CATALYST_SLOT, SLOT_X, CATALYST_Y, "gui.botania.sophisticated.mana_pile.catalyst", mouseX, mouseY);
		emptySlotTooltip(graphics, ManaPileUpgradeItem.Wrapper.INPUT_SLOT, SLOT_X, CRAFT_Y, "gui.botania.sophisticated.mana_pile.infusion_input", mouseX, mouseY);
		emptySlotTooltip(graphics, ManaPileUpgradeItem.Wrapper.OUTPUT_SLOT, OUTPUT_X, CRAFT_Y, "gui.botania.sophisticated.mana_pile.infusion_output", mouseX, mouseY);
	}

	private void emptySlotTooltip(GuiGraphics graphics, int index, int slotX, int slotY, String key, int mouseX, int mouseY) {
		int left = x + slotX;
		int top = y + slotY;
		if (mouseX >= left && mouseX < left + 18 && mouseY >= top && mouseY < top + 18
				&& getContainer().getSlots().size() > index && !getContainer().getSlots().get(index).hasItem()) {
			graphics.renderTooltip(Minecraft.getInstance().font, Component.translatable(key), mouseX, mouseY);
		}
	}

	@Override
	protected void moveSlotsToTab() {
		if (getContainer().getSlots().isEmpty()) {
			return;
		}
		moveSlot(ManaPileUpgradeItem.Wrapper.SOURCE_SLOT, SLOT_X, SLOT_Y);
		moveSlot(ManaPileUpgradeItem.Wrapper.CATALYST_SLOT, SLOT_X, CATALYST_Y);
		moveSlot(ManaPileUpgradeItem.Wrapper.INPUT_SLOT, SLOT_X, CRAFT_Y);
		moveSlot(ManaPileUpgradeItem.Wrapper.OUTPUT_SLOT, OUTPUT_X, CRAFT_Y);
	}

	private void moveSlot(int index, int slotX, int slotY) {
		List<Slot> slots = getContainer().getSlots();
		if (slots.size() > index) {
			StationUpgradeTab.moveSlot(slots.get(index), x - screen.getGuiLeft() + slotX + 1, y - screen.getGuiTop() + slotY + 1);
		}
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
