package velrondevs.botania.compat.sophisticated.storage;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.p3pp3rf1y.sophisticatedstorage.Config;
import net.p3pp3rf1y.sophisticatedstorage.block.TankTier;

import velrondevs.botania.config.BotaniaConfigSpec;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public final class BotaniaTankConfigs {
	private static final Map<String, Config.Server.TankConfig> CONFIGS = new HashMap<>();

	private BotaniaTankConfigs() {}

	public static void define(ModConfigSpec.Builder builder) {
		for (BotaniaStorageTier tier : BotaniaStorageTier.values()) {
			CONFIGS.put(tier.id(), new Config.Server.TankConfig(builder, tier.english() + " Tank", tier.defaultTankCapacity(), tier.defaultUpgradeSlots()));
		}
	}

	public static Supplier<Config.Server.TankConfig> supplier(String id) {
		return () -> {
			if (CONFIGS.isEmpty()) {
				BotaniaConfigSpec.sophisticated();
			}
			return CONFIGS.get(id);
		};
	}

	public static TankTier tankTier(BotaniaStorageTier tier) {
		return TankTier.valueOf(tier.id().toUpperCase(Locale.ROOT));
	}
}
