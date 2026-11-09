package velrondevs.botania.compat.sophisticated;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.util.PlayerInventoryProvider;

import velrondevs.botania.api.mana.ManaItemHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public final class PileChargeHandler {
	private static final Map<Player, Long> NEXT_CHARGE = new WeakHashMap<>();

	private PileChargeHandler() {}

	static void register() {
		NeoForge.EVENT_BUS.addListener(PileChargeHandler::onPlayerTick);
	}

	private static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (player.level().isClientSide() || player.isSpectator() || !SophisticatedConfig.pileChargeItems() || !SophisticatedConfig.linkEnabled()) {
			return;
		}
		long time = player.level().getGameTime();
		Long next = NEXT_CHARGE.get(player);
		if (next != null && time < next) {
			return;
		}
		NEXT_CHARGE.put(player, time + SophisticatedConfig.pileChargeInterval());
		List<ManaPileUpgradeItem.Wrapper> piles = findDischargingPiles(player);
		if (piles.isEmpty()) {
			return;
		}
		List<ItemStack> targets = new ArrayList<>(ManaItemHandler.instance().getManaItems(player));
		targets.addAll(ManaItemHandler.instance().getManaAccesories(player));
		if (targets.isEmpty()) {
			return;
		}
		for (ManaPileUpgradeItem.Wrapper pile : piles) {
			pile.chargeStacks(targets);
		}
	}

	private static List<ManaPileUpgradeItem.Wrapper> findDischargingPiles(Player player) {
		Set<ManaPileUpgradeItem.Wrapper> found = Collections.newSetFromMap(new IdentityHashMap<>());
		List<ManaPileUpgradeItem.Wrapper> piles = new ArrayList<>();
		PlayerInventoryProvider.get().runOnBackpacks(player, (stack, handlerName, identifier, slot) -> {
			try {
				IBackpackWrapper backpack = BackpackWrapper.fromStack(stack);
				if (ManaLinkUpgradeItem.isActive(backpack)) {
					for (ManaPileUpgradeItem.Wrapper pile : ManaPileUpgradeItem.enabledPiles(backpack)) {
						if (pile.isDischarging() && found.add(pile)) {
							piles.add(pile);
						}
					}
				}
			} catch (RuntimeException ignored) {
			}
			return false;
		});
		return piles;
	}
}
