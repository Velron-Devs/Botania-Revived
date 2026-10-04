package velrondevs.botania.common.impl.corporea;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.corporea.CorporeaNode;
import velrondevs.botania.api.corporea.CorporeaResult;

import java.util.List;

public record CorporeaResultImpl(List<ItemStack> stacks, int matchedCount, int extractedCount,
		Object2IntMap<CorporeaNode> matchCountsByNode) implements CorporeaResult {
}
