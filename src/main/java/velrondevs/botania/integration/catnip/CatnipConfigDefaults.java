package velrondevs.botania.integration.catnip;

import net.createmod.catnip.api.config.definition.CatnipConfig;
import net.createmod.catnip.api.config.definition.ConfigValue;
import net.createmod.catnip.impl.config.CatnipModConfig;
import net.createmod.ponder.impl.client.PonderClientConfig;

import velrondevs.botania.api.BotaniaAPI;

import java.lang.reflect.Field;

public final class CatnipConfigDefaults {
	private static boolean done;

	private CatnipConfigDefaults() {}

	public static synchronized void init() {
		if (done) {
			return;
		}
		done = true;
		try {
			Field valueField = ConfigValue.class.getDeclaredField("value");
			valueField.setAccessible(true);
			apply(PonderClientConfig.INSTANCE, valueField);
			apply(CatnipModConfig.INSTANCE, valueField);
		} catch (ReflectiveOperationException | RuntimeException | LinkageError e) {
			BotaniaAPI.LOGGER.warn("Could not initialize the default Catnip config values", e);
		}
	}

	private static void apply(CatnipConfig config, Field valueField) throws IllegalAccessException {
		for (ConfigValue<?> value : config.values()) {
			if (valueField.get(value) == null) {
				valueField.set(value, value.defaultValue);
			}
		}
		for (CatnipConfig nested : config.nested().sequencedValues()) {
			apply(nested, valueField);
		}
	}
}
