package velrondevs.botania.compat.sophisticated.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeSettingsTab;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Dimension;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;

import velrondevs.botania.compat.sophisticated.StationState;
import velrondevs.botania.compat.sophisticated.StationUpgradeContainer;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.lang.reflect.Field;
import java.util.List;

public class StationUpgradeTab<C extends StationUpgradeContainer<?, C>> extends UpgradeSettingsTab<C> {
	private static final int BAR_HEIGHT = 8;
	private static final int BAR_COLOR = 0xFF4DA6FF;
	private static final int BAR_COLOR_LIGHT = 0xFF9FE0FF;
	private static final int BAR_COLOR_DARK = 0xFF2F78D8;
	private static final int TEXT_COLOR = 0xFF404040;
	private static final int WARNING_COLOR = 0xFFB02020;
	private static final int OK_COLOR = 0xFF1E7A2E;

	private static final int HINT_OVERLAY = 0x808B8B8B;

	public record Hint(int slot, ItemStack icon, String key) {}

	public record Layout(int width, int height, int[] slotX, int[] slotY, List<int[]> backgrounds, int barX, int barY, int barWidth, int textY, List<Hint> hints) {}

	private static final Field SLOT_X = slotField("x");
	private static final Field SLOT_Y = slotField("y");

	private final Layout layout;

	public StationUpgradeTab(C container, Position position, StorageScreenBase<?> screen, String key, Layout layout) {
		super(container, position, screen,
				Component.translatable("gui.botania.sophisticated." + key),
				Component.translatable("gui.botania.sophisticated." + key + ".tooltip"));
		this.layout = layout;
		openTabDimension = new Dimension(layout.width(), layout.height());
	}

	public static Layout runicAltar() {
		int[] sx = new int[18];
		int[] sy = new int[18];
		for (int i = 0; i < 16; i++) {
			sx[i] = 3 + (i % 4) * 18;
			sy[i] = 24 + (i / 4) * 18;
		}
		sx[16] = 3;
		sy[16] = 100;
		sx[17] = 21;
		sy[17] = 100;
		List<Hint> hints = List.of(
				new Hint(16, new ItemStack(BotaniaBlocks.livingrock), "gui.botania.sophisticated.runic_altar.reagent"),
				new Hint(17, new ItemStack(BotaniaItems.twigWand), "gui.botania.sophisticated.runic_altar.wand"));
		return new Layout(80, 176, sx, sy, List.of(new int[] {3, 24, 4, 4}, new int[] {3, 100, 2, 1}), 4, 124, 72, 136, hints);
	}

	public static Layout elvenPortal() {
		int[] sx = new int[10];
		int[] sy = new int[10];
		for (int i = 0; i < 8; i++) {
			sx[i] = 3 + (i % 4) * 18;
			sy[i] = 24 + (i / 4) * 18;
		}
		sx[8] = 3;
		sy[8] = 66;
		sx[9] = 21;
		sy[9] = 66;
		ItemStack pylon = new ItemStack(BotaniaBlocks.naturaPylon);
		List<Hint> hints = List.of(
				new Hint(8, pylon, "gui.botania.sophisticated.elven_portal.pylon"),
				new Hint(9, pylon, "gui.botania.sophisticated.elven_portal.pylon"));
		return new Layout(80, 142, sx, sy, List.of(new int[] {3, 24, 4, 2}, new int[] {3, 66, 2, 1}), 4, 90, 72, 102, hints);
	}

	public static Layout petalApothecary() {
		int[] sx = new int[18];
		int[] sy = new int[18];
		for (int i = 0; i < 16; i++) {
			sx[i] = 3 + (i % 4) * 18;
			sy[i] = 24 + (i / 4) * 18;
		}
		sx[16] = 3;
		sy[16] = 100;
		sx[17] = 21;
		sy[17] = 100;
		List<Hint> hints = List.of(
				new Hint(16, new ItemStack(Items.WHEAT_SEEDS), "gui.botania.sophisticated.petal_apothecary.reagent"),
				new Hint(17, new ItemStack(Items.WATER_BUCKET), "gui.botania.sophisticated.petal_apothecary.water"));
		return new Layout(80, 160, sx, sy, List.of(new int[] {3, 24, 4, 4}, new int[] {3, 100, 2, 1}), 0, 0, 0, 124, hints);
	}

	public static Layout terraPlate() {
		int[] sx = new int[21];
		int[] sy = new int[21];
		for (int i = 0; i < 20; i++) {
			sx[i] = 3 + (i % 5) * 18;
			sy[i] = 24 + (i / 5) * 18;
		}
		sx[20] = 3;
		sy[20] = 100;
		List<Hint> hints = List.of(
				new Hint(20, new ItemStack(BotaniaItems.spark), "gui.botania.sophisticated.terra_plate.spark"));
		return new Layout(98, 176, sx, sy, List.of(new int[] {3, 24, 5, 4}, new int[] {3, 100, 1, 1}), 4, 124, 90, 136, hints);
	}

	public static Layout brewery() {
		int[] sx = new int[7];
		int[] sy = new int[7];
		sx[0] = 3;
		sy[0] = 33;
		for (int i = 0; i < 6; i++) {
			sx[i + 1] = 25 + (i % 3) * 18;
			sy[i + 1] = 24 + (i / 3) * 18;
		}
		return new Layout(84, 116, sx, sy, List.of(new int[] {3, 33, 1, 1}, new int[] {25, 24, 3, 2}), 4, 66, 72, 78, List.of());
	}

