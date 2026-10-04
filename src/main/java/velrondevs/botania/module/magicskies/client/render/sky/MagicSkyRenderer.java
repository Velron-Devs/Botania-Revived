package velrondevs.botania.module.magicskies.client.render.sky;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import org.joml.Matrix4f;

import velrondevs.botania.client.core.SkyblockWorldInfo;
import velrondevs.botania.module.magicskies.client.render.sky.layers.PlanetFieldLayer;
import velrondevs.botania.module.magicskies.client.render.sky.layers.RainbowArcLayer;
import velrondevs.botania.module.magicskies.client.render.sky.layers.StarfieldLayer;
import velrondevs.botania.xplat.BotaniaConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MagicSkyRenderer {
	private MagicSkyRenderer() {}

	private static final List<SkyLayer> LAYERS = new ArrayList<>(List.of(
			new StarfieldLayer(),
			new PlanetFieldLayer(),
			new RainbowArcLayer()));

	private record DimensionSettings(SkyCondition condition, SkyPalette palette) {}

	private static final Map<ResourceKey<Level>, DimensionSettings> DIMENSION_SETTINGS = new HashMap<>();
	private static SkyPalette defaultPalette = SkyPalette.CLASSIC;
	private static SkyCondition defaultCondition = SkyCondition.ALWAYS;

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
		if (botaniaFancySkyActive(level)) {
			return false;
		}
		DimensionSettings settings = DIMENSION_SETTINGS.get(level.dimension());
		SkyCondition condition = settings != null ? settings.condition() : defaultCondition;
		return condition.shouldRender(level);
	}

	private static boolean botaniaFancySkyActive(ClientLevel level) {
		boolean gardenOfGlass = level.getLevelData() instanceof SkyblockWorldInfo info && info.isGardenOfGlass();
		return BotaniaConfig.client().enableFancySkybox()
				&& level.dimension() == Level.OVERWORLD
				&& (BotaniaConfig.client().enableFancySkyboxInNormalWorlds() || gardenOfGlass);
	}

	public static void render(PoseStack poseStack, Matrix4f projectionMatrix, ClientLevel level,
			float partialTicks, float voidFade) {
		if (!isActive(level)) {
			return;
		}

		DimensionSettings settings = DIMENSION_SETTINGS.get(level.dimension());
		SkyPalette palette = settings != null ? settings.palette() : defaultPalette;
		SkyRenderContext ctx = new SkyRenderContext(poseStack, projectionMatrix, level, partialTicks, voidFade, palette);

		RenderSystem.enableBlend();
		RenderSystem.depthMask(false);

		for (SkyLayer layer : LAYERS) {
			layer.render(ctx);
		}

		RenderSystem.depthMask(true);
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	}
}
