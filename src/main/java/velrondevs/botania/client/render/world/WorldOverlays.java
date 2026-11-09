package velrondevs.botania.client.render.world;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;

import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.client.core.helper.DeferredBufferSource;
import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.client.fx.BoltRenderer;
import velrondevs.botania.common.item.AssemblyHaloItem;

public final class WorldOverlays {
	public static void onSubmitCustomGeometry(SubmitCustomGeometryEvent event) {
		Level level = Minecraft.getInstance().level;
		if (level == null) {
			return;
		}
		Vec3 camera = event.getLevelRenderState().cameraRenderState.pos;
		PoseStack ms = event.getPoseStack();
		MultiBufferSource buffers = new DeferredBufferSource(event.getSubmitNodeCollector());
		renderWorldLast(camera, ClientTickHandler.partialTicks, ms, buffers, level);
	}

	public static void renderWorldLast(Vec3 camera, float tickDelta, PoseStack matrix, MultiBufferSource buffers, Level level) {
		BoltRenderer.onWorldRenderLast(camera, tickDelta, matrix, buffers);
		AssemblyHaloItem.Rendering.onRenderWorldLast(camera, tickDelta, matrix, buffers);
		BoundBlockRenderer.onWorldRenderLast(camera, matrix, buffers, level);
		AstrolabePreviewHandler.onWorldRenderLast(camera, matrix, buffers, level);
		RenderHelper.onWorldRenderLast();
	}

	private WorldOverlays() {}
}
