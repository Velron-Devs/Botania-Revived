package velrondevs.botania.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public interface PylonModel {
	void renderRing(PoseStack ms, VertexConsumer buffer, int light, int overlay);

	void renderCrystal(PoseStack ms, VertexConsumer buffer, int light, int overlay);
}
