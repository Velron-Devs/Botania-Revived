package velrondevs.botania.module.magicskies.client.render.sky.layers;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.GameRenderer;

import org.joml.Matrix4f;

import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.module.magicskies.client.lib.SkyTextures;
import velrondevs.botania.module.magicskies.client.render.sky.SkyLayer;
import velrondevs.botania.module.magicskies.client.render.sky.SkyRenderContext;

import java.util.Random;

public final class RainbowArcLayer implements SkyLayer {
	private static final int ANGLES = 90;
	private static final float ANGLE_STEP = 360F / ANGLES;
	private static final float U_PER_DEGREE = 1F / 360F;
	private static final float BAND_HEIGHT = 2F;

	@Override
	public void render(SkyRenderContext ctx) {
		PoseStack ms = ctx.poseStack();
		Tesselator tessellator = Tesselator.getInstance();

		float celestialAngle = ctx.level().getTimeOfDay(ctx.partialTicks());
		float visibility = celestialAngle > 0.25F ? 1F - celestialAngle : celestialAngle;
		visibility = 0.25F - Math.min(0.25F, visibility);

		long dayTime = ctx.level().getDayTime() + 1000;
		int day = (int) (dayTime / 24000L);
		Random random = new Random(day * 0xFFL);
		float rotationY = random.nextFloat() * 360F;
		float rotationZ = random.nextFloat() * 360F;

		ms.pushPose();
		GlStateManager._blendFuncSeparate(770, 771, 1, 0);
		RenderSystem.setShaderTexture(0, SkyTextures.RAINBOW);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1F, 1F, 1F, visibility * (1F - ctx.voidFade()));
		ms.mulPose(VecHelper.rotateY(rotationY));
		ms.mulPose(VecHelper.rotateZ(rotationZ));

		float scale = ctx.palette().rainbowScale();
		Matrix4f mat = ms.last().pose();
		var buffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		for (int i = 0; i < ANGLES; i++) {
			int j = (i % 2 == 0) ? i - 1 : i;
			float ang = j * ANGLE_STEP;
			float xp = (float) Math.cos(ang * Math.PI / 180F) * scale;
			float zp = (float) Math.sin(ang * Math.PI / 180F) * scale;
			float u = ang * U_PER_DEGREE;

			if (i % 2 == 0) {
				buffer.addVertex(mat, xp, BAND_HEIGHT, zp).setUv(u, 1F);
				buffer.addVertex(mat, xp, 0F, zp).setUv(u, 0F);
			} else {
				buffer.addVertex(mat, xp, 0F, zp).setUv(u, 0F);
				buffer.addVertex(mat, xp, BAND_HEIGHT, zp).setUv(u, 1F);
			}
		}
		BufferUploader.drawWithShader(buffer.buildOrThrow());
		ms.popPose();

		RenderSystem.setShaderColor(1F, 1F, 1F, 1F - ctx.voidFade());
		GlStateManager._blendFuncSeparate(770, 1, 1, 0);
	}
}
