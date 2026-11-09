package velrondevs.botania.integration.thaumaturge;

import com.leclowndu93150.thaumaturge.api.items.IVisDiscountGear;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import velrondevs.botania.common.item.equipment.armor.terrasteel.TerrasteelHelmItem;

import java.util.function.Consumer;

public class TerrasteelHelmRevealingItem extends TerrasteelHelmItem implements IVisDiscountGear {
	public TerrasteelHelmRevealingItem(Properties props) {
		super(props);
	}

	@Override
	public int getVisDiscount(ItemStack stack) {
		return ThaumaturgeIntegration.VIS_DISCOUNT;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flags) {
		RevealingHelmTooltip.add(tooltip);
		super.appendHoverText(stack, context, tooltipDisplay, tooltip, flags);
	}
}
