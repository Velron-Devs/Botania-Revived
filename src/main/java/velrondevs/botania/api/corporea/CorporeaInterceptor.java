package velrondevs.botania.api.corporea;

import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;

public interface CorporeaInterceptor {

	void interceptRequest(CorporeaRequestMatcher request, int count, CorporeaSpark spark, CorporeaSpark source, List<ItemStack> stacks, Set<CorporeaNode> nodes, boolean doit);

	void interceptRequestLast(CorporeaRequestMatcher request, int count, CorporeaSpark spark, CorporeaSpark source, List<ItemStack> stacks, Set<CorporeaNode> nodes, boolean doit);

}
