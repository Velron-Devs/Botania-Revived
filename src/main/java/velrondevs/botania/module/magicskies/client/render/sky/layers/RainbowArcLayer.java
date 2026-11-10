package velrondevs.botania.module.magicskies.client.render.sky.layers;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;

import org.joml.Vector4f;

import velrondevs.botania.client.render.world.SkyDrawHelper;
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

		float celestialAngle = ctx.celestialAngle();
		float visibility = celestialAngle > 0.25F ? 1F - celestialAngle : celestialAngle;
		visibility = 0.25F - Math.min(0.25F, visibility);

		long dayTime = ctx.dayTime() + 1000;
		int day = (int) (dayTime / 24000L);
		Random random = new Random(day * 0xFFL);
		float rotationY = random.nextFloat() * 360F;
		float rotationZ = random.nextFloat() * 360F;

		ms.pushPose();
		ms.mulPose(VecHelper.rotateY(rotationY));
		ms.mulPose(VecHelper.rotateZ(rotationZ));

		float scale = ctx.palette().rainbowScale();
		BufferBuilder buffer = SkyDrawHelper.begin(DefaultVertexFormat.POSITION_TEX);
		for (int i = 0; i < ANGLES; i++) {
			int j = (i % 2 == 0) ? i - 1 : i;
			float ang = j * ANGLE_STEP;
			float xp = (float) Math.cos(ang * Math.PI / 180F) * scale;
			float zp = (float) Math.sin(ang * Math.PI / 180F) * scale;
			float u = ang * U_PER_DEGREE;

			if (i % 2 == 0) {
				buffer.addVertex(xp, BAND_HEIGHT, zp).setUv(u, 1F);
				buffer.addVertex(xp, 0F, zp).setUv(u, 0F);
			} else {
				buffer.addVertex(xp, 0F, zp).setUv(u, 0F);
				buffer.addVertex(xp, BAND_HEIGHT, zp).setUv(u, 1F);
			}
		}
		SkyDrawHelper.drawMesh(SkyDrawHelper.TEXTURED_TRANSLUCENT, buffer.buildOrThrow(), ctx.currentModelView(),
				new Vector4f(1F, 1F, 1F, visibility * (1F - ctx.voidFade())), SkyTextures.RAINBOW);
		ms.popPose();
	}
}
