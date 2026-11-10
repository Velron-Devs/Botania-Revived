package velrondevs.botania.integration;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;

import velrondevs.botania.registry.BotaniaItems;

public final class InventorySorterIntegration {
	public static final String MOD_ID = "inventorysorter";

	private InventorySorterIntegration() {}

	public static void init(IEventBus modBus) {
		modBus.addListener(InventorySorterIntegration::sendImc);
	}

	private static void sendImc(InterModEnqueueEvent evt) {
		InterModComms.sendTo(MOD_ID, "containerblacklist",
				() -> BuiltInRegistries.MENU.getKey(BotaniaItems.FLOWER_BAG_CONTAINER));
	}
}
