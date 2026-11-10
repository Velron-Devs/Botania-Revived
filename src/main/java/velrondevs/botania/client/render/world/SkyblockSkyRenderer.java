package velrondevs.botania.client.render.world;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.neoforge.client.CustomSkyboxRenderer;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Vector4f;

import velrondevs.botania.client.core.SkyblockWorldInfo;
import velrondevs.botania.client.lib.ResourcesLib;
import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.xplat.BotaniaConfig;

import java.util.Random;

public class SkyblockSkyRenderer implements CustomSkyboxRenderer {
	public static final SkyblockSkyRenderer INSTANCE = new SkyblockSkyRenderer();

	private static final Identifier textureSkybox = Identifier.parse(ResourcesLib.MISC_SKYBOX);
	private static final Identifier textureRainbow = Identifier.parse(ResourcesLib.MISC_RAINBOW);
	private static final Identifier[] planetTextures = new Identifier[] {
			Identifier.parse(ResourcesLib.MISC_PLANET + "0.png"),
			Identifier.parse(ResourcesLib.MISC_PLANET + "1.png"),
			Identifier.parse(ResourcesLib.MISC_PLANET + "2.png"),
			Identifier.parse(ResourcesLib.MISC_PLANET + "3.png"),
			Identifier.parse(ResourcesLib.MISC_PLANET + "4.png"),
			Identifier.parse(ResourcesLib.MISC_PLANET + "5.png")
	};

	private static boolean failed = false;
	private static SkyDrawHelper.Frame frame;

	public static boolean isGogSky(ClientLevel world) {
		boolean isGog = world.getLevelData() instanceof SkyblockWorldInfo skyblockInfo && skyblockInfo.isGardenOfGlass();
		return BotaniaConfig.client().enableFancySkybox()
				&& world.dimension() == Level.OVERWORLD
				&& (BotaniaConfig.client().enableFancySkyboxInNormalWorlds() || isGog);
	}

	public static void onExtractLevelRenderState(ExtractLevelRenderStateEvent event) {
		if (failed) {
			return;
		}
		try {
			LevelRenderState state = event.getRenderState();
			ClientLevel level = event.getLevel();
			if (state.customSkyboxRenderer != null || level == null || !isGogSky(level)) {
				return;
			}
			if (state.skyRenderState.skybox != DimensionType.Skybox.OVERWORLD) {
				return;
			}
			float partialTicks = event.getDeltaTracker().getGameTimeDeltaPartialTick(false);
			frame = SkyDrawHelper.captureFrame(level, event.getCamera(), partialTicks);
			state.customSkyboxRenderer = INSTANCE;
		} catch (Throwable t) {
			failed = true;
			SkyDrawHelper.logFailure("garden_of_glass", t);
		}
	}

	@Override
	public boolean renderSky(LevelRenderState levelRenderState, SkyRenderState sky, Matrix4fc modelViewMatrix, Runnable setupFog) {
		SkyDrawHelper.Frame current = frame;
		if (failed || current == null || sky.skybox != DimensionType.Skybox.OVERWORLD) {
			return false;
		}
		try {
			setupFog.run();
			SkyDrawHelper.drawSkyDisc(modelViewMatrix, sky.skyColor);
			SkyDrawHelper.drawSunriseAndSunset(modelViewMatrix, sky.sunAngle, sky.sunriseAndSunsetColor);

			PoseStack ms = new PoseStack();
			ms.mulPose(Axis.YP.rotationDegrees(-90.0F));
			renderExtra(ms, modelViewMatrix, current, 0);

			ms.pushPose();
			ms.mulPose(Axis.XP.rotation(sky.sunAngle));
			SkyDrawHelper.drawSun(SkyDrawHelper.modelView(modelViewMatrix, ms), 60.0F, sky.rainBrightness);
			ms.popPose();

			ms.pushPose();
			ms.mulPose(Axis.XP.rotation(sky.moonAngle));
			SkyDrawHelper.drawMoon(SkyDrawHelper.modelView(modelViewMatrix, ms), sky.moonPhase, 30.0F, sky.rainBrightness);
			ms.popPose();

			ms.pushPose();
			ms.mulPose(Axis.XP.rotation(sky.starAngle));
			renderStars(ms, modelViewMatrix, current);
			if (sky.starBrightness > 0.0F) {
				float b = sky.starBrightness;
				SkyDrawHelper.drawVanillaStars(RenderPipelines.STARS, SkyDrawHelper.modelView(modelViewMatrix, ms), new Vector4f(b, b, b, b));
			}
			ms.popPose();

			if (sky.shouldRenderDarkDisc) {
				SkyDrawHelper.drawDarkDisc(modelViewMatrix);
			}
			return true;
		} catch (Throwable t) {
			failed = true;
			SkyDrawHelper.logFailure("garden_of_glass", t);
			return false;
		}
	}

