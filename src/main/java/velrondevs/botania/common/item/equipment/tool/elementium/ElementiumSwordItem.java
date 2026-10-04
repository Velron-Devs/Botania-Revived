package velrondevs.botania.common.item.equipment.tool.elementium;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.common.handler.PixieHandler;
import velrondevs.botania.common.item.equipment.tool.manasteel.ManasteelSwordItem;

public class ElementiumSwordItem extends ManasteelSwordItem {

	public ElementiumSwordItem(Properties props) {
		this(BotaniaAPI.instance().getElementiumItemTier(), props);
	}

	private ElementiumSwordItem(Tier tier, Properties props) {
		super(tier, createAttributes(tier, 3, -2.4F)
				.withModifierAdded(PixieHandler.PIXIE_SPAWN_CHANCE, PixieHandler.makeModifier(EquipmentSlot.MAINHAND, "Sword modifier", 0.05), EquipmentSlotGroup.MAINHAND), props);
	}

}
