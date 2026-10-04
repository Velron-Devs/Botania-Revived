package velrondevs.botania.module.botaniaextras.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlockEntities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LightningRodBlockEntity extends BlockEntity {
	public static final int RANGE = 48;
	private static final String REDIRECTED_TAG = "botania_extras_redirected";
	private static final int EXTINGUISH_TICKS = 40;
	private static final int EXTINGUISH_RADIUS = 3;

	private static final Map<ResourceKey<Level>, Set<BlockPos>> RODS = new ConcurrentHashMap<>();
	private static final Map<ResourceKey<Level>, List<Strike>> STRIKES = new ConcurrentHashMap<>();

	private record Strike(BlockPos center, long until) {}

	public LightningRodBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaExtrasBlockEntities.LIGHTNING_ROD, pos, state);
	}

	@Override
	public void setLevel(Level level) {
		super.setLevel(level);
		if (!level.isClientSide) {
			RODS.computeIfAbsent(level.dimension(), k -> ConcurrentHashMap.newKeySet()).add(worldPosition.immutable());
		}
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		if (level != null && !level.isClientSide) {
			Set<BlockPos> set = RODS.get(level.dimension());
			if (set != null) {
				set.remove(worldPosition);
			}
		}
	}

	public static void clear() {
		RODS.clear();
		STRIKES.clear();
	}

	public static void onBoltJoin(EntityJoinLevelEvent e) {
		if (e.getLevel().isClientSide() || !(e.getEntity() instanceof LightningBolt bolt) || bolt.getTags().contains(REDIRECTED_TAG)) {
			return;
		}
		if (bolt.getCause() != null || !(e.getLevel() instanceof ServerLevel level) || !level.isThundering()) {
			return;
		}
		Set<BlockPos> rods = RODS.get(level.dimension());
		if (rods == null || rods.isEmpty()) {
			return;
		}
		Vec3 at = bolt.position();
		BlockPos best = null;
		double bestDist = Double.MAX_VALUE;
		for (BlockPos pos : rods) {
			if (Math.abs(pos.getX() + 0.5 - at.x) > RANGE || Math.abs(pos.getY() + 0.5 - at.y) > RANGE || Math.abs(pos.getZ() + 0.5 - at.z) > RANGE) {
				continue;
			}
			if (!(level.getBlockState(pos).getBlock() instanceof ThunderousLogBlock)) {
				continue;
			}
			double dist = pos.distToCenterSqr(at);
			if (dist < bestDist) {
				bestDist = dist;
				best = pos;
			}
		}
		if (best == null) {
			return;
		}
		BlockPos top = best;
		while (level.getBlockState(top.above()).getBlock() instanceof ThunderousLogBlock) {
			top = top.above();
		}
		BlockPos strike = top.above();
		bolt.addTag(REDIRECTED_TAG);
		bolt.moveTo(strike.getX() + 0.5, strike.getY(), strike.getZ() + 0.5);
		STRIKES.computeIfAbsent(level.dimension(), k -> new ArrayList<>()).add(new Strike(strike, level.getGameTime() + EXTINGUISH_TICKS));
	}

	public static void onLevelTick(LevelTickEvent.Post e) {
		if (!(e.getLevel() instanceof ServerLevel level)) {
			return;
		}
		List<Strike> strikes = STRIKES.get(level.dimension());
		if (strikes == null || strikes.isEmpty()) {
			return;
		}
		long now = level.getGameTime();
		Iterator<Strike> it = strikes.iterator();
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		while (it.hasNext()) {
			Strike strike = it.next();
			if (now > strike.until) {
				it.remove();
				continue;
			}
			BlockPos c = strike.center;
			for (int dx = -EXTINGUISH_RADIUS; dx <= EXTINGUISH_RADIUS; dx++) {
				for (int dy = -EXTINGUISH_RADIUS; dy <= EXTINGUISH_RADIUS; dy++) {
					for (int dz = -EXTINGUISH_RADIUS; dz <= EXTINGUISH_RADIUS; dz++) {
						cursor.set(c.getX() + dx, c.getY() + dy, c.getZ() + dz);
						if (level.getBlockState(cursor).is(Blocks.FIRE)) {
							level.removeBlock(cursor, false);
						}
					}
				}
			}
		}
	}
}
