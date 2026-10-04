package velrondevs.botania.api.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.block.Bound;

public interface CoordBoundItem {

	@Nullable
	BlockPos getBinding(Level world);

}
