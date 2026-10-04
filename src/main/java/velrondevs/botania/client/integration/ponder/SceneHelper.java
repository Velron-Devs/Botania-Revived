package velrondevs.botania.client.integration.ponder;

import net.createmod.ponder.api.ParticleEmitter;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.client.fx.SparkleParticleData;
import velrondevs.botania.api.mana.spark.SparkUpgradeType;
import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.common.entity.CorporeaSparkEntity;
import velrondevs.botania.common.entity.ManaSparkEntity;
import velrondevs.botania.registry.BotaniaEntities;

final class SceneHelper {
	static final int MANA_COLOR = 0x20FF20;
	static final int POOL_COLOR = 0x00C6FF;
	static final int GAIA_COLOR = 0xF74BC8;

	private SceneHelper() {}

	static void burst(SceneBuilder scene, Vec3 from, Vec3 to, int ticks, int color) {
		float r = (color >> 16 & 0xFF) / 255F;
		float g = (color >> 8 & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;
		ParticleEmitter emitter = (level, x, y, z) -> level.addParticle(WispParticleData.wisp(0.3F, r, g, b, 3F), x, y, z, 0, 0, 0);
		for (int i = 0; i <= ticks; i++) {
			scene.effects().emitParticles(from.lerp(to, (double) i / ticks), emitter, 2F, 1);
			scene.idle(1);
		}
	}

	static void path(SceneBuilder scene, int color, int ticksPerSegment, Vec3... points) {
		for (int i = 0; i + 1 < points.length; i++) {
			burst(scene, points[i], points[i + 1], ticksPerSegment, color);
		}
	}

	static void flow(SceneBuilder scene, Vec3 from, Vec3[] targets, int ticks, int color) {
		float r = (color >> 16 & 0xFF) / 255F;
		float g = (color >> 8 & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;
		ParticleEmitter emitter = (level, x, y, z) -> level.addParticle(WispParticleData.wisp(0.3F, r, g, b, 3F), x, y, z, 0, 0, 0);
		for (int i = 0; i <= ticks; i++) {
			for (Vec3 target : targets) {
				scene.effects().emitParticles(from.lerp(target, (double) i / ticks), emitter, 2F, 1);
			}
			scene.idle(1);
		}
	}

	static ElementLink<EntityElement> spark(SceneBuilder scene, Vec3 at, SparkUpgradeType upgrade) {
		return scene.world().createEntity(level -> {
			ManaSparkEntity spark = new ManaSparkEntity(level);
			spark.setPos(at.x, at.y, at.z);
			if (upgrade != SparkUpgradeType.NONE) {
				try {
					spark.setUpgrade(upgrade);
				} catch (RuntimeException ignored) {}
			}
			return spark;
		});
	}

	static ElementLink<EntityElement> corporeaSpark(SceneBuilder scene, Vec3 at, boolean master) {
		return scene.world().createEntity(level -> {
			CorporeaSparkEntity spark = BotaniaEntities.CORPOREA_SPARK.create(level);
			spark.setPos(at.x, at.y, at.z);
			spark.setMaster(master);
			return spark;
		});
	}

	static void wisp(SceneBuilder scene, Vec3 at, int color) {
		float r = (color >> 16 & 0xFF) / 255F;
		float g = (color >> 8 & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;
		ParticleEmitter emitter = (level, x, y, z) -> level.addParticle(WispParticleData.wisp(0.35F, r, g, b, 3F), x, y, z, 0, 0, 0);
		scene.effects().emitParticles(at, emitter, 1F, 1);
	}

	static void sparkles(SceneBuilder scene, Vec3 at, int color, int amount) {
		float r = (color >> 16 & 0xFF) / 255F;
		float g = (color >> 8 & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;
		ParticleEmitter emitter = (level, x, y, z) -> level.addParticle(
				SparkleParticleData.sparkle(0.6F + (float) Math.random() * 0.4F, r, g, b, 10),
				x + (Math.random() - 0.5) * 0.5, y + Math.random() * 0.4, z + (Math.random() - 0.5) * 0.5, 0, 0, 0);
		scene.effects().emitParticles(at, emitter, amount, 1);
	}
}
