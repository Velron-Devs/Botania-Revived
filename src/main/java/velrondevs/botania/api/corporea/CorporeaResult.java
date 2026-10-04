package velrondevs.botania.api.corporea;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;

import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public interface CorporeaResult {

	default List<ItemStack> stacks() {
		return Collections.emptyList();
	}

	default int matchedCount() {
		return 0;
	}

	default int extractedCount() {
		return 0;
	}

	default Object2IntMap<CorporeaNode> matchCountsByNode() {
		return Object2IntMaps.emptyMap();
	}

	enum Dummy implements CorporeaResult {
		INSTANCE
	}
}
