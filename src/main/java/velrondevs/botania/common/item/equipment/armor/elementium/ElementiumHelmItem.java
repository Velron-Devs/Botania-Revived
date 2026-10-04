package velrondevs.botania.common.item.equipment.armor.elementium;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.mana.ManaDiscountArmor;
import velrondevs.botania.common.handler.PixieHandler;

public class ElementiumHelmItem extends ElementiumArmorItem implements ManaDiscountArmor {
	public ElementiumHelmItem(Properties props) {
		super(Type.HELMET, props);
	}

	@NotNull
	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(@NotNull ItemStack stack) {
		EquipmentSlot slot = getEquipmentSlot();
		return super.getDefaultAttributeModifiers(stack)
				.withModifierAdded(PixieHandler.PIXIE_SPAWN_CHANCE, PixieHandler.makeModifier(slot, "Armor modifier", 0.11), EquipmentSlotGroup.bySlot(slot));
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, Player player, @Nullable ItemStack tool) {
		return hasArmorSet(player) ? 0.1F : 0F;
	}

}
