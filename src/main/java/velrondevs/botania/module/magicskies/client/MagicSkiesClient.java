package velrondevs.botania.module.magicskies.client;

import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.module.magicskies.client.render.sky.MagicSkyRenderer;

public final class MagicSkiesClient {
	private MagicSkiesClient() {}

	public static void init() {
		try {
			NeoForge.EVENT_BUS.addListener(EventPriority.LOW, MagicSkyRenderer::onExtractLevelRenderState);
			NeoForge.EVENT_BUS.addListener(MagicSkiesClient::onAfterSky);
		} catch (Throwable t) {
			BotaniaAPI.LOGGER.error("Could not register the Magic Skies renderer", t);
		}
	}

	private static void onAfterSky(RenderLevelStageEvent.AfterSky event) {
		if (event.getLevelRenderState().skyRenderState.skybox != DimensionType.Skybox.END) {
			return;
		}
		MagicSkyRenderer.renderOverlay(event.getModelViewMatrix());
	}
}
