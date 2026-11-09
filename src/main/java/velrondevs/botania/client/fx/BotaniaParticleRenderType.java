package velrondevs.botania.client.fx;

import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;

import velrondevs.botania.client.core.helper.CoreShaders;

public final class BotaniaParticleRenderType {
	public static final SingleQuadParticle.Layer ADDITIVE = new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_PARTICLES, CoreShaders.ADDITIVE_PARTICLE);
	public static final SingleQuadParticle.Layer ADDITIVE_NO_DEPTH = new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_PARTICLES, CoreShaders.ADDITIVE_PARTICLE_NO_DEPTH);
	public static final SingleQuadParticle.Layer CORRUPT = new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_PARTICLES, CoreShaders.filmGrainParticle());

	private BotaniaParticleRenderType() {}
}
