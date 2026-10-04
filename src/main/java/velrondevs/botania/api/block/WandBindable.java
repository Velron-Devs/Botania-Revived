package velrondevs.botania.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface WandBindable extends Bound {

	boolean canSelect(Player player, ItemStack wand, BlockPos pos, Direction side);

	boolean bindTo(Player player, ItemStack wand, BlockPos pos, Direction side);

}
