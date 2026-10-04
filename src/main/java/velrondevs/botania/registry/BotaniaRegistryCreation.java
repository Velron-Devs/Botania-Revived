package velrondevs.botania.registry;

import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import velrondevs.botania.api.BotaniaRegistries;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaRegistryCreation {
	public static void registerRegistry(NewRegistryEvent evt) {
		evt.create(new RegistryBuilder<>(BotaniaRegistries.BREWS)
				.defaultKey(prefix("fallback"))
				.sync(false));
	}
}
