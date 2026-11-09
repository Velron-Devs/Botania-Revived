package velrondevs.botania.compat.sophisticated.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Dimension;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.TextureBlitData;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.UV;

import java.util.List;
import java.util.Optional;

public final class ManaPileBar {
	public static final int WIDTH = 18;
	private static final TextureBlitData BACKGROUND_TOP = new TextureBlitData(GuiHelper.GUI_CONTROLS, Dimension.SQUARE_256, new UV(29, 30), Dimension.SQUARE_18);
	private static final TextureBlitData BACKGROUND_MIDDLE = new TextureBlitData(GuiHelper.GUI_CONTROLS, Dimension.SQUARE_256, new UV(29, 48), Dimension.SQUARE_18);
	private static final TextureBlitData BACKGROUND_BOTTOM = new TextureBlitData(GuiHelper.GUI_CONTROLS, Dimension.SQUARE_256, new UV(29, 66), Dimension.SQUARE_18);
	private static final TextureBlitData OVERLAY = new TextureBlitData(GuiHelper.GUI_CONTROLS, Dimension.SQUARE_256, new UV(47, 56), new Dimension(16, 18));
	private static final TextureBlitData SEGMENT = new TextureBlitData(GuiHelper.GUI_CONTROLS, Dimension.SQUARE_256, new UV(47, 74), new Dimension(16, 6));
	private static final TextureBlitData CONNECTION_TOP = new TextureBlitData(GuiHelper.GUI_CONTROLS, Dimension.SQUARE_256, new UV(47, 48), new Dimension(16, 4));
	private static final TextureBlitData CONNECTION_BOTTOM = new TextureBlitData(GuiHelper.GUI_CONTROLS, Dimension.SQUARE_256, new UV(47, 52), new Dimension(16, 4));

	private ManaPileBar() {}

	public static void extract(GuiGraphicsExtractor graphics, int left, int top, int height, int mana, int capacity, boolean enabled) {
		int edge = height < 36 ? height / 2 : 18;
		GuiHelper.blit(graphics, left, top, BACKGROUND_TOP, 18, edge);
		int offset = 18;
		for (int i = 0; i < (height - 36) / 18; i++) {
			GuiHelper.blit(graphics, left, top + offset, BACKGROUND_MIDDLE);
			offset += 18;
		}
		GuiHelper.blit(graphics, left, top + (height < 36 ? height / 2 : offset), BACKGROUND_BOTTOM, 18, edge);
		offset = 0;
		for (int i = 0; i < height / 18; i++) {
			GuiHelper.blit(graphics, left + 1, top + offset, OVERLAY);
			offset += 18;
		}
		extractFill(graphics, left, top, height, mana, Math.max(1, capacity), enabled);
		GuiHelper.blit(graphics, left + 1, top, CONNECTION_TOP);
		GuiHelper.blit(graphics, left + 1, top + height - 4, CONNECTION_BOTTOM);
	}

	public static boolean isHovered(int left, int top, int height, int mouseX, int mouseY) {
		return mouseX >= left + 1 && mouseX < left + 17 && mouseY >= top + 1 && mouseY < top + height - 1;
	}

	public static void extractTooltip(GuiGraphicsExtractor graphics, Font font, int mana, int capacity, int mouseX, int mouseY) {
		List<Component> lines = List.of(Component.translatable("gui.botania.sophisticated.mana_pile.contents",
				String.format("%,d", mana), String.format("%,d", capacity)));
		graphics.setTooltipForNextFrame(font, lines, Optional.empty(), mouseX, mouseY);
	}

	private static void extractFill(GuiGraphicsExtractor graphics, int left, int top, int height, int mana, int capacity, boolean enabled) {
		int segmentHeight = SEGMENT.getHeight();
		int segments = height / segmentHeight;
		int filled = (int) Math.min((long) segments, (long) (segments * ((double) mana / capacity)));
		if (mana > 0 && filled == 0 && segments > 0) {
			filled = 1;
		}
		int[] bottom = enabled ? new int[] {47, 120, 216} : new int[] {96, 96, 96};
		int[] upper = enabled ? new int[] {159, 224, 255} : new int[] {170, 170, 170};
		for (int i = 0; i < filled; i++) {
			float f = segments > 1 ? (float) i / (segments - 1) : 1f;
			int red = (int) (bottom[0] * (1 - f) + upper[0] * f);
			int green = (int) (bottom[1] * (1 - f) + upper[1] * f);
			int blue = (int) (bottom[2] * (1 - f) + upper[2] * f);
			int color = red << 16 | green << 8 | blue | 0xFF000000;
			GuiHelper.coloredBlit(graphics, left + 1, top + height - (i + 1) * segmentHeight, SEGMENT, color);
		}
	}
}
