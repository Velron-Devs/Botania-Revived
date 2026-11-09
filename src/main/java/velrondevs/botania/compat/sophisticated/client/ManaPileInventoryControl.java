package velrondevs.botania.compat.sophisticated.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeInventoryControlBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Dimension;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.TextureBlitData;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.UV;
import org.joml.Matrix4f;

import velrondevs.botania.compat.sophisticated.ManaPileUpgradeContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManaPileInventoryControl extends UpgradeInventoryControlBase {
	private static final TextureBlitData BACKGROUND_TOP = new TextureBlitData(GuiHelper.GUI_CONTROLS, Dimension.SQUARE_256, new UV(29, 30), Dimension.SQUARE_18);
	private static final TextureBlitData BACKGROUND_MIDDLE = new TextureBlitData(GuiHelper.GUI_CONTROLS, Dimension.SQUARE_256, new UV(29, 48), Dimension.SQUARE_18);
	private static final TextureBlitData BACKGROUND_BOTTOM = new TextureBlitData(GuiHelper.GUI_CONTROLS, Dimension.SQUARE_256, new UV(29, 66), Dimension.SQUARE_18);
	private static final TextureBlitData OVERLAY = new TextureBlitData(GuiHelper.GUI_CONTROLS, Dimension.SQUARE_256, new UV(47, 56), new Dimension(16, 18));
	private static final TextureBlitData SEGMENT = new TextureBlitData(GuiHelper.GUI_CONTROLS, Dimension.SQUARE_256, new UV(47, 74), new Dimension(16, 6));
	private static final TextureBlitData CONNECTION_TOP = new TextureBlitData(GuiHelper.GUI_CONTROLS, Dimension.SQUARE_256, new UV(47, 48), new Dimension(16, 4));
	private static final TextureBlitData CONNECTION_BOTTOM = new TextureBlitData(GuiHelper.GUI_CONTROLS, Dimension.SQUARE_256, new UV(47, 52), new Dimension(16, 4));

	private final ManaPileUpgradeContainer container;
	private final Position pos;
	private final int height;
	private final StorageScreenBase<?> screen;

	public ManaPileInventoryControl(int upgradeIndex, ManaPileUpgradeContainer container, Position pos, int height, StorageScreenBase<?> screen) {
		this.container = container;
		this.pos = pos;
		this.height = height;
		this.screen = screen;
	}

	private int barLeft() {
		return pos.x() + 9;
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY) {
		int edge = height < 36 ? height / 2 : 18;
		GuiHelper.blit(graphics, barLeft(), pos.y(), BACKGROUND_TOP, 18, edge);
		int offset = 18;
		for (int i = 0; i < (height - 36) / 18; i++) {
			GuiHelper.blit(graphics, barLeft(), pos.y() + offset, BACKGROUND_MIDDLE);
			offset += 18;
		}
		GuiHelper.blit(graphics, barLeft(), pos.y() + (height < 36 ? height / 2 : offset), BACKGROUND_BOTTOM, 18, edge);
		offset = 0;
		for (int i = 0; i < height / 18; i++) {
			GuiHelper.blit(graphics, barLeft() + 1, pos.y() + offset, OVERLAY);
			offset += 18;
		}
		renderFill(graphics);
		GuiHelper.blit(graphics, barLeft() + 1, pos.y(), CONNECTION_TOP);
		GuiHelper.blit(graphics, barLeft() + 1, pos.y() + height - 4, CONNECTION_BOTTOM);
	}

	@Override
	public void renderErrorOverlay(GuiGraphics graphics) {
		screen.renderOverlay(graphics, StorageScreenBase.ERROR_SLOT_COLOR, barLeft() + 1, pos.y() + 1, 16, height - 2);
	}

	@Override
	public void renderTooltip(StorageScreenBase<?> screen, GuiGraphics graphics, int mouseX, int mouseY) {
		int left = screen.getGuiLeft() + pos.x() + 10;
		int top = screen.getGuiTop() + pos.y() + 1;
		if (mouseX >= left && mouseX < left + 16 && mouseY >= top && mouseY < top + height - 2) {
			List<Component> lines = new ArrayList<>();
			lines.add(Component.translatable("gui.botania.sophisticated.mana_pile.contents",
					String.format("%,d", container.getMana()), String.format("%,d", container.getCapacity())));
			graphics.renderTooltip(Minecraft.getInstance().font, lines, Optional.empty(), mouseX, mouseY);
		}
	}

	private void renderFill(GuiGraphics graphics) {
		int mana = container.getMana();
		int capacity = Math.max(1, container.getCapacity());
		int segmentHeight = SEGMENT.getHeight();
		int segments = height / segmentHeight;
		int filled = (int) Math.min((long) segments, (long) (segments * ((double) mana / capacity)));
		if (mana > 0 && filled == 0 && segments > 0) {
			filled = 1;
		}
		boolean enabled = container.isPileEnabled();
		int[] bottom = enabled ? new int[] {47, 120, 216} : new int[] {96, 96, 96};
		int[] top = enabled ? new int[] {159, 224, 255} : new int[] {170, 170, 170};
		Matrix4f matrix = graphics.pose().last().pose();
		for (int i = 0; i < filled; i++) {
			float f = segments > 1 ? (float) i / (segments - 1) : 1f;
			int red = (int) (bottom[0] * (1 - f) + top[0] * f);
			int green = (int) (bottom[1] * (1 - f) + top[1] * f);
			int blue = (int) (bottom[2] * (1 - f) + top[2] * f);
			int color = red << 16 | green << 8 | blue | 0xFF000000;
			GuiHelper.coloredBlit(matrix, barLeft() + 1, pos.y() + height - (i + 1) * segmentHeight, SEGMENT, color);
		}
	}
}
