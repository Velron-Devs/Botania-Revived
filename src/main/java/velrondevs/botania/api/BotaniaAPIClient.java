package velrondevs.botania.api;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.block.FloatingFlower;

import java.util.Collections;
import java.util.Map;

public interface BotaniaAPIClient {
	BotaniaAPIClient INSTANCE = ServiceUtil.findService(BotaniaAPIClient.class, () -> new BotaniaAPIClient() {});

	static BotaniaAPIClient instance() {
		return INSTANCE;
	}

	default void registerIslandTypeModel(FloatingFlower.IslandType islandType, ResourceLocation model) {}

	default Map<FloatingFlower.IslandType, ResourceLocation> getRegisteredIslandTypeModels() {
		return Collections.emptyMap();
	}

	default void drawSimpleManaHUD(GuiGraphics gui, int color, int mana, int maxMana, String name) {}

	default void drawComplexManaHUD(GuiGraphics gui, int color, int mana, int maxMana, String name, ItemStack bindDisplay, boolean properlyBound) {}
}
