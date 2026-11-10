package velrondevs.botania.integration.thaumaturge;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.config.BotaniaConfigSpec;

import java.util.Locale;

public final class NodeBoostApi {
	public static final String TOOLTIP_KEY = "botania.thaumaturge.node_boost";
	public static final int HUD_COLOR = 0xC48CFF;

	public enum Target {
		GENERATING_FLOWER,
		FUNCTIONAL_FLOWER,
		SPREADER,
		BREWERY,
		ELVEN_PORTAL,
		RUNIC_ALTAR,
		MANA_ENCHANTER,
		SPARK
	}

	public interface Boosted {
		double getNodeBoostMultiplier();
	}

	@FunctionalInterface
	public interface NodeLocator {
		boolean hasNodeWithin(Level level, BlockPos pos, int range);
	}

	@Nullable
	private static volatile NodeLocator locator;

	private NodeBoostApi() {}

	public static void setLocator(@Nullable NodeLocator newLocator) {
		locator = newLocator;
	}

	public static boolean isAvailable() {
		return locator != null;
	}

	public static double multiplier(@Nullable Level level, @Nullable BlockPos pos, @Nullable Target target) {
		NodeLocator current = locator;
		if (current == null || level == null || pos == null || target == null) {
			return 1.0;
		}
		BotaniaConfigSpec.ThaumaturgeNodeBoostValues config = BotaniaConfigSpec.thaumaturgeNodeBoost();
		if (!BotaniaConfigSpec.get(config.enabled) || !affects(config, target)) {
			return 1.0;
		}
		double multiplier = BotaniaConfigSpec.get(config.speedMultiplier);
		if (!(multiplier > 1.0)) {
			return 1.0;
		}
		try {
			return current.hasNodeWithin(level, pos, BotaniaConfigSpec.get(config.range)) ? multiplier : 1.0;
		} catch (RuntimeException | LinkageError e) {
			locator = null;
			BotaniaAPI.LOGGER.error("Thaumaturge aura node lookup failed, the aura node boost is disabled for this session", e);
			return 1.0;
		}
	}

	public static int maxExtraTicksPerTick() {
		return Math.max(0, BotaniaConfigSpec.get(BotaniaConfigSpec.thaumaturgeNodeBoost().maxExtraTicksPerTick));
	}

	private static boolean affects(BotaniaConfigSpec.ThaumaturgeNodeBoostValues config, Target target) {
		return switch (target) {
			case GENERATING_FLOWER -> BotaniaConfigSpec.get(config.affectGeneratingFlowers);
			case FUNCTIONAL_FLOWER -> BotaniaConfigSpec.get(config.affectFunctionalFlowers);
			case SPREADER -> BotaniaConfigSpec.get(config.affectSpreaders);
			case BREWERY -> BotaniaConfigSpec.get(config.affectBrewery);
			case ELVEN_PORTAL -> BotaniaConfigSpec.get(config.affectElvenPortal);
			case RUNIC_ALTAR -> BotaniaConfigSpec.get(config.affectRunicAltar);
			case MANA_ENCHANTER -> BotaniaConfigSpec.get(config.affectManaEnchanter);
			case SPARK -> BotaniaConfigSpec.get(config.affectSparks);
		};
	}

	public static int scale(int value, double multiplier) {
		if (multiplier <= 1.0) {
			return value;
		}
		return (int) Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, Math.round(value * multiplier)));
	}

	public static String format(double multiplier) {
		if (multiplier == Math.rint(multiplier)) {
			return Long.toString((long) multiplier);
		}
		return String.format(Locale.ROOT, "%.1f", multiplier);
	}

	public static Component tooltip(double multiplier) {
		return Component.translatable(TOOLTIP_KEY, format(multiplier));
	}
}
