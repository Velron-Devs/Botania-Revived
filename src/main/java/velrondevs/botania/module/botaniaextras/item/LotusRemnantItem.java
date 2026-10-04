package velrondevs.botania.module.botaniaextras.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class LotusRemnantItem extends Item {
	private final boolean deathly;

	public LotusRemnantItem(boolean deathly, Properties props) {
		super(props);
		this.deathly = deathly;
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return deathly;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
		tooltip.add(Component.translatable("botaniamisc.botania_extras.lotusDesc").withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, context, tooltip, flags);
	}
}
