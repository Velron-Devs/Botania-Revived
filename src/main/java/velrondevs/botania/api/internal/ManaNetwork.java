package velrondevs.botania.api.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.mana.*;

import java.util.Set;

public interface ManaNetwork {

	void clear();

	@Nullable
	ManaCollector getClosestCollector(BlockPos pos, Level world, int limit);

	@Nullable
	ManaPool getClosestPool(BlockPos pos, Level world, int limit);

	Set<ManaCollector> getAllCollectorsInWorld(Level world);

	Set<ManaPool> getAllPoolsInWorld(Level world);

	void fireManaNetworkEvent(ManaReceiver thing, ManaBlockType type, ManaNetworkAction action);
}
