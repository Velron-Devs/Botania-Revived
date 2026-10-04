package velrondevs.botania.api.corporea;

import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.item.SparkEntity;

import java.util.List;
import java.util.Set;

public interface CorporeaSpark extends SparkEntity {

	void introduceNearbyTo(Set<CorporeaSpark> network, CorporeaSpark master);

	CorporeaNode getSparkNode();

	Set<CorporeaSpark> getConnections();

	List<CorporeaSpark> getRelatives();

	@Nullable
	CorporeaSpark getMaster();

	void onItemExtracted(ItemStack stack);

	void onItemsRequested(List<ItemStack> stacks);

	boolean isMaster();

	boolean isCreative();

}
