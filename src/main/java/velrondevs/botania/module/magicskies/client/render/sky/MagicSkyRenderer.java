package velrondevs.botania.module.magicskies.client.render.sky;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.neoforge.client.CustomSkyboxRenderer;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;

import org.joml.Matrix4fc;
import org.joml.Vector4f;

import velrondevs.botania.client.render.world.SkyDrawHelper;
import velrondevs.botania.client.render.world.SkyblockSkyRenderer;
import velrondevs.botania.module.magicskies.client.render.sky.layers.PlanetFieldLayer;
import velrondevs.botania.module.magicskies.client.render.sky.layers.RainbowArcLayer;
import velrondevs.botania.module.magicskies.client.render.sky.layers.StarfieldLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MagicSkyRenderer implements CustomSkyboxRenderer {
	private static final MagicSkyRenderer INSTANCE = new MagicSkyRenderer();

	private MagicSkyRenderer() {}

	private static final List<SkyLayer> LAYERS = new ArrayList<>(List.of(
			new StarfieldLayer(),
			new PlanetFieldLayer(),
			new RainbowArcLayer()));

	private record DimensionSettings(SkyCondition condition, SkyPalette palette) {}

	private static final Map<ResourceKey<Level>, DimensionSettings> DIMENSION_SETTINGS = new HashMap<>();
	private static SkyPalette defaultPalette = SkyPalette.CLASSIC;
	private static SkyCondition defaultCondition = SkyCondition.ALWAYS;

	private static boolean failed = false;
	private static SkyDrawHelper.Frame frame;
	private static SkyPalette framePalette = SkyPalette.CLASSIC;
	private static boolean overlayPending = false;

	public static void addLayer(SkyLayer layer) {
		LAYERS.add(layer);
	}

	public static void setDefaults(SkyCondition condition, SkyPalette palette) {
		defaultCondition = condition;
		defaultPalette = palette;
	}

	public static void register(ResourceKey<Level> dimension, SkyPalette palette) {
		register(dimension, palette, level -> true);
	}

	public static void register(ResourceKey<Level> dimension, SkyPalette palette, SkyCondition extraCondition) {
		DIMENSION_SETTINGS.put(dimension, new DimensionSettings(
				level -> level.dimension().equals(dimension) && extraCondition.shouldRender(level),
				palette));
	}

	public static boolean isActive(ClientLevel level) {
		if (SkyblockSkyRenderer.isGogSky(level)) {
			return false;
		}
		DimensionSettings settings = DIMENSION_SETTINGS.get(level.dimension());
		SkyCondition condition = settings != null ? settings.condition() : defaultCondition;
		return condition.shouldRender(level);
	}

	public static void onExtractLevelRenderState(ExtractLevelRenderStateEvent event) {
		frame = null;
		overlayPending = false;
		if (failed) {
			return;
		}
		try {
			ClientLevel level = event.getLevel();
			if (level == null || !isActive(level)) {
				return;
			}
			LevelRenderState state = event.getRenderState();
			DimensionType.Skybox skybox = state.skyRenderState.skybox;
			if (skybox == DimensionType.Skybox.OVERWORLD) {
				if (state.customSkyboxRenderer != null) {
					return;
				}
				state.customSkyboxRenderer = INSTANCE;
			} else if (skybox == DimensionType.Skybox.END) {
				overlayPending = true;
			} else {
				return;
			}
			DimensionSettings settings = DIMENSION_SETTINGS.get(level.dimension());
			framePalette = settings != null ? settings.palette() : defaultPalette;
			float partialTicks = event.getDeltaTracker().getGameTimeDeltaPartialTick(false);
			frame = SkyDrawHelper.captureFrame(level, event.getCamera(), partialTicks);
		} catch (Throwable t) {
			fail(t);
		}
	}

	public static void renderOverlay(Matrix4fc modelViewMatrix) {
		SkyDrawHelper.Frame current = frame;
		if (failed || !overlayPending || current == null) {
			return;
		}
		overlayPending = false;
		try {
			renderLayers(modelViewMatrix, current);
		} catch (Throwable t) {
			fail(t);
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
			if (sky.starBrightness > 0.0F) {
				PoseStack ms = new PoseStack();
				ms.mulPose(Axis.YP.rotationDegrees(-90.0F));
				ms.mulPose(Axis.XP.rotation(sky.starAngle));
				float b = sky.starBrightness;
				SkyDrawHelper.drawVanillaStars(RenderPipelines.STARS, SkyDrawHelper.modelView(modelViewMatrix, ms), new Vector4f(b, b, b, b));
			}
			if (sky.shouldRenderDarkDisc) {
				SkyDrawHelper.drawDarkDisc(modelViewMatrix);
			}
			renderLayers(modelViewMatrix, current);
			return true;
		} catch (Throwable t) {
			fail(t);
			return false;
		}
	}

	private static void renderLayers(Matrix4fc modelViewMatrix, SkyDrawHelper.Frame current) {
		SkyRenderContext ctx = new SkyRenderContext(new PoseStack(), modelViewMatrix, current.level(),
				current.partialTicks(), 0F, framePalette, current);
		for (SkyLayer layer : LAYERS) {
			layer.render(ctx);
		}
	}

	private static void fail(Throwable t) {
		failed = true;
		frame = null;
		overlayPending = false;
		SkyDrawHelper.logFailure("magic_skies", t);
	}
}
