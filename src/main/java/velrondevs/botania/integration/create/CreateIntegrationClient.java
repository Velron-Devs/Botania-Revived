package velrondevs.botania.integration.create;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;

import dev.engine_room.flywheel.api.backend.BackendManager;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class CreateIntegrationClient {
	private CreateIntegrationClient() {}

	static void init(IEventBus modBus) {
		modBus.addListener(CreateIntegrationClient::registerRenderers);
		modBus.addListener(CreateIntegrationClient::clientSetup);
	}

	private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(CreateIntegration.MANA_MOTOR_BE.get(), ManaMotorRenderer::new);
	}

	private static void clientSetup(FMLClientSetupEvent event) {
		SimpleBlockEntityVisualizer.builder(CreateIntegration.MANA_MOTOR_BE.get())
				.factory(OrientedRotatingVisual.<ManaMotorBlockEntity>of(AllPartialModels.SHAFT_HALF))
				.skipVanillaRender(be -> BackendManager.isBackendOn())
				.apply();
	}
}
