package velrondevs.botania.module.magicskies.client.render.sky.layers;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import velrondevs.botania.client.render.world.SkyDrawHelper;
import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.module.magicskies.client.lib.SkyTextures;
import velrondevs.botania.module.magicskies.client.render.sky.SkyLayer;
import velrondevs.botania.module.magicskies.client.render.sky.SkyRenderContext;

public final class AuroraRayLayer implements SkyLayer {
	private static final int ANGLES = 90;
	private static final float ANGLE_STEP = 360F / ANGLES;
	private static final float U_PER_DEGREE = 1F / 360F;
	private static final float BAND_HEIGHT = 2F;

	@Override
	public void render(SkyRenderContext ctx) {
		PoseStack ms = ctx.poseStack();
		var palette = ctx.palette();

		float baseIntensity = ctx.baseIntensity();
		float alpha = Math.max(0F, baseIntensity - 0.3F);

		float scale = 20F;
		ms.pushPose();
		ms.translate(0, -1, 0);
		ms.mulPose(VecHelper.rotateX(220));
		Vector4f color = new Vector4f(1F, 1F, 1F, alpha);

		double fuzzPer = Math.PI * 10 / ANGLES;
		float rotSpeed = palette.raySpeed();
		float rotSpeedMod = 0.4F;
		float animTime = ctx.animTicks();

		for (int band = 0; band < 3; band++) {
			float baseAngle = rotSpeed * rotSpeedMod * animTime;
			ms.mulPose(VecHelper.rotateY(animTime * 0.25F * rotSpeed * rotSpeedMod));

			drawFuzzedRing(ctx.currentModelView(), new Vector4f(color), scale, baseAngle, fuzzPer);

			switch (band) {
				case 0 -> {
					ms.mulPose(VecHelper.rotateX(20));
					float[] c = palette.rayColorA();
					color.set(c[0], c[1], c[2], alpha);
					fuzzPer = Math.PI * 14 / ANGLES;
					rotSpeed = 0.2F * palette.raySpeed();
				}
				case 1 -> {
					ms.mulPose(VecHelper.rotateX(50));
					float[] c = palette.rayColorB();
					color.set(c[0], c[1], c[2], alpha);
					fuzzPer = Math.PI * 6 / ANGLES;
					rotSpeed = 2F * palette.raySpeed();
				}
				default -> {}
			}
		}
		ms.popPose();
	}

	private static void drawFuzzedRing(Matrix4f modelView, Vector4f color, float scale, float baseAngle, double fuzzPer) {
		BufferBuilder buffer = SkyDrawHelper.begin(DefaultVertexFormat.POSITION_TEX);

		for (int i = 0; i < ANGLES; i++) {
			int j = (i % 2 == 0) ? i - 1 : i;

			float ang = j * ANGLE_STEP + baseAngle;
			float xp = (float) Math.cos(ang * Math.PI / 180F) * scale;
			float zp = (float) Math.sin(ang * Math.PI / 180F) * scale;
			float yo = (float) Math.sin(fuzzPer * j);
			float u = ang * U_PER_DEGREE;

			if (i % 2 == 0) {
				buffer.addVertex(xp, yo + BAND_HEIGHT, zp).setUv(u, 1F);
				buffer.addVertex(xp, yo, zp).setUv(u, 0F);
			} else {
				buffer.addVertex(xp, yo, zp).setUv(u, 0F);
				buffer.addVertex(xp, yo + BAND_HEIGHT, zp).setUv(u, 1F);
			}
		}
		SkyDrawHelper.drawMesh(SkyDrawHelper.TEXTURED_ADDITIVE, buffer.buildOrThrow(), modelView, color, SkyTextures.SKYBOX);
	}
}
