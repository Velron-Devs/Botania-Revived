package velrondevs.botania.module.magicskies.client.render.sky.layers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;

import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;

import org.joml.Quaternionf;

import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.module.magicskies.client.render.sky.SkyLayer;
import velrondevs.botania.module.magicskies.client.render.sky.SkyRenderContext;

public final class StarfieldLayer implements SkyLayer {
	private static final int STAR_COUNT = 700;
	private static final float SHELL_RADIUS = 100F;

	private VertexBuffer starSphere;

	@Override
	public void render(SkyRenderContext ctx) {
		if (starSphere == null) {
			starSphere = StarSphereFactory.build(STAR_COUNT, SHELL_RADIUS, 10842L);
		}

		FogRenderer.setupNoFog();
		ShaderInstance shader = GameRenderer.getPositionShader();

		float rain = 1.0F - ctx.level().getRainLevel(ctx.partialTicks());
		float baseIntensity = ctx.baseIntensity();
		float alpha = rain * Math.max(0.1F, baseIntensity * 2) * (1F - ctx.voidFade());

		float speed = ctx.palette().starRotationSpeed();
		float t = (ClientTickHandler.total() + 2000) * 0.005F * speed;

		PoseStack ms = ctx.poseStack();
		starSphere.bind();

		RenderSystem.blendFuncSeparate(770, 771, 1, 0);

		drawPass(ms, ctx, shader, VecHelper.rotateY(t), 1F, 1F, 1F, alpha);

		VertexBuffer.unbind();
	}

	private void drawPass(PoseStack ms, SkyRenderContext ctx, ShaderInstance shader, Quaternionf rotation,
			float r, float g, float b, float a) {
		ms.pushPose();
		ms.mulPose(rotation);
		RenderSystem.setShaderColor(r, g, b, a);
		starSphere.drawWithShader(ms.last().pose(), ctx.projectionMatrix(), shader);
		ms.popPose();
	}

	public void close() {
		if (starSphere != null) {
			starSphere.close();
			starSphere = null;
		}
	}
}
