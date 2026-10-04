package velrondevs.botania.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

import velrondevs.botania.client.fx.SparkleParticleData;
import velrondevs.botania.client.fx.SparkleParticleType;
import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.client.fx.WispParticleType;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaParticles {
	public static final ParticleType<WispParticleData> WISP = new WispParticleType();
	public static final ParticleType<SparkleParticleData> SPARKLE = new SparkleParticleType();

	public static void registerParticles(BiConsumer<ParticleType<?>, ResourceLocation> r) {
		r.accept(WISP, prefix("wisp"));
		r.accept(SPARKLE, prefix("sparkle"));
	}
}
