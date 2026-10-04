package velrondevs.botania.common.item.material;

import net.minecraft.world.item.Item;

public class SelfReturningItem extends Item {

	public SelfReturningItem(Item.Properties builder) {
		super(builder);
	}

	@Override
	public boolean hasCraftingRemainingItem() {
		return true;
	}
}
