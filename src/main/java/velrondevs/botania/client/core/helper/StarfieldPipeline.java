package velrondevs.botania.client.core.helper;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import net.minecraft.client.renderer.RenderPipelines;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class StarfieldPipeline {
	private StarfieldPipeline() {}

	public static final RenderPipeline PIPELINE = RenderPipeline.builder(RenderPipelines.END_PORTAL_SNIPPET)
			.withLocation(prefix("pipeline/starfield"))
			.withFragmentShader(prefix("core/starfield"))
			.withShaderDefine("PORTAL_LAYERS", 15)
			.build();
}
