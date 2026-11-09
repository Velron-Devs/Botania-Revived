package velrondevs.botania.compat.sophisticated.rei;

import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;

import net.minecraft.world.item.BlockItem;

import velrondevs.botania.client.integration.rei.BotaniaReiCategories;
import velrondevs.botania.compat.sophisticated.SophisticatedItems;
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

	public static void registerWorkstations(CategoryRegistry registry) {
		if (!SophisticatedModule.INSTANCE.isEnabled() || !SophisticatedModule.INSTANCE.isAvailable()) {
			return;
		}
		registry.addWorkstations(BotaniaReiCategories.MANA_INFUSION, EntryStacks.of(SophisticatedItems.MANA_PILE_UPGRADE));
		registry.addWorkstations(BotaniaReiCategories.RUNIC_ALTAR, EntryStacks.of(SophisticatedItems.RUNIC_ALTAR_UPGRADE));
		registry.addWorkstations(BotaniaReiCategories.ELVEN_TRADE, EntryStacks.of(SophisticatedItems.ELVEN_PORTAL_UPGRADE));
		registry.addWorkstations(BotaniaReiCategories.BOTANICAL_BREWERY, EntryStacks.of(SophisticatedItems.BREWERY_UPGRADE));
	}

	public static void registerTransfers(TransferHandlerRegistry registry) {
		registry.register(new StationReiTransferHandler());
	}
}