	@Override
	protected void renderBg(GuiGraphics graphics, Minecraft minecraft, int mouseX, int mouseY) {
		super.renderBg(graphics, minecraft, mouseX, mouseY);
		if (!getContainer().isOpen()) {
			return;
		}
		moveSlotsToTab();
		for (int[] background : layout.backgrounds()) {
			GuiHelper.renderSlotsBackground(graphics, x + background[0], y + background[1], background[2], background[3]);
		}
		List<Slot> slots = getContainer().getSlots();
		for (Hint hint : layout.hints()) {
			if (hint.slot() < slots.size() && slots.get(hint.slot()).getItem().isEmpty()) {
				int hintX = x + layout.slotX()[hint.slot()] + 1;
				int hintY = y + layout.slotY()[hint.slot()] + 1;
				graphics.renderFakeItem(hint.icon(), hintX, hintY);
				graphics.fill(RenderType.guiGhostRecipeOverlay(), hintX, hintY, hintX + 16, hintY + 16, HINT_OVERLAY);
			}
		}
		StationState state = getContainer().getState();
		if (layout.barWidth() > 0) {
			int left = x + layout.barX();
			int top = y + layout.barY();
			GuiHelper.renderControlBackground(graphics, left - 2, top - 2, layout.barWidth() + 4, BAR_HEIGHT + 4);
			int filled = fillWidth(state);
			if (filled > 0) {
				graphics.fill(left, top, left + filled, top + BAR_HEIGHT, BAR_COLOR);
				graphics.fill(left, top, left + filled, top + 2, BAR_COLOR_LIGHT);
				graphics.fill(left, top + BAR_HEIGHT - 2, left + filled, top + BAR_HEIGHT, BAR_COLOR_DARK);
			}
		}
		Component message = Component.translatable(messageKey(state.status()));
		int color = state.status() == StationState.WORKING ? OK_COLOR : state.status() == StationState.IDLE ? TEXT_COLOR : WARNING_COLOR;
		graphics.drawWordWrap(minecraft.font, message, x + 4, y + layout.textY(), layout.width() - 8, color);
		if (state.cost() > 0 && state.status() != StationState.IDLE) {
			graphics.drawString(minecraft.font, compact(state.mana()) + " / " + compact(state.cost()), x + 4, y + layout.textY() + 28, TEXT_COLOR, false);
		}
	}

	@Override
	public void renderTooltip(Screen screen, GuiGraphics graphics, int mouseX, int mouseY) {
		super.renderTooltip(screen, graphics, mouseX, mouseY);
		if (!getContainer().isOpen()) {
			return;
		}
		List<Slot> slots = getContainer().getSlots();
		for (Hint hint : layout.hints()) {
			int hintX = x + layout.slotX()[hint.slot()] + 1;
			int hintY = y + layout.slotY()[hint.slot()] + 1;
			if (hint.slot() < slots.size() && slots.get(hint.slot()).getItem().isEmpty()
					&& mouseX >= hintX && mouseX < hintX + 16 && mouseY >= hintY && mouseY < hintY + 16) {
				graphics.renderTooltip(Minecraft.getInstance().font, Component.translatable(hint.key()), mouseX, mouseY);
				return;
			}
		}
		int left = x + layout.barX();
		int top = y + layout.barY();
		StationState state = getContainer().getState();
		if (layout.barWidth() > 0 && state.cost() > 0 && mouseX >= left && mouseX < left + layout.barWidth() && mouseY >= top && mouseY < top + BAR_HEIGHT) {
			String text = String.format("%,d / %,d", state.mana(), state.cost());
			graphics.renderTooltip(Minecraft.getInstance().font, Component.literal(text), mouseX, mouseY);
		}
	}

	@Override
	protected void moveSlotsToTab() {
		List<Slot> slots = getContainer().getSlots();
		int count = Math.min(slots.size(), layout.slotX().length);
		for (int i = 0; i < count; i++) {
			Slot slot = slots.get(i);
			moveSlot(slot, x - screen.getGuiLeft() + layout.slotX()[i] + 1, y - screen.getGuiTop() + layout.slotY()[i] + 1);
		}
	}

	static void moveSlot(Slot slot, int slotX, int slotY) {
		try {
			SLOT_X.setInt(slot, slotX);
			SLOT_Y.setInt(slot, slotY);
		} catch (IllegalAccessException ignored) {
		}
	}

	private static Field slotField(String name) {
		try {
			Field field = Slot.class.getField(name);
			field.setAccessible(true);
			return field;
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		}
	}

	private int fillWidth(StationState state) {
		if (state.status() == StationState.WORKING) {
			return layout.barWidth();
		}
		if (state.cost() <= 0 || state.status() == StationState.IDLE) {
			return 0;
		}
		return (int) Math.min(layout.barWidth(), Math.round((double) layout.barWidth() * state.mana() / state.cost()));
	}

	private static String messageKey(int status) {
		return switch (status) {
			case StationState.NO_PILE -> "gui.botania.sophisticated.status.no_pile";
			case StationState.NEED_MANA -> "gui.botania.sophisticated.status.need_mana";
			case StationState.NO_SPACE -> "gui.botania.sophisticated.status.no_space";
			case StationState.NO_REAGENT -> "gui.botania.sophisticated.status.no_reagent";
			case StationState.WORKING -> "gui.botania.sophisticated.status.working";
			case StationState.NO_WAND -> "gui.botania.sophisticated.status.no_wand";
			case StationState.NO_PYLONS -> "gui.botania.sophisticated.status.no_pylons";
			case StationState.NO_WATER -> "gui.botania.sophisticated.status.no_water";
			case StationState.NO_SEED -> "gui.botania.sophisticated.status.no_seed";
			case StationState.NO_SPARK -> "gui.botania.sophisticated.status.no_spark";
			default -> "gui.botania.sophisticated.status.idle";
		};
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
