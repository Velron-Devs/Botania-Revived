package velrondevs.botania.client.integration.ponder;

import net.createmod.catnip.api.client.gui.render.pip.GuiBlockEntityRenderState;
import net.createmod.catnip.api.client.gui.render.pip.GuiBlockModelRenderState;
import net.createmod.catnip.api.client.gui.render.pip.GuiFluidStateRenderState;
import net.createmod.catnip.impl.client.gui.element.pip.GuiBlockEntityRenderer;
import net.createmod.catnip.impl.client.gui.element.pip.GuiBlockModelRenderer;
import net.createmod.catnip.impl.client.gui.element.pip.GuiFluidStateRenderer;
import net.createmod.catnip.impl.neoforge.service.NeoForgeClientHooksHelper;
import net.createmod.ponder.api.client.PonderIndex;
import net.createmod.ponder.impl.client.gui.PonderSceneRenderState;
import net.createmod.ponder.impl.client.gui.PonderSceneRenderer;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.neoforged.neoforge.client.event.RegisterPictureInPictureRenderersEvent;

import java.util.function.Function;

public final class BotaniaPonderIntegration {
	private BotaniaPonderIntegration() {}

	public static void init() {
		PonderIndex.addPlugin(new BotaniaPonderPlugin());
	}

	public static void registerPictureInPictureRenderers(RegisterPictureInPictureRenderersEvent event) {
		register(event, PonderSceneRenderState.class, PonderSceneRenderer::new);
		register(event, GuiBlockModelRenderState.class, GuiBlockModelRenderer::new);
		register(event, GuiBlockEntityRenderState.class, GuiBlockEntityRenderer::new);
		register(event, GuiFluidStateRenderState.class, GuiFluidStateRenderer::new);
	}

	private static <T extends PictureInPictureRenderState> void register(RegisterPictureInPictureRenderersEvent event, Class<T> stateClass,
			Function<MultiBufferSource.BufferSource, PictureInPictureRenderer<T>> factory) {
		if (!NeoForgeClientHooksHelper.PIP_RENDERERS.containsKey(stateClass)) {
			event.register(stateClass, factory);
		}
	}
}
