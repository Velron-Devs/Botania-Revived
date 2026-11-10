package velrondevs.botania.client.core.helper;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.xplat.BotaniaConfig;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaShaders {
	public static final RenderPipeline DOPPLEGANGER = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_LIGHT_DIR_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
			.withLocation(prefix("pipeline/doppleganger"))
			.withVertexShader(prefix("core/doppleganger"))
			.withFragmentShader(prefix("core/doppleganger"))
			.withSampler("Sampler0")
			.withSampler("Sampler1")
			.withSampler("Sampler2")
			.withVertexFormat(DefaultVertexFormat.ENTITY, VertexFormat.Mode.QUADS)
			.withDepthStencilState(DepthStencilState.DEFAULT)
			.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
			.withCull(false)
			.build();

	public static final RenderPipeline PYLON = RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
			.withLocation(prefix("pipeline/pylon"))
			.withFragmentShader(prefix("core/pylon"))
			.withSampler("Sampler1")
			.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
			.withCull(false)
			.build();

	public static final RenderPipeline MANA_POOL = lightmapped("mana_pool");
	public static final RenderPipeline TERRA_PLATE = lightmapped("terra_plate_rune");
	public static final RenderPipeline ENCHANTER = lightmapped("enchanter_rune");

	public static final RenderPipeline HALO = halo("halo", true);
	public static final RenderPipeline HALO_NO_CULL = halo("halo_no_cull", false);

	public static final RenderPipeline FILM_GRAIN_PARTICLE = RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
			.withLocation(prefix("pipeline/film_grain_particle"))
			.withFragmentShader(prefix("core/film_grain_particle"))
			.withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
			.withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
			.build();

	public static final RenderPipeline DOPPLEGANGER_BAR = RenderPipeline.builder(RenderPipelines.GUI_TEXTURED_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
			.withLocation(prefix("pipeline/doppleganger_bar"))
			.withFragmentShader(prefix("core/doppleganger_bar"))
			.build();

	private static final Map<RenderType, Shaded> SHADED = new ConcurrentHashMap<>();
	private static final Set<RenderPipeline> REPORTED = ConcurrentHashMap.newKeySet();

	private record Shaded(RenderPipeline pipeline, RenderType fallback) {}

	private BotaniaShaders() {}

	private static RenderPipeline lightmapped(String name) {
		return RenderPipeline.builder(RenderPipelines.TEXT_SNIPPET, RenderPipelines.FOG_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
				.withLocation(prefix("pipeline/" + name))
				.withVertexShader("core/rendertype_text")
				.withFragmentShader(prefix("core/" + name))
				.withSampler("Sampler0")
				.withSampler("Sampler2")
				.build();
	}

	private static RenderPipeline halo(String name, boolean cull) {
		return RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
				.withLocation(prefix("pipeline/" + name))
				.withVertexShader("core/position_tex_color")
				.withFragmentShader(prefix("core/halo"))
				.withSampler("Sampler0")
				.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
				.withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
				.withDepthStencilState(DepthStencilState.DEFAULT)
				.withCull(cull)
				.build();
	}

	public static boolean usable(RenderPipeline pipeline) {
		try {
			if (!BotaniaConfig.client().useShaders()) {
				return false;
			}
			boolean valid = RenderSystem.getDevice().precompilePipeline(pipeline).isValid();
			if (!valid && REPORTED.add(pipeline)) {
				BotaniaAPI.LOGGER.warn("Botania shader pipeline {} failed to compile, using vanilla rendering instead", pipeline.getLocation());
			}
			return valid;
		} catch (Throwable t) {
			if (REPORTED.add(pipeline)) {
				BotaniaAPI.LOGGER.warn("Botania shader pipeline {} is unavailable, using vanilla rendering instead", pipeline.getLocation(), t);
			}
			return false;
		}
	}

	public static RenderType shaded(String name, RenderPipeline shader, RenderPipeline fallback, Function<RenderPipeline, RenderSetup> setup) {
		RenderType type = RenderType.create(name, setup.apply(shader));
		SHADED.put(type, new Shaded(shader, RenderType.create(name + "_fallback", setup.apply(fallback))));
		return type;
	}

	public static RenderType resolve(RenderType type) {
		Shaded shaded = SHADED.get(type);
		if (shaded == null || usable(shaded.pipeline())) {
			return type;
		}
		return shaded.fallback();
	}

	public static int dopplegangerTint(float grainIntensity, float disfiguration) {
		int r = Mth.clamp(Math.round((1F - (grainIntensity - 0.05F) / 0.95F) * 255F), 0, 255);
		int g = Mth.clamp(Math.round((1F - (disfiguration - 0.025F) / 0.5F) * 255F), 0, 255);
		return ARGB.color(255, r, g, 255);
	}

	public static int dopplegangerBarTint(float grainIntensity, float hpFract) {
		int r = Mth.clamp(Math.round(grainIntensity * 255F), 0, 255);
		int g = Mth.clamp(Math.round(hpFract * 255F), 0, 255);
		return ARGB.color(255, r, g, 255);
	}
}
