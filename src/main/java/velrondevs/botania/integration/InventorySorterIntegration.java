package velrondevs.botania.integration;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import velrondevs.botania.Botania;

import velrondevs.botania.registry.BotaniaItems;

public class InventorySorterIntegration {
	public static void init() {
		Botania.modBus()
				.addListener(InventorySorterIntegration::sendImc);
	}

	private static void sendImc(InterModEnqueueEvent evt) {

		InterModComms.sendTo("inventorysorter", "containerblacklist",
				() -> BuiltInRegistries.MENU.getKey(BotaniaItems.FLOWER_BAG_CONTAINER));
	}
}
