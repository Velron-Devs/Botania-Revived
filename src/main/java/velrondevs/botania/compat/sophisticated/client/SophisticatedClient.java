package velrondevs.botania.compat.sophisticated.client;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeGuiManager;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;

import velrondevs.botania.compat.sophisticated.BreweryUpgradeContainer;
import velrondevs.botania.compat.sophisticated.ElvenPortalUpgradeContainer;
import velrondevs.botania.compat.sophisticated.ManaPileUpgradeContainer;
import velrondevs.botania.compat.sophisticated.RunicAltarUpgradeContainer;

public final class SophisticatedClient {
	private SophisticatedClient() {}

	public static void init(IEventBus modBus) {
		modBus.addListener((RegisterMenuScreensEvent event) -> {
			UpgradeGuiManager.registerTab(ManaPileUpgradeContainer.TYPE, ManaPileUpgradeTab::new);
			UpgradeGuiManager.registerInventoryControl(ManaPileUpgradeContainer.TYPE, ManaPileInventoryControl::new);
			UpgradeGuiManager.registerTab(RunicAltarUpgradeContainer.TYPE,
					(RunicAltarUpgradeContainer container, Position position, StorageScreenBase<?> screen) -> new StationUpgradeTab<RunicAltarUpgradeContainer>(container, position, screen, "runic_altar", StationUpgradeTab.runicAltar()));
			UpgradeGuiManager.registerTab(ElvenPortalUpgradeContainer.TYPE,
					(ElvenPortalUpgradeContainer container, Position position, StorageScreenBase<?> screen) -> new StationUpgradeTab<ElvenPortalUpgradeContainer>(container, position, screen, "elven_portal", StationUpgradeTab.elvenPortal()));
			UpgradeGuiManager.registerTab(BreweryUpgradeContainer.TYPE,
					(BreweryUpgradeContainer container, Position position, StorageScreenBase<?> screen) -> new StationUpgradeTab<BreweryUpgradeContainer>(container, position, screen, "brewery", StationUpgradeTab.brewery()));
		});
	}
}
