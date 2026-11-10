package velrondevs.botania.integration.thaumaturge.node;

import com.leclowndu93150.thaumaturge.content.aura.node.BlockEntityNode;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

import velrondevs.botania.integration.thaumaturge.NodeBoostApi;

import java.util.Map;
import java.util.WeakHashMap;

public final class NodeBoost implements NodeBoostApi.NodeLocator {
	private static final int CACHE_TICKS = 20;
	private static final int PURGE_INTERVAL = 600;
	private static final long[] NONE = new long[0];

	private final Map<Level, LevelCache> caches = new WeakHashMap<>();

	private NodeBoost() {}

	public static void register() {
		NodeBoostApi.setLocator(new NodeBoost());
	}

	@Override
	public boolean hasNodeWithin(Level level, BlockPos pos, int range) {
		LevelCache cache;
		synchronized (caches) {
			cache = caches.computeIfAbsent(level, l -> new LevelCache());
		}
		synchronized (cache) {
			long time = level.getGameTime();
			cache.purge(time);
			long rangeSq = (long) range * range;
			int px = pos.getX();
			int py = pos.getY();
			int pz = pos.getZ();
			int minCx = SectionPos.blockToSectionCoord(px - range);
			int maxCx = SectionPos.blockToSectionCoord(px + range);
			int minCz = SectionPos.blockToSectionCoord(pz - range);
			int maxCz = SectionPos.blockToSectionCoord(pz + range);
			for (int cx = minCx; cx <= maxCx; cx++) {
				for (int cz = minCz; cz <= maxCz; cz++) {
					for (long packed : cache.nodes(level, cx, cz, time)) {
						long dx = BlockPos.getX(packed) - px;
						long dy = BlockPos.getY(packed) - py;
						long dz = BlockPos.getZ(packed) - pz;
						if (dx * dx + dy * dy + dz * dz <= rangeSq) {
							return true;
						}
					}
				}
			}
			return false;
		}
	}

	private static long[] scan(Level level, int cx, int cz) {
		LevelChunk chunk = level.getChunkSource().getChunkNow(cx, cz);
		if (chunk == null) {
			return NONE;
		}
		LongArrayList found = null;
		for (BlockEntity be : chunk.getBlockEntities().values()) {
			if (be instanceof BlockEntityNode && !be.isRemoved()) {
				if (found == null) {
					found = new LongArrayList(2);
				}
				found.add(be.getBlockPos().asLong());
			}
		}
		return found == null ? NONE : found.toLongArray();
	}

	private record Entry(long time, long[] nodes) {}

	private static final class LevelCache {
		private final Long2ObjectOpenHashMap<Entry> chunks = new Long2ObjectOpenHashMap<>();
		private long lastPurge = Long.MIN_VALUE;

		long[] nodes(Level level, int cx, int cz, long time) {
			long key = ChunkPos.asLong(cx, cz);
			Entry entry = chunks.get(key);
			if (entry != null && time >= entry.time() && time - entry.time() < CACHE_TICKS) {
				return entry.nodes();
			}
			long[] nodes = scan(level, cx, cz);
			chunks.put(key, new Entry(time, nodes));
			return nodes;
		}

		void purge(long time) {
			if (lastPurge != Long.MIN_VALUE && time >= lastPurge && time - lastPurge < PURGE_INTERVAL) {
				return;
			}
			lastPurge = time;
			chunks.values().removeIf(e -> time < e.time() || time - e.time() >= CACHE_TICKS * 5L);
		}
	}
}
