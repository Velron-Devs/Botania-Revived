package velrondevs.botania.common.item.block;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import velrondevs.botania.common.block.PlatformBlock;
import velrondevs.botania.common.block.decor.FlowerMotifBlock;
import velrondevs.botania.common.block.mana.ManaPoolBlock;

import java.util.function.Consumer;

public class TooltipBlockItem extends BlockItem {

	public TooltipBlockItem(Block block, Properties props) {
		super(block, props);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltipDisplay, tooltip, flag);
		Block block = getBlock();
		if (block instanceof ManaPoolBlock pool) {
			pool.appendHoverText(stack, context, tooltipDisplay, tooltip, flag);
		} else if (block instanceof PlatformBlock platform) {
			platform.appendHoverText(stack, context, tooltipDisplay, tooltip, flag);
		} else if (block instanceof FlowerMotifBlock motif) {
			motif.appendHoverText(stack, context, tooltipDisplay, tooltip, flag);
		}
	}
}
