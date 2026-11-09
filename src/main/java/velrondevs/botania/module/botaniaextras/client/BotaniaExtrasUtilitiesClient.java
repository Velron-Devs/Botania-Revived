package velrondevs.botania.module.botaniaextras.client;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import velrondevs.botania.api.BotaniaClientCapabilities;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasUtilities;
import velrondevs.botania.module.botaniaextras.block.LivingwoodFunnelBlockEntity;
import velrondevs.botania.module.botaniaextras.entity.ManasealCreeperRenderer;

public final class BotaniaExtrasUtilitiesClient {
	private BotaniaExtrasUtilitiesClient() {}

	public static void init(IEventBus modBus) {
		modBus.addListener(BotaniaExtrasUtilitiesClient::renderers);
		modBus.addListener(BotaniaExtrasUtilitiesClient::capabilities);
		ToolbeltClient.init();
	}

	private static void renderers(EntityRenderersEvent.RegisterRenderers e) {
		e.registerEntityRenderer(BotaniaExtrasUtilities.MANASEAL_CREEPER, ManasealCreeperRenderer::new);
	}

	private static void capabilities(RegisterCapabilitiesEvent e) {
		e.registerBlockEntity(BotaniaClientCapabilities.WAND_HUD, BotaniaExtrasUtilities.FUNNEL, (be, ctx) -> new LivingwoodFunnelBlockEntity.WandHud(be));
	}
}
