package velrondevs.botania.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.mana.ManaBarTooltip;
import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;

public class ManaBarTooltipComponent implements ClientTooltipComponent {
	private static final int BAR_HEIGHT = 3;
	private static final int TEXT_HEIGHT = 10;
	private final float percentageFull;
	private final int pickLevel;
	private final String manaText;

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
	public int getHeight(Font font) {
		return BAR_HEIGHT + 4 + (pickLevel >= 0 ? TEXT_HEIGHT : 0) + (manaText != null ? TEXT_HEIGHT : 0);
	}

	@Override
	public int getWidth(Font font) {
		return manaText != null ? font.width(manaText) : 0;
	}

	@Override
	public void extractImage(Font font, int x, int y, int totalWidth, int h, GuiGraphicsExtractor gui) {
		int top = y + 2;
		if (pickLevel >= 0) {
			boolean ss = pickLevel >= TerraShattererItem.maxLevel();
			String rank = I18n.get("botania.rank" + pickLevel).replaceAll("&", "§");
			gui.text(font, rank, x, top, 0xFFFFFFFF, true);
			if (!ss) {
				String next = I18n.get("botania.rank" + (pickLevel + 1)).replaceAll("&", "§");
				gui.text(font, next, x + totalWidth - font.width(next), top, 0xFFFFFFFF, true);
			}
			top += TEXT_HEIGHT;
			if (manaText != null) {
				gui.text(font, manaText, x, top, 0xFFAAAAAA, true);
				top += TEXT_HEIGHT;
			}

			int rainbowWidth = Math.min(totalWidth - (ss ? 0 : 1), (int) (totalWidth * percentageFull));
			float huePer = totalWidth == 0 ? 0F : 1F / totalWidth;
			float hueOff = (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.01F;

			gui.fill(x - 1, top - 1, x + totalWidth + 1, top + BAR_HEIGHT + 1, 0xFF000000);
			for (int i = 0; i < rainbowWidth; i++) {
				gui.fill(x + i, top, x + i + 1, top + BAR_HEIGHT, 0xFF000000 | Mth.hsvToRgb((hueOff + huePer * i) % 1F, 1F, 1F));
			}
			gui.fill(x + rainbowWidth, top, x + totalWidth, top + BAR_HEIGHT, 0xFF555555);
		} else {
			if (manaText != null) {
				gui.text(font, manaText, x, top, 0xFFAAAAAA, true);
				top += TEXT_HEIGHT;
			}
			int manaBarWidth = (int) Math.ceil(totalWidth * percentageFull);

			gui.fill(x - 1, top - 1, x + totalWidth + 1, top + BAR_HEIGHT + 1, 0xFF000000);
			gui.fill(x, top, x + manaBarWidth, top + BAR_HEIGHT, 0xFF000000 | Mth.hsvToRgb(0.528F, ((float) Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2) + 1F) * 0.3F + 0.4F, 1F));
			gui.fill(x + manaBarWidth, top, x + totalWidth, top + BAR_HEIGHT, 0xFF555555);
		}
	}
}
