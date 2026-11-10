package velrondevs.botania.compat.sophisticated.backpack;

import com.electronwill.nightconfig.toml.TomlParser;

import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.data.loading.DatagenModLoader;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.common.lib.LibMisc;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

public final class BackpackSwitch {
	private static Boolean state;

	private BackpackSwitch() {}

	public static synchronized boolean enabled() {
		if (state == null) {
			state = read();
		}
		return state;
	}

	private static boolean read() {
		if (DatagenModLoader.isRunningDataGen()) {
			return true;
		}
		Path file = FMLPaths.CONFIGDIR.get().resolve(LibMisc.MOD_ID + "-common.toml");
		if (!Files.isRegularFile(file)) {
			return true;
		}
		try (Reader reader = Files.newBufferedReader(file)) {
			Object value = new TomlParser().parse(reader).get("integrations.sophisticated.backpacks.enabled");
			return !(value instanceof Boolean b) || b;
		} catch (Exception e) {
			BotaniaAPI.LOGGER.warn("Could not read backpack settings from {}, using defaults", file, e);
			return true;
		}
	}
}
