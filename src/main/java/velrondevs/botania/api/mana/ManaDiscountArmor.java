package velrondevs.botania.api.mana;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

public interface ManaDiscountArmor {

	default float getDiscount(ItemStack stack, int slot, Player player, @Nullable ItemStack tool) {
		return 0;
	}
}
