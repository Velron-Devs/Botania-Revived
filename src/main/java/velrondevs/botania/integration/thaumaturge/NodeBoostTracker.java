package velrondevs.botania.integration.thaumaturge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class NodeBoostTracker {
	public static final int CHECK_INTERVAL = 20;

	private boolean scheduled = false;
	private long nextCheck;
	private double multiplier = 1.0;
	private double fraction;

	public void update(Level level, BlockPos pos, NodeBoostApi.Target target) {
		if (!NodeBoostApi.isAvailable()) {
			multiplier = 1.0;
			fraction = 0;
			return;
		}
		long time = level.getGameTime();
		if (scheduled && time < nextCheck && nextCheck - time <= CHECK_INTERVAL * 2L) {
			return;
		}
		nextCheck = time + (scheduled ? CHECK_INTERVAL : 1 + Math.floorMod(pos.asLong(), CHECK_INTERVAL));
		scheduled = true;
		multiplier = NodeBoostApi.multiplier(level, pos, target);
		if (multiplier <= 1.0) {
			multiplier = 1.0;
			fraction = 0;
		}
	}

	public double multiplier() {
		return multiplier;
	}

	public boolean isBoosted() {
		return multiplier > 1.0;
	}

	public int consumeExtraTicks() {
		if (multiplier <= 1.0) {
			return 0;
		}
		double total = multiplier - 1.0 + fraction;
		int extra = (int) Math.min(Integer.MAX_VALUE, total);
		fraction = total - extra;
		int cap = NodeBoostApi.maxExtraTicksPerTick();
		if (extra > cap) {
			extra = cap;
			fraction = 0;
		}
		return extra;
	}
}
