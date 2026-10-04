package velrondevs.botania.api.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface SequentialBreaker {

	void breakOtherBlock(Player player, ItemStack stack, BlockPos pos, BlockPos originPos, Direction side);

}
