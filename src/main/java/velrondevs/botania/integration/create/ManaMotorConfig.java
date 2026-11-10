package velrondevs.botania.integration.create;

import velrondevs.botania.config.BotaniaConfigSpec;
import velrondevs.botania.xplat.BotaniaConfig;

public final class ManaMotorConfig {
	private ManaMotorConfig() {}

	private static BotaniaConfig.ManaMotorConfigAccess values() {
		return BotaniaConfigSpec.manaMotor();
	}

	public static boolean enabled() {
		return values().enabled();
	}

	public static int manaCapacity() {
		return values().manaCapacity();
	}

	public static int defaultRpm() {
		return values().defaultRpm();
	}

	public static int minRpm() {
		return Math.min(values().minRpm(), maxRpm());
	}

	public static int maxRpm() {
		return values().maxRpm();
	}

	public static double stressPerRpm() {
		return values().stressPerRpm();
	}

	public static double stressUnit() {
		return values().stressUnit();
	}

	public static double manaPerStressUnit() {
		return values().manaPerStressUnit();
	}

	public static double baseManaPerTick() {
		return values().baseManaPerTick();
	}
}
