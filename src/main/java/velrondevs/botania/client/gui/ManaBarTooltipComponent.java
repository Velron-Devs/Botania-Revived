package velrondevs.botania.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import velrondevs.botania.api.mana.ManaBarTooltip;
import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;

public class ManaBarTooltipComponent implements ClientTooltipComponent {
	private final float percentageFull;
	private final int pickLevel;
	private final String manaText;

	private int mouseX, mouseY;

	private int totalWidth;

	public ManaBarTooltipComponent(ManaBarTooltip component) {
		this.percentageFull = component.getPercentageFull();
		this.pickLevel = component.getPickLevel();
		this.manaText = component.getMaxMana() > 0 ? component.getMana() + "/" + component.getMaxMana() : null;
	}

	@Nullable
	public static ClientTooltipComponent tryConvert(TooltipComponent component) {
		if (component instanceof ManaBarTooltip t) {
			return new ManaBarTooltipComponent(t);
		}
		return null;
	}

	@Override
	public int getHeight() {
		if (manaText == null) {
			return 0;
		}
		return pickLevel >= 0 ? 26 : 15;
	}

	@Override
	public int getWidth(Font font) {
		return manaText == null ? 0 : font.width(manaText);
	}

	public void setContext(int mouseX, int mouseY, int totalWidth) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.totalWidth = totalWidth;
	}

	@Override
	public void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource buffers) {
		if (manaText != null) {
			font.drawInBatch(manaText, x, y, 0xAAAAAA, true, matrix, buffers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
		}
		int level = pickLevel;
		if (level < 0) {
			return;
		}

		boolean ss = level >= TerraShattererItem.maxLevel();
		String rank = I18n.get("botania.rank" + pickLevel).replaceAll("&", "\u00a7");
		boolean inline = manaText != null;
		int rankX = inline ? x : mouseX;
		int rankY = inline ? y + 10 : mouseY - 16;
		int width = inline ? Math.max(totalWidth, font.width(manaText)) : totalWidth;

		font.drawInBatch(rank, rankX, rankY, 0xFFFFFF, true, matrix, buffers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
		if (!ss) {
			rank = I18n.get("botania.rank" + (level + 1)).replaceAll("&", "\u00a7");
			font.drawInBatch(rank, rankX + width - font.width(rank), rankY, 0xFFFFFF, true,
					matrix, buffers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
		}
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics gui) {
		PoseStack ps = gui.pose();
		int height = 3;
		boolean inline = manaText != null;
		int left = inline ? x : mouseX;
		int width = inline ? Math.max(totalWidth, font.width(manaText)) : totalWidth;
		int top = inline ? y + (pickLevel >= 0 ? 21 : 11) : mouseY - height - 4;

		ps.pushPose();

		if (pickLevel >= 0) {
			boolean ss = pickLevel >= TerraShattererItem.maxLevel();

			int rainbowWidth = Math.min(width - (ss ? 0 : 1), (int) (width * percentageFull));
			float huePer = width == 0 ? 0F : 1F / width;
			float hueOff = (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.01F;

			gui.fill(left - 1, top - 1, left + width + 1, top + height, 0xFF000000);
			for (int i = 0; i < rainbowWidth; i++) {
				gui.fill(left + i, top, left + i + 1, top + height, 0xFF000000 | Mth.hsvToRgb((hueOff + huePer * i) % 1F, 1F, 1F));
			}
			gui.fill(left + rainbowWidth, top, left + width, top + height, 0xFF555555);
		} else {
			int manaBarWidth = (int) Math.ceil(width * percentageFull);

			gui.fill(left - 1, top - 1, left + width + 1, top + height, 0xFF000000);
			gui.fill(left, top, left + manaBarWidth, top + height, 0xFF000000 | Mth.hsvToRgb(0.528F, ((float) Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2) + 1F) * 0.3F + 0.4F, 1F));
			gui.fill(left + manaBarWidth, top, left + width, top + height, 0xFF555555);
		}
		ps.popPose();

		if (!inline) {
			mouseX = mouseY = 0;
			totalWidth = 50;
		}
	}
}
