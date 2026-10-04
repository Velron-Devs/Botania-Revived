package velrondevs.botania.common.helper;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;

import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class EthicalTntHelper {

	private static final ThreadLocal<EthicalTntHelper> tracker = ThreadLocal.withInitial(EthicalTntHelper::new);

	private final AtomicInteger trackTntEntities = new AtomicInteger();

	private final WeakHashMap<Level, IntOpenHashSet> trackedTntEntities = new WeakHashMap<>();

	public static void startTrackingTntEntities() {
		tracker.get().startTracking();
	}

	public static void addTrackedTntEntity(PrimedTnt entity) {
		tracker.get().addTrackedEntity(entity);
	}

	public static void endTrackingTntEntitiesAndCheck() {
		tracker.get().endTracking();
	}

	private void startTracking() {
		trackTntEntities.incrementAndGet();
	}

	private void addTrackedEntity(PrimedTnt entity) {
		if (trackTntEntities.get() > 0) {
			trackedTntEntities.computeIfAbsent(entity.level(), lvl -> new IntOpenHashSet()).add(entity.getId());
		}
	}

	private void endTracking() {
		if (trackTntEntities.decrementAndGet() == 0) {
			for (final var entry : trackedTntEntities.entrySet()) {
				final var level = entry.getKey();
				final var trackedEntities = entry.getValue();
				if (trackedEntities != null) {
					for (final var tntId : trackedEntities) {
						final var entity = level.getEntity(tntId);
						if (entity instanceof PrimedTnt tnt) {
							checkUnethical(tnt);
						}
					}
					trackedEntities.clear();
				}
			}
		}
	}

	private static void checkUnethical(PrimedTnt entity) {
		BlockPos center = entity.blockPosition();
		if (!entity.level().isLoaded(center)) {
			return;
		}

		BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
		for (final var dir : Direction.values()) {
			blockPos.setWithOffset(center, dir);
			if (!entity.level().isLoaded(blockPos)) {
				continue;
			}

			final var blockState = entity.level().getBlockState(blockPos);
			if (blockState.is(Blocks.MOVING_PISTON)
					&& entity.level().getBlockEntity(blockPos) instanceof PistonMovingBlockEntity movingBlockEntity
					&& movingBlockEntity.getMovementDirection() == dir
					&& (movingBlockEntity.getMovedState().getBlock() instanceof TntBlock
							|| movingBlockEntity.getMovedState().is(BotaniaTags.Blocks.UNETHICAL_TNT_CHECK))) {

				XplatAbstractions.INSTANCE.ethicalComponent(entity).markUnethical();
				break;
			}
		}
	}
}
