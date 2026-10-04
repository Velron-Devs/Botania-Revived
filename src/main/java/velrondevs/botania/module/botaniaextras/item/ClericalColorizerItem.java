package velrondevs.botania.module.botaniaextras.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DyedItemColor;

import velrondevs.botania.api.item.CosmeticBauble;
import velrondevs.botania.common.item.equipment.bauble.BaubleItem;
import velrondevs.botania.module.botaniaextras.ClericalColor;

import java.util.List;

public class ClericalColorizerItem extends BaubleItem implements CosmeticBauble, ClericalColor {
	public static final int DEFAULT_COLOR = 0xB0B0B0;

	public ClericalColorizerItem(Properties props) {
		super(props);
	}

	public static boolean hasColor(ItemStack stack) {
		return stack.has(DataComponents.DYED_COLOR);
	}

	public static int getColor(ItemStack stack) {
		return DyedItemColor.getOrDefault(stack, DEFAULT_COLOR) & 0xFFFFFF;
	}

	@Override
	public int clericalColor(ItemStack stack) {
		return hasColor(stack) ? getColor(stack) : -1;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
		tooltip.add(Component.translatable("botaniamisc.cosmeticBauble").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
		super.appendHoverText(stack, context, tooltip, flags);
	}
}
