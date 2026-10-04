package velrondevs.botania.api.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import velrondevs.botania.api.mana.*;

import java.util.Collections;
import java.util.Set;

public class DummyManaNetwork implements ManaNetwork {

	public static final DummyManaNetwork instance = new DummyManaNetwork();

	@Override
	public void clear() {}

	@Override
	public ManaPool getClosestPool(BlockPos pos, Level world, int limit) {
		return null;
	}

	@Override
	public ManaCollector getClosestCollector(BlockPos pos, Level world, int limit) {
		return null;
	}

	@Override
	public Set<ManaCollector> getAllCollectorsInWorld(Level world) {
		return Collections.emptySet();
	}

	@Override
	public Set<ManaPool> getAllPoolsInWorld(Level world) {
		return Collections.emptySet();
	}

	@Override
	public void fireManaNetworkEvent(ManaReceiver thing, ManaBlockType type, ManaNetworkAction action) {

	}

}
