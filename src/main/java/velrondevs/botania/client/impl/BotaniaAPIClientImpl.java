package velrondevs.botania.client.impl;

import com.google.common.collect.Maps;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.BotaniaAPIClient;
import velrondevs.botania.api.block.FloatingFlower;
import velrondevs.botania.client.gui.HUDHandler;

import java.util.Collections;
import java.util.Map;

public class BotaniaAPIClientImpl implements BotaniaAPIClient {
	private final Map<FloatingFlower.IslandType, ResourceLocation> islandTypeModels = Maps.newHashMap();

	@Override
	public void registerIslandTypeModel(FloatingFlower.IslandType islandType, ResourceLocation model) {
		islandTypeModels.put(islandType, model);
	}

	@Override
	public Map<FloatingFlower.IslandType, ResourceLocation> getRegisteredIslandTypeModels() {
		return Collections.unmodifiableMap(islandTypeModels);
	}

	@Override
	public void drawSimpleManaHUD(GuiGraphics gui, int color, int mana, int maxMana, String name) {
		HUDHandler.drawSimpleManaHUD(gui, color, mana, maxMana, name);
	}

	@Override
	public void drawComplexManaHUD(GuiGraphics gui, int color, int mana, int maxMana, String name, ItemStack bindDisplay, boolean properlyBound) {
		HUDHandler.drawComplexManaHUD(color, gui, mana, maxMana, name, bindDisplay, properlyBound);
	}
}
