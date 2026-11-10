package velrondevs.botania.compat.sophisticated;

import velrondevs.botania.compat.sophisticated.storage.BotaniaStorageTier;
import velrondevs.botania.config.BotaniaConfigSpec;
import velrondevs.botania.xplat.BotaniaConfig;

public final class SophisticatedConfig {
	private SophisticatedConfig() {}

	private static BotaniaConfig.SophisticatedConfigAccess config() {
		BotaniaConfig.ConfigAccess common = BotaniaConfig.common();
		return (common != null ? common : BotaniaConfigSpec.commonAccess()).sophisticated();
	}

	public static int pileInfusionInterval() {
		return config().pileInfusionInterval();
	}

	public static int pileInfusionMaxPerInterval() {
		return config().pileInfusionMaxPerInterval();
	}

	public static boolean pileInfusionAutoFeed() {
		return config().pileInfusionAutoFeed();
	}

	public static boolean pileInfusionAutoOutput() {
		return config().pileInfusionAutoOutput();
	}

	public static boolean stationAutoFeed() {
		return config().stationAutoFeed();
	}

	public static int stationMaxPerInterval() {
		return config().stationMaxPerInterval();
	}

	public static boolean pileChargeItems() {
		return config().pileChargeItems();
	}

	public static int pileChargeInterval() {
		return config().pileChargeInterval();
	}

	public static int pileChargeRate() {
		return config().pileChargeRate();
	}

	public static int pileBaseCapacity() {
		return config().pileBaseCapacity();
	}

	public static double pileStackRatio() {
		return config().pileStackRatio();
	}

	public static int pileMaxPerStorage() {
		return config().pileMaxPerStorage();
	}

	public static boolean pileAbsorbFromItems() {
		return config().pileAbsorbFromItems();
	}

	public static int pileAbsorbInterval() {
		return config().pileAbsorbInterval();
	}

	public static int pileAbsorbAmount() {
		return config().pileAbsorbAmount();
	}

	public static boolean pileAbsorbFromSlot() {
		return config().pileAbsorbFromSlot();
	}

	public static boolean pilePullFromPlayer() {
		return config().pilePullFromPlayer();
	}

	public static boolean pileReceiveFromWorld() {
		return config().pileReceiveFromWorld();
	}

	public static boolean altarEnabled() {
		return config().altarEnabled();
	}

	public static int altarMaxPerStorage() {
		return config().altarMaxPerStorage();
	}

	public static int altarInterval() {
		return config().altarInterval();
	}

	public static int altarCostPercent() {
		return config().altarCostPercent();
	}

	public static boolean portalEnabled() {
		return config().portalEnabled();
	}

	public static int portalMaxPerStorage() {
		return config().portalMaxPerStorage();
	}

	public static int portalInterval() {
		return config().portalInterval();
	}

	public static int portalCostPerTrade() {
		return config().portalCostPerTrade();
	}

	public static boolean breweryEnabled() {
		return config().breweryEnabled();
	}

	public static int breweryMaxPerStorage() {
		return config().breweryMaxPerStorage();
	}

	public static int breweryInterval() {
		return config().breweryInterval();
	}

	public static int breweryCostPercent() {
		return config().breweryCostPercent();
	}

	public static boolean altarRequireWand() {
		return config().altarRequireWand();
	}

	public static boolean portalRequirePylons() {
		return config().portalRequirePylons();
	}

	public static boolean apothecaryEnabled() {
		return config().apothecaryEnabled();
	}

	public static int apothecaryMaxPerStorage() {
		return config().apothecaryMaxPerStorage();
	}

	public static int apothecaryInterval() {
		return config().apothecaryInterval();
	}

	public static boolean apothecaryAutoFeed() {
		return config().apothecaryAutoFeed();
	}

	public static boolean apothecaryConsumeWater() {
		return config().apothecaryConsumeWater();
	}

	public static boolean terraPlateEnabled() {
		return config().terraPlateEnabled();
	}

	public static int terraPlateMaxPerStorage() {
		return config().terraPlateMaxPerStorage();
	}

	public static int terraPlateInterval() {
		return config().terraPlateInterval();
	}

	public static int terraPlateCostPercent() {
		return config().terraPlateCostPercent();
	}

	public static boolean terraPlateAutoFeed() {
		return config().terraPlateAutoFeed();
	}

	public static boolean terraPlateRequireSpark() {
		return config().terraPlateRequireSpark();
	}

	public static boolean linkEnabled() {
		return config().linkEnabled();
	}

	public static int linkMaxPerStorage() {
		return config().linkMaxPerStorage();
	}

	public static boolean storageEnabled() {
		return config().storageEnabled();
	}

	public static int storageSlots(BotaniaStorageTier tier) {
		return config().storageSlots(tier.id());
	}

	public static int storageUpgradeSlots(BotaniaStorageTier tier) {
		return config().storageUpgradeSlots(tier.id());
	}

	public static boolean backpacksEnabled() {
		return config().backpacksEnabled();
	}

	public static int backpackSlots(String tier) {
		return config().backpackSlots(tier);
	}

	public static int backpackUpgradeSlots(String tier) {
		return config().backpackUpgradeSlots(tier);
	}
}
