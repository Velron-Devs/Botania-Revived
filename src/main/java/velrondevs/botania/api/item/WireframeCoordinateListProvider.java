package velrondevs.botania.api.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface WireframeCoordinateListProvider {

	List<BlockPos> getWireframesToDraw(Player player, ItemStack stack);

	@Nullable
	default BlockPos getSourceWireframe(Player player, ItemStack stack) {
		return null;
	}

}
