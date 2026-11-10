package velrondevs.botania.compat.sophisticated;

import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerRegistry;

import velrondevs.botania.common.impl.mana.PlayerManaSources;
import velrondevs.botania.compat.sophisticated.backpack.BackpackManaCapability;
import velrondevs.botania.compat.sophisticated.backpack.BackpackSwitch;
import velrondevs.botania.compat.sophisticated.backpack.BotaniaBackpackRegistry;
import velrondevs.botania.compat.sophisticated.client.BotaniaBackpackClient;
import velrondevs.botania.compat.sophisticated.client.BotaniaStorageClient;
import velrondevs.botania.compat.sophisticated.client.SophisticatedClient;
import velrondevs.botania.compat.sophisticated.storage.BotaniaStorageRegistry;
import velrondevs.botania.compat.sophisticated.storage.StorageManaCapability;
import velrondevs.botania.compat.sophisticated.storage.StorageSwitch;
import velrondevs.botania.compat.sophisticated.transfer.StationTransferPayload;
import velrondevs.botania.module.ModuleContext;

import net.minecraft.world.item.ItemStack;

public final class SophisticatedCompat {
	private SophisticatedCompat() {}

	static void register(ModuleContext ctx) {
		ctx.bind(Registries.DATA_COMPONENT_TYPE, consumer -> {
			consumer.accept(SophisticatedComponents.MANA, SophisticatedItems.id("upgrade_mana"));
			consumer.accept(SophisticatedComponents.STATION, SophisticatedItems.id("station_state"));
			consumer.accept(SophisticatedComponents.PILE_DISCHARGE, SophisticatedItems.id("pile_discharge"));
			consumer.accept(SophisticatedComponents.PILE_INFUSION, SophisticatedItems.id("pile_infusion"));
			consumer.accept(SophisticatedComponents.PILE_INFUSED, SophisticatedItems.id("pile_infused"));
		});
		ctx.bindItems(SophisticatedItems::register);
		ctx.modBus().addListener((RegisterEvent event) -> {
			if (event.getRegistryKey().equals(Registries.MENU)) {
				UpgradeContainerRegistry.register(SophisticatedItems.id(SophisticatedItems.MANA_PILE), ManaPileUpgradeContainer.TYPE);
				UpgradeContainerRegistry.register(SophisticatedItems.id(SophisticatedItems.RUNIC_ALTAR), RunicAltarUpgradeContainer.TYPE);
				UpgradeContainerRegistry.register(SophisticatedItems.id(SophisticatedItems.ELVEN_PORTAL), ElvenPortalUpgradeContainer.TYPE);
				UpgradeContainerRegistry.register(SophisticatedItems.id(SophisticatedItems.BREWERY), BreweryUpgradeContainer.TYPE);
				UpgradeContainerRegistry.register(SophisticatedItems.id(SophisticatedItems.PETAL_APOTHECARY), PetalApothecaryUpgradeContainer.TYPE);
				UpgradeContainerRegistry.register(SophisticatedItems.id(SophisticatedItems.TERRA_PLATE), TerraPlateUpgradeContainer.TYPE);
			}
		});
		ctx.modBus().addListener(StationTransferPayload::register);
		boolean storage = SophisticatedModule.INSTANCE.hasStorage() && StorageSwitch.enabled();
		ctx.creativeSection("sophisticated", () -> new ItemStack(SophisticatedItems.MANA_PILE_UPGRADE), storage ? BotaniaStorageRegistry::addCreativeTabItems : null);
		boolean backpacks = SophisticatedModule.INSTANCE.hasBackpacks();
		boolean storages = SophisticatedModule.INSTANCE.hasStorage();
		ctx.modBus().addListener((RegisterCapabilitiesEvent event) -> {
			if (backpacks) {
				BackpackManaCapability.register(event);
			}
			if (storages) {
				StorageManaCapability.register(event);
			}
		});
		if (SophisticatedModule.INSTANCE.hasBackpacks()) {
			PlayerManaSources.register(new SophisticatedManaSource());
			PileChargeHandler.register();
			if (BackpackSwitch.enabled()) {
				BotaniaBackpackRegistry.register(ctx);
			}
		}
		if (SophisticatedModule.INSTANCE.hasStorage() && StorageSwitch.enabled()) {
			BotaniaStorageRegistry.register(ctx);
		}
	}

	static void registerClient(ModuleContext ctx) {
		SophisticatedClient.init(ctx.modBus());
		if (SophisticatedModule.INSTANCE.hasBackpacks() && BackpackSwitch.enabled()) {
			BotaniaBackpackClient.init(ctx.modBus());
		}
		if (SophisticatedModule.INSTANCE.hasStorage() && StorageSwitch.enabled()) {
			BotaniaStorageClient.init(ctx.modBus());
		}
	}
}
