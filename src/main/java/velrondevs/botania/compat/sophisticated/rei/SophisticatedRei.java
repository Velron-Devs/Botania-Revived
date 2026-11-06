package velrondevs.botania.compat.sophisticated.rei;

import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;

import net.minecraft.world.item.BlockItem;

import velrondevs.botania.compat.sophisticated.SophisticatedModule;
import velrondevs.botania.compat.sophisticated.storage.BotaniaStorageRegistry;
import velrondevs.botania.compat.sophisticated.storage.StorageSwitch;

public final class SophisticatedRei {
	private SophisticatedRei() {}

	public static void registerComparators(ItemComparatorRegistry registry) {
		if (!SophisticatedModule.INSTANCE.isAvailable() || !SophisticatedModule.INSTANCE.hasStorage() || !StorageSwitch.enabled()) {
			return;
		}
		for (BlockItem item : BotaniaStorageRegistry.barrelItems()) {
			registry.registerComponents(item);
		}
		for (BlockItem item : BotaniaStorageRegistry.chestItems()) {
			registry.registerComponents(item);
		}
	}

	public static void registerTransfers(TransferHandlerRegistry registry) {
		registry.register(new StationReiTransferHandler());
	}
}
