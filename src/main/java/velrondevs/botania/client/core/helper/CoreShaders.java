package velrondevs.botania.client.core.helper;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.RenderPipelines;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class CoreShaders {
	public static final RenderPipeline STAR = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
			.withLocation(prefix("pipeline/star"))
			.withVertexShader("core/rendertype_lightning")
			.withFragmentShader("core/rendertype_lightning")
			.withColorTargetState(new ColorTargetState(Optional.of(BlendFunction.LIGHTNING), ColorTargetState.WRITE_COLOR))
			.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES)
			.withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
			.build();

	public static final RenderPipeline HIGHLIGHT_QUADS = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
			.withLocation(prefix("pipeline/highlight_quads"))
			.withCull(false)
			.withDepthStencilState(DepthStencilState.DEFAULT)
			.build();

	public static final RenderPipeline HIGHLIGHT_TRIANGLES = RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
			.withLocation(prefix("pipeline/highlight_triangles"))
			.withCull(false)
			.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES)
			.withDepthStencilState(DepthStencilState.DEFAULT)
			.build();

	public static final RenderPipeline LINES_NO_DEPTH = RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
			.withLocation(prefix("pipeline/lines_no_depth"))
			.withColorTargetState(new ColorTargetState(Optional.of(BlendFunction.TRANSLUCENT), ColorTargetState.WRITE_COLOR))
			.withDepthStencilState(Optional.empty())
			.build();

	public static final RenderPipeline TEXTURED_LIGHTMAP = RenderPipeline.builder(RenderPipelines.TEXT_SNIPPET, RenderPipelines.FOG_SNIPPET)
			.withLocation(prefix("pipeline/textured_lightmap"))
			.withVertexShader("core/rendertype_text")
			.withFragmentShader("core/rendertype_text")
			.withSampler("Sampler0")
			.withSampler("Sampler2")
			.build();

	public static final RenderPipeline TEXTURED_COLOR = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
			.withLocation(prefix("pipeline/textured_color"))
			.withVertexShader("core/position_tex_color")
			.withFragmentShader("core/position_tex_color")
			.withSampler("Sampler0")
			.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
			.withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
			.withDepthStencilState(DepthStencilState.DEFAULT)
			.build();

	public static final RenderPipeline TEXTURED_COLOR_NO_CULL = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
			.withLocation(prefix("pipeline/textured_color_no_cull"))
			.withVertexShader("core/position_tex_color")
			.withFragmentShader("core/position_tex_color")
			.withSampler("Sampler0")
			.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
			.withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
			.withDepthStencilState(DepthStencilState.DEFAULT)
			.withCull(false)
			.build();

	public static final RenderPipeline LIGHTNING_QUADS = RenderPipeline.builder(RenderPipelines.MATRICES_FOG_SNIPPET)
			.withLocation(prefix("pipeline/lightning_quads"))
			.withVertexShader("core/rendertype_lightning")
			.withFragmentShader("core/rendertype_lightning")
			.withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
			.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
			.withDepthStencilState(DepthStencilState.DEFAULT)
			.build();

	public static final RenderPipeline ADDITIVE_PARTICLE = RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
			.withLocation(prefix("pipeline/additive_particle"))
			.withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
			.withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
			.build();

	public static final RenderPipeline ADDITIVE_PARTICLE_NO_DEPTH = RenderPipeline.builder(RenderPipelines.PARTICLE_SNIPPET)
			.withLocation(prefix("pipeline/additive_particle_no_depth"))
			.withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
			.withDepthStencilState(Optional.empty())
			.build();

	public static final RenderPipeline GUI_TRIANGLE_FAN =RenderPipeline.builder(RenderPipelines.GUI_SNIPPET)
			.withLocation(prefix("pipeline/gui_triangle_fan"))
			.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_FAN)
			.build();

	public static final RenderPipeline GUI_TRIANGLES = RenderPipeline.builder(RenderPipelines.GUI_SNIPPET)
			.withLocation(prefix("pipeline/gui_triangles"))
			.withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES)
			.build();

	private static final List<RenderPipeline> ALL = List.of(
			STAR, HIGHLIGHT_QUADS, HIGHLIGHT_TRIANGLES, LINES_NO_DEPTH, TEXTURED_LIGHTMAP, TEXTURED_COLOR,
			TEXTURED_COLOR_NO_CULL, LIGHTNING_QUADS, ADDITIVE_PARTICLE, ADDITIVE_PARTICLE_NO_DEPTH, GUI_TRIANGLE_FAN, GUI_TRIANGLES
	);

	public static void init(Consumer<RenderPipeline> registrations) {
		ALL.forEach(registrations);
	}

	public static RenderPipeline starfield() {
		return RenderPipelines.END_PORTAL;
	}

	public static RenderPipeline doppleganger() {
		return RenderPipelines.ENTITY_TRANSLUCENT;
	}

	public static RenderPipeline manaPool() {
		return TEXTURED_LIGHTMAP;
	}

	public static RenderPipeline terraPlate() {
		return TEXTURED_LIGHTMAP;
	}

	public static RenderPipeline enchanter() {
		return TEXTURED_LIGHTMAP;
	}

	public static RenderPipeline pylon() {
		return RenderPipelines.ENTITY_TRANSLUCENT;
	}

	public static RenderPipeline halo() {
		return TEXTURED_COLOR;
	}

	public static RenderPipeline filmGrainParticle() {
		return ADDITIVE_PARTICLE;
	}

	public static RenderPipeline dopplegangerBar() {
		return RenderPipelines.GUI_TEXTURED;
	}
}
