package velrondevs.botania.common.impl.mana;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class PlayerManaSources {
	public interface Source {
		int getAvailable(Player player, ItemStack requestor);

		int take(Player player, ItemStack requestor, int amount, boolean simulate);
	}

	private static final List<Source> SOURCES = new CopyOnWriteArrayList<>();

	private PlayerManaSources() {}

	public static void register(Source source) {
		SOURCES.add(source);
	}

	static int available(Player player, ItemStack requestor) {
		long total = 0;
		for (Source source : SOURCES) {
			total += Math.max(0, source.getAvailable(player, requestor));
		}
		return (int) Math.min(Integer.MAX_VALUE, total);
	}

	static int take(Player player, ItemStack requestor, int amount, boolean simulate) {
		int taken = 0;
		for (Source source : SOURCES) {
			if (taken >= amount) {
				break;
			}
			int available = Math.max(0, source.getAvailable(player, requestor));
			if (available <= 0) {
				continue;
			}
			taken += Math.max(0, source.take(player, requestor, Math.min(available, amount - taken), simulate));
		}
		return taken;
	}
}
