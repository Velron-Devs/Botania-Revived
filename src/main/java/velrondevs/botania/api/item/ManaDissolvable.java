package velrondevs.botania.api.item;

import net.minecraft.world.entity.item.ItemEntity;

import velrondevs.botania.api.mana.ManaPool;

public interface ManaDissolvable {

	void onDissolveTick(ManaPool pool, ItemEntity item);

}
