package velrondevs.botania.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.SkullModelBase;

import velrondevs.botania.client.render.block_entity.GaiaHeadBlockEntityRenderer;

public class GaiaHeadModel extends SkullModelBase {
	@Override
	public void setupAnim(float animationProgress, float yRot, float xRot) {
		var type = GaiaHeadBlockEntityRenderer.getViewType();
		var model = GaiaHeadBlockEntityRenderer.models.get(type);
		if (model != null) {
			model.setupAnim(animationProgress, yRot, xRot);
		}
	}

	@Override
	public void renderToBuffer(PoseStack ms, VertexConsumer buffer, int light, int overlay, int color) {
		var type = GaiaHeadBlockEntityRenderer.getViewType();
		var model = GaiaHeadBlockEntityRenderer.models.get(type);
		if (model != null) {
			model.renderToBuffer(ms, buffer, light, overlay, color);
		}
	}
}
