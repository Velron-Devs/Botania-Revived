package velrondevs.botania.client.integration.rei;

import com.mojang.blaze3d.systems.RenderSystem;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import velrondevs.botania.client.gui.HUDHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class BotaniaReiCategory implements DisplayCategory<BotaniaReiDisplay> {
	private final CategoryIdentifier<BotaniaReiDisplay> id;
	private final Component title;
	private final EntryStack<?> icon;
	private final int contentWidth;
	private final int contentHeight;

	protected BotaniaReiCategory(CategoryIdentifier<BotaniaReiDisplay> id, String titleKey, EntryStack<?> icon, int contentWidth, int contentHeight) {
		this.id = id;
		this.title = Component.translatable(titleKey);
		this.icon = icon;
		this.contentWidth = contentWidth;
		this.contentHeight = contentHeight;
	}

	@Override
	public CategoryIdentifier<? extends BotaniaReiDisplay> getCategoryIdentifier() {
		return id;
	}

	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public Renderer getIcon() {
		return icon;
	}

	@Override
	public int getDisplayWidth(BotaniaReiDisplay display) {
		return contentWidth + 16;
	}

	@Override
	public int getDisplayHeight() {
		return contentHeight + 16;
	}

	@Override
	public List<Widget> setupDisplay(BotaniaReiDisplay display, Rectangle bounds) {
		Point origin = new Point(bounds.getCenterX() - contentWidth / 2, bounds.getCenterY() - contentHeight / 2);
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(bounds));
		addContent(widgets, display, origin);
		return widgets;
	}

	protected abstract void addContent(List<Widget> widgets, BotaniaReiDisplay display, Point origin);

	protected static Slot slot(Point origin, int x, int y, EntryIngredient entries) {
		return Widgets.createSlot(new Point(origin.x + x, origin.y + y)).entries(entries).disableBackground();
	}

	protected static Slot slot(Point origin, int x, int y, EntryStack<?> entry) {
		return Widgets.createSlot(new Point(origin.x + x, origin.y + y)).entry(entry).disableBackground();
	}

	protected static Widget texture(Point origin, ResourceLocation texture, int x, int y, int width, int height, int u, int v) {
		int drawX = origin.x + x;
		int drawY = origin.y + y;
		return Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
			RenderSystem.enableBlend();
			graphics.blit(texture, drawX, drawY, u, v, width, height, 256, 256);
			RenderSystem.disableBlend();
		});
	}

	protected static Widget manaBar(Point origin, int x, int y, int mana, int maxMana) {
		int drawX = origin.x + x;
		int drawY = origin.y + y;
		return Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
			RenderSystem.enableBlend();
			HUDHandler.renderManaBar(graphics, drawX, drawY, 0x0000FF, 0.75F, mana, maxMana);
			RenderSystem.disableBlend();
		});
	}

	protected static int rotateX(int x, int y, int cx, int cy, double degrees) {
		double rad = Math.toRadians(degrees);
		return (int) (Math.cos(rad) * (x - cx) - Math.sin(rad) * (y - cy) + cx);
	}

	protected static int rotateY(int x, int y, int cx, int cy, double degrees) {
		double rad = Math.toRadians(degrees);
		return (int) (Math.sin(rad) * (x - cx) - Math.cos(rad) * (y - cy) + cy);
	}
}
