package velrondevs.botania.module.magicskies.client.render.sky.layers;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import org.joml.Quaternionf;
import org.joml.Vector4f;

import velrondevs.botania.client.render.world.SkyDrawHelper;
import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.module.magicskies.client.lib.SkyTextures;
import velrondevs.botania.module.magicskies.client.render.sky.SkyLayer;
import velrondevs.botania.module.magicskies.client.render.sky.SkyRenderContext;

public final class PlanetFieldLayer implements SkyLayer {
	private static final Identifier[] TEXTURES = SkyTextures.PLANETS;

	private static final int MOON_INDEX = 0;
	private static final int SUN_INDEX = 5;

	private static final float[] RELATIVE_SCALE = { 1F, 0.6F, 0.75F, 1.25F, 0.5F, 2F };

	@Override
	public void render(SkyRenderContext ctx) {
		PoseStack ms = ctx.poseStack();

		float insideVoid = ctx.voidFade();
		float baseIntensity = ctx.baseIntensity();
		float lowAlpha = Math.max(0F, baseIntensity - 0.3F);
		float ambientAlpha = Math.max(0.1F, lowAlpha) * (1F - insideVoid);
		float moonAlpha = moonAlpha(ctx) * (1F - insideVoid);
		float sunAlpha = sunAlpha(ctx) * (1F - insideVoid);

		float scale = ctx.palette().planetScale();

		ms.pushPose();
		ms.mulPose(new Quaternionf().rotateAxis(VecHelper.toRadians(90), 0.5F, 0.5F, 0F));

		for (int i = 0; i < TEXTURES.length; i++) {
			float alpha = (i == MOON_INDEX) ? moonAlpha : (i == SUN_INDEX) ? sunAlpha : ambientAlpha;

			BufferBuilder buffer = SkyDrawHelper.begin(DefaultVertexFormat.POSITION_TEX);
			buffer.addVertex(-scale, 100, -scale).setUv(0.0F, 0.0F);
			buffer.addVertex(scale, 100, -scale).setUv(1.0F, 0.0F);
			buffer.addVertex(scale, 100, scale).setUv(1.0F, 1.0F);
			buffer.addVertex(-scale, 100, scale).setUv(0.0F, 1.0F);
			SkyDrawHelper.drawMesh(SkyDrawHelper.TEXTURED_TRANSLUCENT, buffer.buildOrThrow(), ctx.currentModelView(),
					new Vector4f(1F, 1F, 1F, Math.min(1F, alpha * 4)), TEXTURES[i]);

			applyNextTransform(ms, i);
			if (i + 1 < RELATIVE_SCALE.length) {
				scale = ctx.palette().planetScale() * RELATIVE_SCALE[i + 1];
			}
		}

		ms.popPose();
	}

	private static float moonAlpha(SkyRenderContext ctx) {
		float rain = 1.0F - ctx.rainLevel();
		return ctx.starBrightness() * rain;
	}

	private static float sunAlpha(SkyRenderContext ctx) {
		float rain = 1.0F - ctx.rainLevel();
		float t = (ctx.dayTime() % 24000L) + ctx.partialTicks();

		float value;
		if (t < 11000F) {
			value = 1F;
		} else if (t < 13000F) {
			value = 1F - (t - 11000F) / 2000F;
		} else if (t < 23000F) {
			value = 0F;
		} else {
			value = (t - 23000F) / 1000F;
		}

		return Mth.clamp(value, 0F, 1F) * rain;
	}

	private static void applyNextTransform(PoseStack ms, int index) {
		switch (index) {
			case 0 -> ms.mulPose(VecHelper.rotateX(70));
			case 1 -> ms.mulPose(VecHelper.rotateZ(120));
			case 2 -> ms.mulPose(new Quaternionf().rotateAxis(VecHelper.toRadians(80), 1, 0, 1));
			case 3 -> ms.mulPose(VecHelper.rotateZ(100));
			case 4 -> ms.mulPose(new Quaternionf().rotateAxis(VecHelper.toRadians(-60), 1, 0, 0.5F));
			default -> {}
		}
	}
}
