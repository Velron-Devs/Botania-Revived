package velrondevs.botania.module.magicskies.client.render.sky.layers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.GameRenderer;

import org.joml.Matrix4f;

import velrondevs.botania.client.core.handler.ClientTickHandler;
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
		Tesselator tessellator = Tesselator.getInstance();
		var palette = ctx.palette();

		float baseIntensity = ctx.baseIntensity();
		float alpha = Math.max(0F, baseIntensity - 0.3F);

		RenderSystem.setShaderTexture(0, SkyTextures.SKYBOX);

		float scale = 20F;
		ms.pushPose();
		RenderSystem.blendFuncSeparate(770, 1, 1, 0);
		ms.translate(0, -1, 0);
		ms.mulPose(VecHelper.rotateX(220));
		RenderSystem.setShaderColor(1F, 1F, 1F, alpha);

		double fuzzPer = Math.PI * 10 / ANGLES;
		float rotSpeed = palette.raySpeed();
		float rotSpeedMod = 0.4F;
		float animTime = ClientTickHandler.total();

		for (int band = 0; band < 3; band++) {
			float baseAngle = rotSpeed * rotSpeedMod * animTime;
			ms.mulPose(VecHelper.rotateY(animTime * 0.25F * rotSpeed * rotSpeedMod));

			drawFuzzedRing(ms, tessellator, scale, baseAngle, fuzzPer);

			switch (band) {
				case 0 -> {
					ms.mulPose(VecHelper.rotateX(20));
					float[] c = palette.rayColorA();
					RenderSystem.setShaderColor(c[0], c[1], c[2], alpha);
					fuzzPer = Math.PI * 14 / ANGLES;
					rotSpeed = 0.2F * palette.raySpeed();
				}
				case 1 -> {
					ms.mulPose(VecHelper.rotateX(50));
					float[] c = palette.rayColorB();
					RenderSystem.setShaderColor(c[0], c[1], c[2], alpha);
					fuzzPer = Math.PI * 6 / ANGLES;
					rotSpeed = 2F * palette.raySpeed();
				}
				default -> {}
			}
		}
		ms.popPose();
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
	}

	private static void drawFuzzedRing(PoseStack ms, Tesselator tessellator, float scale, float baseAngle, double fuzzPer) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		Matrix4f mat = ms.last().pose();
		var buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

		for (int i = 0; i < ANGLES; i++) {
			int j = (i % 2 == 0) ? i - 1 : i;

			float ang = j * ANGLE_STEP + baseAngle;
			float xp = (float) Math.cos(ang * Math.PI / 180F) * scale;
			float zp = (float) Math.sin(ang * Math.PI / 180F) * scale;
			float yo = (float) Math.sin(fuzzPer * j);
			float u = ang * U_PER_DEGREE;

			if (i % 2 == 0) {
				buffer.addVertex(mat, xp, yo + BAND_HEIGHT, zp).setUv(u, 1F);
				buffer.addVertex(mat, xp, yo, zp).setUv(u, 0F);
			} else {
				buffer.addVertex(mat, xp, yo, zp).setUv(u, 0F);
				buffer.addVertex(mat, xp, yo + BAND_HEIGHT, zp).setUv(u, 1F);
			}
		}
		BufferUploader.drawWithShader(buffer.buildOrThrow());
	}
}
