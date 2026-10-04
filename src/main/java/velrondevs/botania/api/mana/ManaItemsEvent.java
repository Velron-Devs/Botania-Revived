package velrondevs.botania.api.mana;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

import java.util.List;

public class ManaItemsEvent extends Event {

	private final Player entityPlayer;
	private final List<ItemStack> items;

	public ManaItemsEvent(Player entityPlayer, List<ItemStack> items) {
		this.entityPlayer = entityPlayer;
		this.items = items;
	}

	public Player getPlayer() {
		return entityPlayer;
	}

	public List<ItemStack> getItems() {
		return items;
	}
}
