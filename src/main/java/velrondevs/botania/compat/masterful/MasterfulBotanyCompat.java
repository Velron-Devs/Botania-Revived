package velrondevs.botania.compat.masterful;

import net.neoforged.fml.ModList;

import velrondevs.botania.xplat.BotaniaConfig;

public final class MasterfulBotanyCompat {
	public static final String MOD_ID = "masterfulbotany";
	public static final String ALFSTEEL_INGOT = MOD_ID + ":alfsteel_ingot";
	public static final double DEFAULT_ALFSTEEL_SPREADER_SPEED_MULTIPLIER = 4.0;
	public static final double MAX_ALFSTEEL_SPREADER_VELOCITY_FACTOR = 4.0;

	private MasterfulBotanyCompat() {}

	public static boolean isLoaded() {
		ModList list = ModList.get();
		return list != null && list.isLoaded(MOD_ID);
	}

	public static double alfsteelSpreaderSpeedMultiplier() {
		var common = BotaniaConfig.common();
		double value = common == null ? DEFAULT_ALFSTEEL_SPREADER_SPEED_MULTIPLIER : common.alfsteelSpreaderSpeedMultiplier();
		if (Double.isNaN(value) || value < 1.0) {
			return DEFAULT_ALFSTEEL_SPREADER_SPEED_MULTIPLIER;
		}
		return value;
	}

	public static double alfsteelSpreaderVelocityFactor() {
		return Math.min(Math.sqrt(alfsteelSpreaderSpeedMultiplier()), MAX_ALFSTEEL_SPREADER_VELOCITY_FACTOR);
	}

	public static double alfsteelSpreaderManaFactor() {
		return alfsteelSpreaderSpeedMultiplier() / alfsteelSpreaderVelocityFactor();
	}
}