	public static void renderExtra(PoseStack ms, Matrix4fc baseView, SkyDrawHelper.Frame frame, float insideVoid) {
		float rain = 1.0F - frame.rainLevel();
		float celAng = frame.timeOfDay();
		float effCelAng = frame.effectiveCelestialAngle();

		float scale = 20F;
		float lowA = Math.max(0F, effCelAng - 0.3F) * rain;
		float a = Math.max(0.1F, lowA);

		ms.pushPose();
		Vector4f planetColor = new Vector4f(1F, 1F, 1F, a * 4 * (1F - insideVoid));
		ms.mulPose(new Quaternionf().rotateAxis(VecHelper.toRadians(90), 0.5F, 0.5F, 0F));
		for (int p = 0; p < planetTextures.length; p++) {
			Matrix4f mat = SkyDrawHelper.modelView(baseView, ms);
			BufferBuilder builder = SkyDrawHelper.begin(DefaultVertexFormat.POSITION_TEX);
			builder.addVertex(-scale, 100, -scale).setUv(0.0F, 0.0F);
			builder.addVertex(scale, 100, -scale).setUv(1.0F, 0.0F);
			builder.addVertex(scale, 100, scale).setUv(1.0F, 1.0F);
			builder.addVertex(-scale, 100, scale).setUv(0.0F, 1.0F);
			SkyDrawHelper.drawMesh(SkyDrawHelper.TEXTURED_TRANSLUCENT, builder.buildOrThrow(), mat, planetColor, planetTextures[p]);

			switch (p) {
				case 0 -> {
					ms.mulPose(VecHelper.rotateX(70));
					scale = 12F;
				}
				case 1 -> {
					ms.mulPose(VecHelper.rotateZ(120));
					scale = 15F;
				}
				case 2 -> {
					ms.mulPose(new Quaternionf().rotateAxis(VecHelper.toRadians(80), 1, 0, 1));
					scale = 25F;
				}
				case 3 -> {
					ms.mulPose(VecHelper.rotateZ(100));
					scale = 10F;
				}
				case 4 -> {
					ms.mulPose(new Quaternionf().rotateAxis(VecHelper.toRadians(-60), 1, 0, 0.5F));
					scale = 40F;
				}
				default -> {}
			}
		}
		ms.popPose();

		scale = 20F;
		a = lowA;
		ms.pushPose();
		ms.translate(0, -1, 0);
		ms.mulPose(VecHelper.rotateX(220));
		Vector4f rayColor = new Vector4f(1F, 1F, 1F, a);
		int angles = 90;
		float y = 2F;
		float y0 = 0F;
		float uPer = 1F / 360F;
		float anglePer = 360F / angles;
		double fuzzPer = Math.PI * 10 / angles;
		float rotSpeed = 1F;
		float rotSpeedMod = 0.4F;
		float ticks = frame.animTicks();

		for (int p = 0; p < 3; p++) {
			float baseAngle = rotSpeed * rotSpeedMod * ticks;
			ms.mulPose(VecHelper.rotateY(ticks * 0.25F * rotSpeed * rotSpeedMod));

			Matrix4f mat = SkyDrawHelper.modelView(baseView, ms);
			BufferBuilder builder = SkyDrawHelper.begin(DefaultVertexFormat.POSITION_TEX);
			for (int i = 0; i < angles; i++) {
				int j = i;
				if (i % 2 == 0) {
					j--;
				}

				float ang = j * anglePer + baseAngle;
				float xp = (float) Math.cos(ang * Math.PI / 180F) * scale;
				float zp = (float) Math.sin(ang * Math.PI / 180F) * scale;
				float yo = (float) Math.sin(fuzzPer * j);

				float ut = ang * uPer;
				if (i % 2 == 0) {
					builder.addVertex(xp, yo + y0 + y, zp).setUv(ut, 1F);
					builder.addVertex(xp, yo + y0, zp).setUv(ut, 0);
				} else {
					builder.addVertex(xp, yo + y0, zp).setUv(ut, 0);
					builder.addVertex(xp, yo + y0 + y, zp).setUv(ut, 1F);
				}
			}
			SkyDrawHelper.drawMesh(SkyDrawHelper.TEXTURED_ADDITIVE, builder.buildOrThrow(), mat, new Vector4f(rayColor), textureSkybox);

			switch (p) {
				case 0 -> {
					ms.mulPose(VecHelper.rotateX(20));
					rayColor.set(1F, 0.4F, 0.4F, a);
					fuzzPer = Math.PI * 14 / angles;
					rotSpeed = 0.2F;
				}
				case 1 -> {
					ms.mulPose(VecHelper.rotateX(50));
					rayColor.set(0.4F, 1F, 0.7F, a);
					fuzzPer = Math.PI * 6 / angles;
					rotSpeed = 2F;
				}
				default -> {}
			}
		}
		ms.popPose();

		ms.pushPose();
		scale = 10F;
		float effCelAng1 = celAng;
		if (effCelAng1 > 0.25F) {
			effCelAng1 = 1F - effCelAng1;
		}
		effCelAng1 = 0.25F - Math.min(0.25F, effCelAng1);

		long time = frame.dayTime() + 1000;
		int day = (int) (time / 24000L);
		Random rand = new Random(day * 0xFF);
		float angle1 = rand.nextFloat() * 360F;
		float angle2 = rand.nextFloat() * 360F;
		ms.mulPose(VecHelper.rotateY(angle1));
		ms.mulPose(VecHelper.rotateZ(angle2));

		Matrix4f mat = SkyDrawHelper.modelView(baseView, ms);
		BufferBuilder builder = SkyDrawHelper.begin(DefaultVertexFormat.POSITION_TEX);
		for (int i = 0; i < angles; i++) {
			int j = i;
			if (i % 2 == 0) {
				j--;
			}

			float ang = j * anglePer;
			float xp = (float) Math.cos(ang * Math.PI / 180F) * scale;
			float zp = (float) Math.sin(ang * Math.PI / 180F) * scale;

			float ut = ang * uPer;
			if (i % 2 == 0) {
				builder.addVertex(xp, y0 + y, zp).setUv(ut, 1F);
				builder.addVertex(xp, y0, zp).setUv(ut, 0);
			} else {
				builder.addVertex(xp, y0, zp).setUv(ut, 0);
				builder.addVertex(xp, y0 + y, zp).setUv(ut, 1F);
			}
		}
		SkyDrawHelper.drawMesh(SkyDrawHelper.TEXTURED_TRANSLUCENT, builder.buildOrThrow(), mat,
				new Vector4f(1F, 1F, 1F, effCelAng1 * (1F - insideVoid)), textureRainbow);
		ms.popPose();
	}

