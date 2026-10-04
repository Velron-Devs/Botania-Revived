package velrondevs.botania.common.item.relic;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import velrondevs.botania.xplat.XplatAbstractions;

import java.util.List;

public class RelicItem extends Item {
	public RelicItem(Properties props) {
		super(props);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if (!world.isClientSide && entity instanceof Player player) {
			var relic = XplatAbstractions.INSTANCE.findRelic(stack);
			if (relic != null) {
				relic.tickBinding(player);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext world, List<Component> tooltip, TooltipFlag flags) {
		RelicImpl.addDefaultTooltip(stack, tooltip);
	}
}
