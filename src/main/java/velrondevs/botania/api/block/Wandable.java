package velrondevs.botania.api.block;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

public interface Wandable {

	boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side);

}