	public static void renderStars(PoseStack ms, Matrix4fc baseView, SkyDrawHelper.Frame frame) {
		float rain = 1.0F - frame.rainLevel();
		float effCelAng = frame.effectiveCelestialAngle();
		float alpha = rain * Math.max(0.1F, effCelAng * 2);

		float t = (frame.animTicks() + 2000) * 0.005F;

		drawStarPass(ms, baseView, VecHelper.rotateY(t * 3), 1F, 1F, 1F, alpha);
		drawStarPass(ms, baseView, VecHelper.rotateY(t), 0.5F, 1F, 1F, alpha);
		drawStarPass(ms, baseView, VecHelper.rotateY(t * 2), 1F, 0.75F, 0.75F, alpha);
		drawStarPass(ms, baseView, VecHelper.rotateZ(t * 3), 1F, 1F, 1F, 0.25F * alpha);
		drawStarPass(ms, baseView, VecHelper.rotateZ(t), 0.5F, 1F, 1F, 0.25F * alpha);
		drawStarPass(ms, baseView, VecHelper.rotateZ(t * 2), 1F, 0.75F, 0.75F, 0.25F * alpha);
	}

	private static void drawStarPass(PoseStack ms, Matrix4fc baseView, Quaternionf rotation, float r, float g, float b, float a) {
		ms.pushPose();
		ms.mulPose(rotation);
		SkyDrawHelper.drawVanillaStars(RenderPipelines.STARS, SkyDrawHelper.modelView(baseView, ms), new Vector4f(r, g, b, a));
		ms.popPose();
	}
}
