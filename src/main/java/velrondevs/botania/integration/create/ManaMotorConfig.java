package velrondevs.botania.integration.create;

import velrondevs.botania.config.BotaniaConfigSpec;

public final class ManaMotorConfig {
	private ManaMotorConfig() {}

	private static BotaniaConfigSpec.ManaMotorValues values() {
		return BotaniaConfigSpec.manaMotor();
	}

	public static boolean enabled() {
		return BotaniaConfigSpec.get(values().enabled);
	}

	public static int manaCapacity() {
		return BotaniaConfigSpec.get(values().manaCapacity);
	}

	public static int defaultRpm() {
		return BotaniaConfigSpec.get(values().defaultRpm);
	}

	public static int minRpm() {
		return Math.min(BotaniaConfigSpec.get(values().minRpm), maxRpm());
	}

	public static int maxRpm() {
		return BotaniaConfigSpec.get(values().maxRpm);
	}

	public static double stressPerRpm() {
		return BotaniaConfigSpec.get(values().stressPerRpm);
	}

	public static double stressUnit() {
		return BotaniaConfigSpec.get(values().stressUnit);
	}

	public static double manaPerStressUnit() {
		return BotaniaConfigSpec.get(values().manaPerStressUnit);
	}

	public static double baseManaPerTick() {
		return BotaniaConfigSpec.get(values().baseManaPerTick);
	}
}
