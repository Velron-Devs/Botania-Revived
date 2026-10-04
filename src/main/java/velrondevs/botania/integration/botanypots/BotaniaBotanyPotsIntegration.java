package velrondevs.botania.integration.botanypots;

import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

import velrondevs.botania.common.lib.ResourceLocationHelper;

public final class BotaniaBotanyPotsIntegration {
	private BotaniaBotanyPotsIntegration() {}

	public static void init(IEventBus modBus) {
		modBus.addListener(BotaniaBotanyPotsIntegration::register);
	}

	private static void register(RegisterEvent event) {
		event.register(Registries.RECIPE_SERIALIZER, ResourceLocationHelper.prefix("mana_soil"), () -> ManaSoil.SERIALIZER);
	}
}
