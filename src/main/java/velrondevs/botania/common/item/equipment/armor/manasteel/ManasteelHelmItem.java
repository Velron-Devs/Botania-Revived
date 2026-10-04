package velrondevs.botania.common.item.equipment.armor.manasteel;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.mana.ManaDiscountArmor;

public class ManasteelHelmItem extends ManasteelArmorItem implements ManaDiscountArmor {

	public ManasteelHelmItem(Properties props) {
		super(Type.HELMET, props);
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, Player player, @Nullable ItemStack tool) {
		return hasArmorSet(player) ? 0.1F : 0F;
	}

}
