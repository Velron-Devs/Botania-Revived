package velrondevs.botania.api.block;

import net.minecraft.core.BlockPos;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.internal.ManaBurst;

public interface Bound {
	BlockPos UNBOUND_POS = ManaBurst.NO_SOURCE;

	@Nullable
	BlockPos getBinding();

}
