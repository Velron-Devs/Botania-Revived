package velrondevs.botania.api.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public interface CorporeaNode {

	Level getWorld();

	BlockPos getPos();

	List<ItemStack> countItems(CorporeaRequest request);

	CorporeaSpark getSpark();

	List<ItemStack> extractItems(CorporeaRequest request);
}
