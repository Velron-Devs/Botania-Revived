package velrondevs.botania.common.item.equipment.bauble;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.xplat.XplatAbstractions;

public class RingOfFarReachItem extends BaubleItem {

	public RingOfFarReachItem(Properties props) {
		super(props);
	}

	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getEquippedAttributeModifiers(ItemStack stack) {
		Multimap<Holder<Attribute>, AttributeModifier> attributes = HashMultimap.create();
		attributes.put(XplatAbstractions.INSTANCE.getReachDistanceAttribute(),
				new AttributeModifier(getBaubleModifierId(stack), 3.5, AttributeModifier.Operation.ADD_VALUE));
		return attributes;
	}
}
