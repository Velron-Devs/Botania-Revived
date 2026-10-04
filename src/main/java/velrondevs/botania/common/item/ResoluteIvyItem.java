package velrondevs.botania.common.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.internal_caps.KeptItemsComponent;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;

public class ResoluteIvyItem extends Item {

	public static final String TAG_KEEP = "Botania_keepIvy";

	public static final String TAG_PLAYER_KEPT_DROPS = "Botania_playerKeptDrops";
	private static final String TAG_DROP_COUNT = "dropCount";
	private static final String TAG_DROP_PREFIX = "dropPrefix";

	public ResoluteIvyItem(Properties props) {
		super(props);
	}

	public static boolean hasIvy(ItemStack stack) {
		return !stack.isEmpty() && ItemNBTHelper.hasTag(stack) && ItemNBTHelper.getBoolean(stack, TAG_KEEP, false);
	}

	public static void keepDropsOnDeath(Player player) {
		List<ItemStack> keeps = new ArrayList<>();
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (!stack.isEmpty() && ItemNBTHelper.hasTag(stack) && ItemNBTHelper.getBoolean(stack, TAG_KEEP, false)) {
				keeps.add(stack);
				player.getInventory().setItem(i, ItemStack.EMPTY);
			}
		}

		KeptItemsComponent data = XplatAbstractions.INSTANCE.keptItemsComponent(player, false);
		data.addAll(keeps);
	}

	public static void onPlayerRespawn(Player oldPlayer, Player newPlayer, boolean alive) {
		if (!alive) {

			KeptItemsComponent keeps = XplatAbstractions.INSTANCE.keptItemsComponent(oldPlayer, true);

			for (ItemStack stack : keeps.getStacks()) {
				ItemStack copy = stack.copy();
				ItemNBTHelper.removeEntry(copy, TAG_KEEP);
				if (!newPlayer.getInventory().add(copy)) {
					newPlayer.spawnAtLocation(copy);
				}
			}
		}
	}

}
