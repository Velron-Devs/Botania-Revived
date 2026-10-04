package velrondevs.botania.common.item.equipment.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface BlockStartBreakItem {
	boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player);
}
