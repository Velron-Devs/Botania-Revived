package velrondevs.botania.integration.kubejs;

import dev.latvian.mods.kubejs.script.ScriptType;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.event.BotaniaEvents;

public final class BotaniaKubeJSIntegration {
	private BotaniaKubeJSIntegration() {}

	public static void init(IEventBus modBus) {
		modBus.addListener(BotaniaKubeJSIntegration::registerRunes);
		NeoForge.EVENT_BUS.addListener(BotaniaKubeJSIntegration::registerMachineRecipes);
	}

	private static void registerRunes(BotaniaEvents.RegisterRunes event) {
		if (!BotaniaKubeEvents.REGISTRY.hasListeners()) {
			return;
		}
		try {
			BotaniaKubeEvents.REGISTRY.post(ScriptType.STARTUP, new BotaniaRegistryKubeEvent(event));
		} catch (Throwable t) {
			BotaniaAPI.LOGGER.error("Error posting BotaniaEvents.registry", t);
		}
	}

	private static void registerMachineRecipes(BotaniaEvents.RegisterMachineRecipes event) {
		if (!BotaniaKubeEvents.SERVER.hasListeners()) {
			return;
		}
		try {
			var kubeEvent = new BotaniaMachineRecipesKubeEvent(event);
			BotaniaKubeEvents.SERVER.post(ScriptType.SERVER, kubeEvent);
			kubeEvent.register();
			ScriptType.SERVER.console.info("[Botania] Added " + kubeEvent.getAdded() + " machine recipes from scripts");
		} catch (Throwable t) {
			BotaniaAPI.LOGGER.error("Error posting BotaniaEvents.server", t);
		}
	}
}
