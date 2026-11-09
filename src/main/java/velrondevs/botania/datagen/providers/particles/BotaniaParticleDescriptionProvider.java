package velrondevs.botania.datagen.providers.particles;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.data.ParticleDescriptionProvider;

import velrondevs.botania.registry.BotaniaParticles;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaParticleDescriptionProvider extends ParticleDescriptionProvider {
	public BotaniaParticleDescriptionProvider(PackOutput output) {
		super(output);
	}

	@Override
	protected void addDescriptions() {
		spriteSet(BotaniaParticles.SPARKLE, prefix("sparkle"), 4, false);
		spriteSet(BotaniaParticles.WISP, prefix("wisp"));
	}
}
