package velrondevs.botania.compat.sophisticated;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import velrondevs.botania.compat.sophisticated.backpack.BotaniaBackpackTier;
import velrondevs.botania.compat.sophisticated.storage.BotaniaStorageTier;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class SophisticatedConfig {
	private static final int MAX_INVENTORY_SLOTS = 1000;
	private static final ModConfigSpec.IntValue PILE_BASE_CAPACITY;
	private static final ModConfigSpec.DoubleValue PILE_STACK_RATIO;
	private static final ModConfigSpec.IntValue PILE_MAX_PER_STORAGE;
	private static final ModConfigSpec.BooleanValue PILE_ABSORB_FROM_ITEMS;
	private static final ModConfigSpec.IntValue PILE_ABSORB_INTERVAL;
	private static final ModConfigSpec.IntValue PILE_ABSORB_AMOUNT;
	private static final ModConfigSpec.BooleanValue PILE_ABSORB_FROM_SLOT;
	private static final ModConfigSpec.BooleanValue PILE_PULL_FROM_PLAYER;
	private static final ModConfigSpec.BooleanValue PILE_RECEIVE_FROM_WORLD;
	private static final ModConfigSpec.BooleanValue PILE_CHARGE_ITEMS;
	private static final ModConfigSpec.IntValue PILE_CHARGE_INTERVAL;
	private static final ModConfigSpec.IntValue PILE_CHARGE_RATE;
	private static final ModConfigSpec.IntValue PILE_INFUSION_INTERVAL;
	private static final ModConfigSpec.IntValue PILE_INFUSION_MAX;
	private static final ModConfigSpec.BooleanValue PILE_INFUSION_AUTO_FEED;
	private static final ModConfigSpec.BooleanValue PILE_INFUSION_AUTO_OUTPUT;
	private static final ModConfigSpec.BooleanValue LINK_ENABLED;
	private static final ModConfigSpec.IntValue LINK_MAX_PER_STORAGE;
	private static final ModConfigSpec.BooleanValue BACKPACKS_ENABLED;
	private static final Map<String, ModConfigSpec.IntValue> BACKPACK_SLOTS = new HashMap<>();
	private static final Map<String, ModConfigSpec.IntValue> BACKPACK_UPGRADE_SLOTS = new HashMap<>();
	private static final ModConfigSpec.BooleanValue STORAGE_ENABLED;
	private static final Map<BotaniaStorageTier, ModConfigSpec.IntValue> STORAGE_SLOTS = new EnumMap<>(BotaniaStorageTier.class);
	private static final Map<BotaniaStorageTier, ModConfigSpec.IntValue> STORAGE_UPGRADE_SLOTS = new EnumMap<>(BotaniaStorageTier.class);
	private static final ModConfigSpec.BooleanValue STATION_AUTO_FEED;
	private static final ModConfigSpec.IntValue STATION_MAX_PER_INTERVAL;
	private static final ModConfigSpec.BooleanValue ALTAR_ENABLED;
	private static final ModConfigSpec.IntValue ALTAR_MAX_PER_STORAGE;
	private static final ModConfigSpec.IntValue ALTAR_INTERVAL;
	private static final ModConfigSpec.IntValue ALTAR_COST_PERCENT;
	private static final ModConfigSpec.BooleanValue PORTAL_ENABLED;
	private static final ModConfigSpec.IntValue PORTAL_MAX_PER_STORAGE;
	private static final ModConfigSpec.IntValue PORTAL_INTERVAL;
	private static final ModConfigSpec.IntValue PORTAL_COST_PER_TRADE;
	private static final ModConfigSpec.BooleanValue BREWERY_ENABLED;
	private static final ModConfigSpec.IntValue BREWERY_MAX_PER_STORAGE;
	private static final ModConfigSpec.IntValue BREWERY_INTERVAL;
	private static final ModConfigSpec.IntValue BREWERY_COST_PERCENT;
	private static final ModConfigSpec SPEC;

	static {
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		builder.push("mana_pile");
		PILE_BASE_CAPACITY = builder.defineInRange("base_capacity", 1000000, 1, Integer.MAX_VALUE);
		PILE_STACK_RATIO = builder.defineInRange("stack_upgrade_ratio", 0.5, 0.0, 1000.0);
		PILE_MAX_PER_STORAGE = builder.defineInRange("max_per_storage", 1, 1, 64);
		PILE_ABSORB_FROM_ITEMS = builder.define("absorb_from_items", true);
		PILE_ABSORB_INTERVAL = builder.defineInRange("absorb_interval_ticks", 20, 1, 1200);
		PILE_ABSORB_AMOUNT = builder.defineInRange("absorb_amount_per_interval", 10000, 1, Integer.MAX_VALUE);
		PILE_ABSORB_FROM_SLOT = builder.define("absorb_from_slot", true);
		PILE_PULL_FROM_PLAYER = builder.define("pull_from_player_items", true);
		PILE_RECEIVE_FROM_WORLD = builder.define("receive_from_world", true);
		PILE_CHARGE_ITEMS = builder.define("charge_items", true);
		PILE_CHARGE_INTERVAL = builder.defineInRange("charge_interval_ticks", 20, 1, 1200);
		PILE_CHARGE_RATE = builder.defineInRange("charge_rate", 10000, 1, Integer.MAX_VALUE);
		PILE_INFUSION_INTERVAL = builder.defineInRange("infusion_interval_ticks", 20, 1, 1200);
		PILE_INFUSION_MAX = builder.defineInRange("infusion_max_per_interval", 4, 1, 64);
		PILE_INFUSION_AUTO_FEED = builder.define("infusion_auto_feed", true);
		PILE_INFUSION_AUTO_OUTPUT = builder.define("infusion_auto_output", true);
		builder.pop();
		builder.push("mana_link");
		LINK_ENABLED = builder.define("enabled", true);
		LINK_MAX_PER_STORAGE = builder.defineInRange("max_per_storage", 1, 1, 64);
		builder.pop();
		builder.push("backpacks");
		BACKPACKS_ENABLED = builder.define("enabled", true);
		for (BotaniaBackpackTier tier : BotaniaBackpackTier.values()) {
			defineBackpack(builder, tier.material(), tier.defaultSlots(), tier.defaultUpgradeSlots());
		}
		builder.pop();
		builder.push("storage");
		STORAGE_ENABLED = builder.define("enabled", true);
		for (BotaniaStorageTier tier : BotaniaStorageTier.values()) {
			builder.push(tier.id());
			STORAGE_SLOTS.put(tier, builder.defineInRange("inventory_slots", tier.defaultInventorySlots(), 1, MAX_INVENTORY_SLOTS));
			STORAGE_UPGRADE_SLOTS.put(tier, builder.defineInRange("upgrade_slots", tier.defaultUpgradeSlots(), 0, 10));
			builder.pop();
		}
		builder.pop();
		builder.push("stations");
		STATION_AUTO_FEED = builder.define("auto_feed", true);
		STATION_MAX_PER_INTERVAL = builder.defineInRange("max_per_interval", 1, 1, 64);
		builder.pop();
		builder.push("runic_altar");
		ALTAR_ENABLED = builder.define("enabled", true);
		ALTAR_MAX_PER_STORAGE = builder.defineInRange("max_per_storage", 1, 1, 64);
		ALTAR_INTERVAL = builder.defineInRange("interval_ticks", 20, 1, 1200);
		ALTAR_COST_PERCENT = builder.defineInRange("cost_percent", 100, 0, 10000);
		builder.pop();
		builder.push("elven_portal");
		PORTAL_ENABLED = builder.define("enabled", true);
		PORTAL_MAX_PER_STORAGE = builder.defineInRange("max_per_storage", 1, 1, 64);
		PORTAL_INTERVAL = builder.defineInRange("interval_ticks", 20, 1, 1200);
		PORTAL_COST_PER_TRADE = builder.defineInRange("cost_per_trade", 500, 0, 1000000);
		builder.pop();
		builder.push("brewery");
		BREWERY_ENABLED = builder.define("enabled", true);
		BREWERY_MAX_PER_STORAGE = builder.defineInRange("max_per_storage", 1, 1, 64);
		BREWERY_INTERVAL = builder.defineInRange("interval_ticks", 20, 1, 1200);
		BREWERY_COST_PERCENT = builder.defineInRange("cost_percent", 100, 0, 10000);
		builder.pop();
		SPEC = builder.build();
	}

	private SophisticatedConfig() {}

	private static void defineBackpack(ModConfigSpec.Builder builder, String tier, int slots, int upgradeSlots) {
		builder.push(tier);
		BACKPACK_SLOTS.put(tier, builder.defineInRange("inventory_slots", slots, 1, MAX_INVENTORY_SLOTS));
		BACKPACK_UPGRADE_SLOTS.put(tier, builder.defineInRange("upgrade_slots", upgradeSlots, 0, 10));
		builder.pop();
	}

	static void register(ModContainer container) {
		container.registerConfig(ModConfig.Type.COMMON, SPEC, "botania-sophisticated-common.toml");
	}

	private static <T> T safe(ModConfigSpec.ConfigValue<T> value) {
		try {
			return value.get();
		} catch (IllegalStateException e) {
			return value.getDefault();
		}
	}

	public static int pileInfusionInterval() {
		return safe(PILE_INFUSION_INTERVAL);
	}

	public static int pileInfusionMaxPerInterval() {
		return safe(PILE_INFUSION_MAX);
	}

	public static boolean pileInfusionAutoFeed() {
		return safe(PILE_INFUSION_AUTO_FEED);
	}

	public static boolean pileInfusionAutoOutput() {
		return safe(PILE_INFUSION_AUTO_OUTPUT);
	}

	public static boolean stationAutoFeed() {
		return safe(STATION_AUTO_FEED);
	}

	public static int stationMaxPerInterval() {
		return safe(STATION_MAX_PER_INTERVAL);
	}

	public static boolean pileChargeItems() {
		return safe(PILE_CHARGE_ITEMS);
	}

	public static int pileChargeInterval() {
		return safe(PILE_CHARGE_INTERVAL);
	}

	public static int pileChargeRate() {
		return safe(PILE_CHARGE_RATE);
	}

	public static int pileBaseCapacity() {
		return safe(PILE_BASE_CAPACITY);
	}

	public static double pileStackRatio() {
		return safe(PILE_STACK_RATIO);
	}

	public static int pileMaxPerStorage() {
		return safe(PILE_MAX_PER_STORAGE);
	}

	public static boolean pileAbsorbFromItems() {
		return safe(PILE_ABSORB_FROM_ITEMS);
	}

	public static int pileAbsorbInterval() {
		return safe(PILE_ABSORB_INTERVAL);
	}

	public static int pileAbsorbAmount() {
		return safe(PILE_ABSORB_AMOUNT);
	}

	public static boolean pileAbsorbFromSlot() {
		return safe(PILE_ABSORB_FROM_SLOT);
	}

	public static boolean pilePullFromPlayer() {
		return safe(PILE_PULL_FROM_PLAYER);
	}

	public static boolean pileReceiveFromWorld() {
		return safe(PILE_RECEIVE_FROM_WORLD);
	}

	public static boolean altarEnabled() {
		return safe(ALTAR_ENABLED);
	}

	public static int altarMaxPerStorage() {
		return safe(ALTAR_MAX_PER_STORAGE);
	}

	public static int altarInterval() {
		return safe(ALTAR_INTERVAL);
	}

	public static int altarCostPercent() {
		return safe(ALTAR_COST_PERCENT);
	}

	public static boolean portalEnabled() {
		return safe(PORTAL_ENABLED);
	}

	public static int portalMaxPerStorage() {
		return safe(PORTAL_MAX_PER_STORAGE);
	}

	public static int portalInterval() {
		return safe(PORTAL_INTERVAL);
	}

	public static int portalCostPerTrade() {
		return safe(PORTAL_COST_PER_TRADE);
	}

	public static boolean breweryEnabled() {
		return safe(BREWERY_ENABLED);
	}

	public static int breweryMaxPerStorage() {
		return safe(BREWERY_MAX_PER_STORAGE);
	}

	public static int breweryInterval() {
		return safe(BREWERY_INTERVAL);
	}

	public static int breweryCostPercent() {
		return safe(BREWERY_COST_PERCENT);
	}

	public static boolean linkEnabled() {
		return safe(LINK_ENABLED);
	}

	public static int linkMaxPerStorage() {
		return safe(LINK_MAX_PER_STORAGE);
	}

	public static boolean storageEnabled() {
		return safe(STORAGE_ENABLED);
	}

	public static int storageSlots(BotaniaStorageTier tier) {
		return safe(STORAGE_SLOTS.get(tier));
	}

	public static int storageUpgradeSlots(BotaniaStorageTier tier) {
		return safe(STORAGE_UPGRADE_SLOTS.get(tier));
	}

	public static boolean backpacksEnabled() {
		return safe(BACKPACKS_ENABLED);
	}

	public static int backpackSlots(String tier) {
		return safe(BACKPACK_SLOTS.get(tier));
	}

	public static int backpackUpgradeSlots(String tier) {
		return safe(BACKPACK_UPGRADE_SLOTS.get(tier));
	}
}
