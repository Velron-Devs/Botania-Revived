package velrondevs.botania.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SpellbindingClothItem extends Item {

	public SpellbindingClothItem(Properties builder) {
		super(builder);
	}

	public static boolean shouldDenyAnvil(ItemStack left, ItemStack right) {
		return left.getItem() instanceof SpellbindingClothItem
				&& !(right.getItem() instanceof SpellbindingClothItem);
	}
}
