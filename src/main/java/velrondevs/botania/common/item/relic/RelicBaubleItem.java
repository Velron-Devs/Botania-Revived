package velrondevs.botania.common.item.relic;

import java.util.function.Consumer;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import velrondevs.botania.common.item.equipment.bauble.BaubleItem;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.List;

public abstract class RelicBaubleItem extends BaubleItem {

	public RelicBaubleItem(Properties props) {
		super(props);
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot eqSlot) {
		if (!world.isClientSide() && entity instanceof Player player) {
			var relic = XplatAbstractions.INSTANCE.findRelic(stack);
			if (relic != null) {
				relic.tickBinding(player);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext world, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, world, tooltipDisplay, tooltip, flags);
		RelicImpl.addDefaultTooltip(stack, tooltip);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		var relic = XplatAbstractions.INSTANCE.findRelic(stack);
		if (relic != null && entity instanceof Player ePlayer) {
			relic.tickBinding(ePlayer);
			if (relic.isRightPlayer(ePlayer)) {
				onValidPlayerWornTick(ePlayer);
			}
		}
	}

	public void onValidPlayerWornTick(Player player) {}

	@Override
	public boolean canEquip(ItemStack stack, LivingEntity entity) {
		var relic = XplatAbstractions.INSTANCE.findRelic(stack);
		return entity instanceof Player player && relic != null && relic.isRightPlayer(player);
	}
}
