package velrondevs.botania.client.impl;

import com.google.common.collect.Maps;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.BotaniaAPIClient;
import velrondevs.botania.api.block.FloatingFlower;
import velrondevs.botania.client.gui.HUDHandler;

import java.util.Collections;
import java.util.Map;

public class BotaniaAPIClientImpl implements BotaniaAPIClient {
	private final Map<FloatingFlower.IslandType, Identifier> islandTypeModels = Maps.newHashMap();

	@Override
	public void registerIslandTypeModel(FloatingFlower.IslandType islandType, Identifier model) {
		islandTypeModels.put(islandType, model);
	}

	@Override
	public Map<FloatingFlower.IslandType, Identifier> getRegisteredIslandTypeModels() {
		return Collections.unmodifiableMap(islandTypeModels);
	}

	@Override
	public void drawSimpleManaHUD(GuiGraphicsExtractor gui, int color, int mana, int maxMana, String name) {
		HUDHandler.drawSimpleManaHUD(gui, color, mana, maxMana, name);
	}

	@Override
	public void drawComplexManaHUD(GuiGraphicsExtractor gui, int color, int mana, int maxMana, String name, ItemStack bindDisplay, boolean properlyBound) {
		HUDHandler.drawComplexManaHUD(color, gui, mana, maxMana, name, bindDisplay, properlyBound);
	}
}
