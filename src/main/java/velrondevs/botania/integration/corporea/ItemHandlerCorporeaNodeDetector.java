package velrondevs.botania.integration.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.corporea.CorporeaNode;
import velrondevs.botania.api.corporea.CorporeaNodeDetector;
import velrondevs.botania.api.corporea.CorporeaSpark;

public class ItemHandlerCorporeaNodeDetector implements CorporeaNodeDetector {
	@Nullable
	@Override
	public CorporeaNode getNode(Level world, CorporeaSpark spark) {
		IItemHandler inv = getInventory(world, spark.getAttachPos());
		if (inv != null) {
			return new ItemHandlerCorporeaNode(world, spark.getAttachPos(), inv, spark);
		}
		return null;
	}

	@Nullable
	private static IItemHandler getInventory(Level level, BlockPos pos) {
		IItemHandler ret = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, Direction.UP);
		if (ret == null) {
			ret = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
		}
		return ret;
	}
}
