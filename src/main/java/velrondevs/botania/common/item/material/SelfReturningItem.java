package velrondevs.botania.common.item.material;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStackTemplate;

import org.jetbrains.annotations.Nullable;

public class SelfReturningItem extends Item {

	public SelfReturningItem(Item.Properties builder) {
		super(builder);
	}

	@Override
	public @Nullable ItemStackTemplate getCraftingRemainder(ItemInstance instance) {
		return new ItemStackTemplate(this);
	}
}
