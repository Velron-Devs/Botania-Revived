package velrondevs.botania.common.item.relic;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.item.Relic;
import velrondevs.botania.common.handler.EquipmentHandler;
import velrondevs.botania.registry.BotaniaItems;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class RingOfThorItem extends RelicBaubleItem {

	public RingOfThorItem(Properties props) {
		super(props);
	}

	public static Relic makeRelic(ItemStack stack) {
		return new RelicImpl(stack, prefix("challenge/thor_ring"));
	}

	public static ItemStack getThorRing(Player player) {
		return EquipmentHandler.findOrEmpty(BotaniaItems.thorRing, player);
	}
}
