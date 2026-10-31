package velrondevs.botania.compat.sophisticated;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.util.PlayerInventoryProvider;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeHandler;

import velrondevs.botania.common.impl.mana.PlayerManaSources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public final class SophisticatedManaSource implements PlayerManaSources.Source {
	private record Snapshot(long tick, int available) {}

	private final Map<Player, Snapshot> snapshots = new WeakHashMap<>();

	@Override
	public int getAvailable(Player player, ItemStack requestor) {
		if (requestor.isEmpty() || !SophisticatedConfig.linkEnabled()) {
			return 0;
		}
		long tick = player.level().getGameTime();
		synchronized (snapshots) {
			Snapshot snapshot = snapshots.get(player);
			if (snapshot != null && snapshot.tick() == tick) {
				return snapshot.available();
			}
		}
		long total = 0;
		for (ManaPileUpgradeItem.Wrapper pile : findPiles(player)) {
			total += pile.getMana();
		}
		int available = (int) Math.min(Integer.MAX_VALUE, total);
		synchronized (snapshots) {
			snapshots.put(player, new Snapshot(tick, available));
		}
		return available;
	}

	@Override
	public int take(Player player, ItemStack requestor, int amount, boolean simulate) {
		if (requestor.isEmpty() || amount <= 0 || !SophisticatedConfig.linkEnabled()) {
			return 0;
		}
		boolean dry = simulate || player.level().isClientSide();
		int taken = 0;
		for (ManaPileUpgradeItem.Wrapper pile : findPiles(player)) {
			if (taken >= amount) {
				break;
			}
			taken += pile.extract(amount - taken, dry);
		}
		if (!dry && taken > 0) {
			synchronized (snapshots) {
				snapshots.remove(player);
			}
		}
		return taken;
	}

	private static boolean hasEnabledLink(UpgradeHandler upgrades) {
		for (ManaLinkUpgradeItem.Wrapper link : upgrades.getTypeWrappers(ManaLinkUpgradeItem.TYPE)) {
			if (link.isEnabled()) {
				return true;
			}
		}
		return false;
	}

	private static List<ManaPileUpgradeItem.Wrapper> findPiles(Player player) {
		List<ManaPileUpgradeItem.Wrapper> piles = new ArrayList<>();
		PlayerInventoryProvider.get().runOnBackpacks(player, (stack, handlerName, identifier, slot) -> {
			try {
				IBackpackWrapper backpack = BackpackWrapper.fromStack(stack);
				UpgradeHandler upgrades = backpack.getUpgradeHandler();
				if (hasEnabledLink(upgrades)) {
					piles.addAll(ManaPileUpgradeItem.enabledPiles(backpack));
				}
			} catch (RuntimeException ignored) {
			}
			return false;
		});
		return piles;
	}
}
