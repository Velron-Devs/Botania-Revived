package velrondevs.botania.compat.sophisticated;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import velrondevs.botania.compat.sophisticated.storage.BotaniaStorageTier;
import velrondevs.botania.config.BotaniaConfigSpec;

public final class SophisticatedConfig {
	private static final int LEGACY_PILE_BASE_CAPACITY = 500000;

	private SophisticatedConfig() {}

	static void registerMigration(IEventBus bus) {
		if (bus != null) {
			bus.addListener((ModConfigEvent.Loading event) -> migrateDefaults(event));
		}
	}

	private static void migrateDefaults(ModConfigEvent.Loading event) {
		if (!BotaniaConfigSpec.isCommonSpec(event.getConfig())) {
			return;
		}
		BotaniaConfigSpec.SophisticatedValues values = values();
		try {
			if (!values.pileMigrateDefaults.get() || values.pileCapacityMigrated.get()) {
				return;
			}
			if (values.pileBaseCapacity.get() == LEGACY_PILE_BASE_CAPACITY) {
				values.pileBaseCapacity.set(BotaniaConfigSpec.SophisticatedValues.DEFAULT_PILE_BASE_CAPACITY);
			}
			values.pileCapacityMigrated.set(true);
			BotaniaConfigSpec.saveCommon();
		} catch (IllegalStateException e) {
		}
	}

	private static BotaniaConfigSpec.SophisticatedValues values() {
		return BotaniaConfigSpec.sophisticated();
	}

	private static <T> T safe(ModConfigSpec.ConfigValue<T> value) {
		return BotaniaConfigSpec.get(value);
	}

	public static int pileBaseCapacity() {
		return safe(values().pileBaseCapacity);
	}

	public static double pileStackRatio() {
		return safe(values().pileStackRatio);
	}

	public static int pileMaxPerStorage() {
		return safe(values().pileMaxPerStorage);
	}

	public static boolean pileAbsorbFromItems() {
		return safe(values().pileAbsorbFromItems);
	}

	public static int pileAbsorbInterval() {
		return safe(values().pileAbsorbInterval);
	}

	public static int pileAbsorbAmount() {
		return safe(values().pileAbsorbAmount);
	}

	public static boolean pileAbsorbFromSlot() {
		return safe(values().pileAbsorbFromSlot);
	}

	public static boolean pilePullFromPlayer() {
		return safe(values().pilePullFromPlayer);
	}

	public static boolean pileReceiveFromWorld() {
		return safe(values().pileReceiveFromWorld);
	}

	public static int pileInfusionInterval() {
		return safe(values().pileInfusionInterval);
	}

	public static int pileInfusionMaxPerInterval() {
		return safe(values().pileInfusionMax);
	}

	public static boolean pileInfusionAutoFeed() {
		return safe(values().pileInfusionAutoFeed);
	}

	public static boolean pileInfusionAutoOutput() {
		return safe(values().pileInfusionAutoOutput);
	}

	public static boolean stationAutoFeed() {
		return safe(values().stationAutoFeed);
	}

	public static int stationMaxPerInterval() {
		return safe(values().stationMaxPerInterval);
	}

	public static boolean pileChargeItems() {
		return safe(values().pileChargeItems);
	}

	public static int pileChargeInterval() {
		return safe(values().pileChargeInterval);
	}

	public static int pileChargeRate() {
		return safe(values().pileChargeRate);
	}

	public static boolean altarEnabled() {
		return safe(values().altarEnabled);
	}

	public static int altarMaxPerStorage() {
		return safe(values().altarMaxPerStorage);
	}

	public static int altarInterval() {
		return safe(values().altarInterval);
	}

	public static int altarCostPercent() {
		return safe(values().altarCostPercent);
	}

	public static boolean altarRequireWand() {
		return safe(values().altarRequireWand);
	}

	public static boolean portalRequirePylons() {
		return safe(values().portalRequirePylons);
	}

	public static boolean apothecaryEnabled() {
		return safe(values().apothecaryEnabled);
	}

	public static int apothecaryMaxPerStorage() {
		return safe(values().apothecaryMaxPerStorage);
	}

	public static int apothecaryInterval() {
		return safe(values().apothecaryInterval);
	}

	public static boolean apothecaryAutoFeed() {
		return safe(values().apothecaryAutoFeed);
	}

	public static boolean apothecaryConsumeWater() {
		return safe(values().apothecaryConsumeWater);
	}

	public static boolean terraPlateEnabled() {
		return safe(values().terraPlateEnabled);
	}

	public static int terraPlateMaxPerStorage() {
		return safe(values().terraPlateMaxPerStorage);
	}

	public static int terraPlateInterval() {
		return safe(values().terraPlateInterval);
	}

	public static int terraPlateCostPercent() {
		return safe(values().terraPlateCostPercent);
	}

	public static boolean terraPlateAutoFeed() {
		return safe(values().terraPlateAutoFeed);
	}

	public static boolean terraPlateRequireSpark() {
		return safe(values().terraPlateRequireSpark);
	}

	public static boolean portalEnabled() {
		return safe(values().portalEnabled);
	}

	public static int portalMaxPerStorage() {
		return safe(values().portalMaxPerStorage);
	}

	public static int portalInterval() {
		return safe(values().portalInterval);
	}

	public static int portalCostPerTrade() {
		return safe(values().portalCostPerTrade);
	}

	public static boolean breweryEnabled() {
		return safe(values().breweryEnabled);
	}

	public static int breweryMaxPerStorage() {
		return safe(values().breweryMaxPerStorage);
	}

	public static int breweryInterval() {
		return safe(values().breweryInterval);
	}

	public static int breweryCostPercent() {
		return safe(values().breweryCostPercent);
	}

	public static boolean linkEnabled() {
		return safe(values().linkEnabled);
	}

	public static int linkMaxPerStorage() {
		return safe(values().linkMaxPerStorage);
	}

	public static boolean storageEnabled() {
		return safe(values().storageEnabled);
	}

	public static int storageSlots(BotaniaStorageTier tier) {
		return safe(values().storageSlots.get(tier));
	}

	public static int storageUpgradeSlots(BotaniaStorageTier tier) {
		return safe(values().storageUpgradeSlots.get(tier));
	}

	public static boolean backpacksEnabled() {
		return safe(values().backpacksEnabled);
	}

	public static int backpackSlots(String tier) {
		return safe(values().backpackSlots.get(tier));
	}

	public static int backpackUpgradeSlots(String tier) {
		return safe(values().backpackUpgradeSlots.get(tier));
	}
}
