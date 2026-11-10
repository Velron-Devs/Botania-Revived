package velrondevs.botania.module.magicskies.client.render.sky.layers;

import com.mojang.blaze3d.vertex.PoseStack;

import org.joml.Quaternionf;
import org.joml.Vector4f;

import velrondevs.botania.client.render.world.SkyDrawHelper;
import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.module.magicskies.client.render.sky.SkyLayer;
import velrondevs.botania.module.magicskies.client.render.sky.SkyRenderContext;

public final class StarfieldLayer implements SkyLayer {
	private static final int STAR_COUNT = 700;
	private static final float SHELL_RADIUS = 100F;

	private SkyDrawHelper.StaticQuads starSphere;

	@Override
	public void render(SkyRenderContext ctx) {
		if (starSphere == null) {
			starSphere = StarSphereFactory.build(STAR_COUNT, SHELL_RADIUS, 10842L);
		}

		float rain = 1.0F - ctx.rainLevel();
		float baseIntensity = ctx.baseIntensity();
		float alpha = rain * Math.max(0.1F, baseIntensity * 2) * (1F - ctx.voidFade());

		float speed = ctx.palette().starRotationSpeed();
		float t = (ctx.animTicks() + 2000) * 0.005F * speed;

		drawPass(ctx, VecHelper.rotateY(t), 1F, 1F, 1F, alpha);
	}

	private void drawPass(SkyRenderContext ctx, Quaternionf rotation, float r, float g, float b, float a) {
		PoseStack ms = ctx.poseStack();
		ms.pushPose();
		ms.mulPose(rotation);
		SkyDrawHelper.drawQuads(SkyDrawHelper.STARS_TRANSLUCENT, starSphere, ctx.currentModelView(), new Vector4f(r, g, b, a));
		ms.popPose();
	}

	public void close() {
		if (starSphere != null) {
			starSphere.buffer().close();
			starSphere = null;
		}
	}
}
