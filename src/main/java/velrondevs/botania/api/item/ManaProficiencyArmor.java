package velrondevs.botania.api.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ManaProficiencyArmor {

	default boolean shouldGiveProficiency(ItemStack armorStack, EquipmentSlot slot, Player player, ItemStack rod) {
		return false;
	}

}
