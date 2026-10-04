package velrondevs.botania.common.item.equipment.armor.elementium;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.handler.PixieHandler;

public class ElementiumChestItem extends ElementiumArmorItem {

	public ElementiumChestItem(Properties props) {
		super(Type.CHESTPLATE, props);
	}

	@NotNull
	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(@NotNull ItemStack stack) {
		EquipmentSlot slot = getEquipmentSlot();
		return super.getDefaultAttributeModifiers(stack)
				.withModifierAdded(PixieHandler.PIXIE_SPAWN_CHANCE, PixieHandler.makeModifier(slot, "Armor modifier", 0.17), EquipmentSlotGroup.bySlot(slot));
	}

}
