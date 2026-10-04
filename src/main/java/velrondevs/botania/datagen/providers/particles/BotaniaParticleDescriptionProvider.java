package velrondevs.botania.datagen.providers.particles;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

import velrondevs.botania.registry.BotaniaParticles;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaParticleDescriptionProvider extends ParticleDescriptionProvider {
	public BotaniaParticleDescriptionProvider(PackOutput output, ExistingFileHelper fileHelper) {
		super(output, fileHelper);
	}

	@Override
	protected void addDescriptions() {
		spriteSet(BotaniaParticles.SPARKLE, prefix("sparkle"), 4, false);
		sprite(BotaniaParticles.WISP, prefix("wisp"));
	}
}
