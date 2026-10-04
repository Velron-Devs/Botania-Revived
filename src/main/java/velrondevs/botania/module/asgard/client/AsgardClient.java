package velrondevs.botania.module.asgard.client;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import velrondevs.botania.api.BotaniaClientCapabilities;
import velrondevs.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import velrondevs.botania.client.render.block_entity.SpecialFlowerBlockEntityRenderer;
import velrondevs.botania.module.asgard.AsgardFlowers;

public final class AsgardClient {
	private AsgardClient() {}

	public static void init(IEventBus modBus) {
		modBus.addListener(AsgardClient::renderers);
		modBus.addListener(AsgardClient::capabilities);
	}

	private static void renderers(EntityRenderersEvent.RegisterRenderers e) {
		e.registerBlockEntityRenderer(AsgardFlowers.ASGARDANDELION, SpecialFlowerBlockEntityRenderer::new);
	}

	private static void capabilities(RegisterCapabilitiesEvent e) {
		e.registerBlockEntity(BotaniaClientCapabilities.WAND_HUD, AsgardFlowers.ASGARDANDELION, (be, ctx) -> new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>(be));
	}
}
