package velrondevs.botania.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WitherBossRenderer;
import net.minecraft.client.renderer.entity.state.WitherRenderState;
import net.minecraft.resources.Identifier;

import velrondevs.botania.client.lib.ResourcesLib;

public class PinkWitherRenderer extends WitherBossRenderer {
	private static final Identifier resource = Identifier.parse(ResourcesLib.MODEL_PINK_WITHER);

	public PinkWitherRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public Identifier getTextureLocation(WitherRenderState state) {
		return resource;
	}
}
