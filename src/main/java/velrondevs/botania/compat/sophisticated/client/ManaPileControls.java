package velrondevs.botania.compat.sophisticated.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeInventoryControlBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;

import velrondevs.botania.compat.sophisticated.ManaPileUpgradeContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ManaPileControls {
	private static final int CONTROL_WIDTH = 36;
	private static final int BAR_OFFSET = 9;
	private static final int TOP = 17;

	private record Bar(ManaPileUpgradeContainer container, int left, int height) {}

	private ManaPileControls() {}

	private static List<Bar> bars(StorageContainerMenuBase<?> menu, Map<Integer, UpgradeInventoryControlBase> controls, int slotsOnLine, int imageHeight) {
		List<Bar> bars = new ArrayList<>();
		int reserved = menu.getColumnsTaken() * 18;
		if (reserved <= 0) {
			return bars;
		}
		int rows = WideStorageLayout.applies(menu) ? WideStorageLayout.rows(menu) : menu.getNumberOfRows();
		int visibleRows = Math.max(1, Math.min((imageHeight - 114) / 18, rows));
		int scrollBarOffset = visibleRows < rows ? 6 : 0;
		int used = controls.size() * CONTROL_WIDTH;
		for (Map.Entry<Integer, UpgradeContainerBase<?, ?>> entry : menu.getUpgradeContainers().entrySet()) {
			if (!(entry.getValue() instanceof ManaPileUpgradeContainer pile)) {
				continue;
			}
			if (used + CONTROL_WIDTH > reserved) {
				break;
			}
			bars.add(new Bar(pile, 7 + slotsOnLine * 18 + scrollBarOffset + used + BAR_OFFSET, visibleRows * 18));
			used += CONTROL_WIDTH;
		}
		return bars;
	}

	public static void extract(GuiGraphicsExtractor graphics, StorageContainerMenuBase<?> menu, Map<Integer, UpgradeInventoryControlBase> controls,
			int slotsOnLine, int imageHeight) {
		for (Bar bar : bars(menu, controls, slotsOnLine, imageHeight)) {
			ManaPileBar.extract(graphics, bar.left(), TOP, bar.height(), bar.container().getMana(), bar.container().getCapacity(), bar.container().isPileEnabled());
		}
	}

	public static void extractTooltip(GuiGraphicsExtractor graphics, Font font, StorageContainerMenuBase<?> menu, Map<Integer, UpgradeInventoryControlBase> controls,
			int slotsOnLine, int imageHeight, int guiLeft, int guiTop, int mouseX, int mouseY) {
		for (Bar bar : bars(menu, controls, slotsOnLine, imageHeight)) {
			if (ManaPileBar.isHovered(guiLeft + bar.left(), guiTop + TOP, bar.height(), mouseX, mouseY)) {
				ManaPileBar.extractTooltip(graphics, font, bar.container().getMana(), bar.container().getCapacity(), mouseX, mouseY);
				return;
			}
		}
	}
}
