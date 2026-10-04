package velrondevs.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.client.fx.SparkleParticleData;
import velrondevs.botania.common.proxy.Proxy;
import velrondevs.botania.registry.BotaniaBlockEntities;

public class StarfieldCreatorBlockEntity extends BotaniaBlockEntity {
	public StarfieldCreatorBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.STARFIELD, pos, state);
	}

	public static void clientTick(Level level, BlockPos worldPosition, BlockState state, StarfieldCreatorBlockEntity self) {
		level.updateSkyBrightness();
		if (level.isDay()) {
			return;
		}

		double radius = 512;
		int iter = 2;
		for (int i = 0; i < iter; i++) {
			double x = worldPosition.getX() + 0.5 + (Math.random() - 0.5) * radius;
			double y = Math.min(256, worldPosition.getY() + Proxy.INSTANCE.getClientRenderDistance() * 16);
			double z = worldPosition.getZ() + 0.5 + (Math.random() - 0.5) * radius;

			float w = 0.6F;
			float c = 1F - w;

			float r = w + (float) Math.random() * c;
			float g = w + (float) Math.random() * c;
			float b = w + (float) Math.random() * c;

			float s = 20F + (float) Math.random() * 20F;
			int m = 50;

			SparkleParticleData data = SparkleParticleData.sparkle(s, r, g, b, m);
			level.addParticle(data, true, x, y, z, 0, 0, 0);
		}
	}

}
