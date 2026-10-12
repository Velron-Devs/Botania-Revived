package velrondevs.botania.integration.thaumaturge;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.item.equipment.armor.manasteel.ManasteelHelmItem;

import java.util.List;

public class ManasteelHelmRevealingItem extends ManasteelHelmItem {
	public ManasteelHelmRevealingItem(Properties props) {
		super(props);
	}

	@NotNull
	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(@NotNull ItemStack stack) {
		return ThaumaturgeIntegration.withVisDiscount(this, super.getDefaultAttributeModifiers(stack));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flags) {
		RevealingHelmTooltip.add(list);
		super.appendHoverText(stack, context, list, flags);
	}
}
