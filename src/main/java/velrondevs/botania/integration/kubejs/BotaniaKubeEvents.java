package velrondevs.botania.integration.kubejs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface BotaniaKubeEvents {
	EventGroup GROUP = EventGroup.of("BotaniaEvents");

	EventHandler REGISTRY = GROUP.startup("registry", () -> BotaniaRegistryKubeEvent.class);
	EventHandler SERVER = GROUP.server("server", () -> BotaniaMachineRecipesKubeEvent.class);
}
