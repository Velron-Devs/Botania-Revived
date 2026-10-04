package velrondevs.botania.client.fx;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import velrondevs.botania.registry.BotaniaParticles;

import java.util.function.Function;

public class BotaniaParticleProviders {
	public interface Consumer {
		<T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> constructor);
	}

	public static void registerFactories(Consumer consumer) {
		consumer.register(BotaniaParticles.WISP, WispParticleType.Factory::new);
		consumer.register(BotaniaParticles.SPARKLE, SparkleParticleType.Factory::new);
	}
}
