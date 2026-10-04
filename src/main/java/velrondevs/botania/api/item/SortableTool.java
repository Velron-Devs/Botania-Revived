package velrondevs.botania.api.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public interface SortableTool {

	default int getSortingPriority(ItemStack stack, BlockState state) {
		return 0;
	}

}
