package velrondevs.botania.compat.sophisticated;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ITickableUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class StationUpgradeWrapper<W extends StationUpgradeWrapper<W, I>, I extends UpgradeItemBase<W>> extends UpgradeWrapperBase<W, I> implements ITickableUpgrade {
	protected record Plan(int cost, List<ItemStack> outputs, boolean missingReagent, Runnable commit) {}

	private final UpgradeSlots inventory;

	protected StationUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler, int slots) {
		super(storageWrapper, upgrade, upgradeSaveHandler);
		inventory = new UpgradeSlots(upgrade, slots, (slot, stack) -> accepts(slot, stack), this::slotLimit, this::save);
	}

	public UpgradeSlots getInventory() {
		return inventory;
	}

	public StationState getState() {
		return upgrade.getOrDefault(SophisticatedComponents.STATION, StationState.EMPTY);
	}

	protected abstract boolean stationEnabled();

	protected abstract int interval();

	protected abstract boolean accepts(int slot, ItemStack stack);

	protected int slotLimit(int slot) {
		return 1;
	}

	protected abstract Plan plan(Level level);

	protected static int scaleCost(int cost, int percent) {
		if (cost <= 0 || percent <= 0) {
			return 0;
		}
		return (int) Math.max(1L, Math.min(Integer.MAX_VALUE, (long) cost * percent / 100L));
	}

	@Override
	public void tick(Entity entity, Level level, BlockPos pos) {
		if (level.isClientSide() || !stationEnabled() || isInCooldown(level)) {
			return;
		}
		setCooldown(level, interval());
		int maxCrafts = SophisticatedConfig.stationMaxPerInterval();
		for (int craft = 0; craft < maxCrafts; craft++) {
			if (!run(level)) {
				return;
			}
		}
	}

	private boolean run(Level level) {
		Plan plan = plan(level);
		if (plan == null) {
			setState(StationState.EMPTY);
			return false;
		}
		long total = ManaPileUpgradeItem.availableMana(storageWrapper);
		int available = (int) Math.min(Integer.MAX_VALUE, total);
		if (plan.missingReagent()) {
			setState(new StationState(StationState.NO_REAGENT, available, plan.cost()));
			return false;
		}
		if (plan.cost() > 0) {
			if (!ManaPileUpgradeItem.hasPile(storageWrapper)) {
				setState(new StationState(StationState.NO_PILE, 0, plan.cost()));
				return false;
			}
			if (total < plan.cost()) {
				setState(new StationState(StationState.NEED_MANA, available, plan.cost()));
				return false;
			}
		}
		IItemHandler target = storageWrapper.getInventoryForUpgradeProcessing();
		if (!canStore(target, plan.outputs())) {
			setState(new StationState(StationState.NO_SPACE, available, plan.cost()));
			return false;
		}
		if (plan.cost() > 0 && ManaPileUpgradeItem.drainMana(storageWrapper, plan.cost(), false) < plan.cost()) {
			setState(new StationState(StationState.NEED_MANA, available, plan.cost()));
			return false;
		}
		ItemStack[] pattern = snapshot();
		plan.commit().run();
		for (ItemStack output : plan.outputs()) {
			if (!output.isEmpty()) {
				InventoryHelper.insertIntoInventory(output.copy(), target, false);
			}
		}
		if (SophisticatedConfig.stationAutoFeed()) {
			for (int slot = 0; slot < pattern.length; slot++) {
				StationAutomation.refill(inventory, slot, pattern[slot], pattern[slot].getCount(), target);
			}
		}
		setState(new StationState(StationState.WORKING, (int) Math.min(Integer.MAX_VALUE, Math.max(0L, total - plan.cost())), plan.cost()));
		return true;
	}

	private ItemStack[] snapshot() {
		ItemStack[] pattern = new ItemStack[inventory.getSlots()];
		for (int slot = 0; slot < pattern.length; slot++) {
			pattern[slot] = inventory.getStackInSlot(slot).copy();
		}
		return pattern;
	}

	private void setState(StationState state) {
		if (state.equals(getState())) {
			return;
		}
		if (state.equals(StationState.EMPTY)) {
			upgrade.remove(SophisticatedComponents.STATION);
		} else {
			upgrade.set(SophisticatedComponents.STATION, state);
		}
		save();
	}

	private static boolean canStore(IItemHandler handler, List<ItemStack> outputs) {
		List<ItemStack> merged = new ArrayList<>();
		for (ItemStack output : outputs) {
			if (output.isEmpty()) {
				continue;
			}
			boolean found = false;
			for (ItemStack existing : merged) {
				if (ItemStack.isSameItemSameComponents(existing, output)) {
					existing.grow(output.getCount());
					found = true;
					break;
				}
			}
			if (!found) {
				merged.add(output.copy());
			}
		}
		int needEmpty = 0;
		for (ItemStack stack : merged) {
			if (!InventoryHelper.insertIntoInventory(stack.copy(), handler, true).isEmpty()) {
				return false;
			}
			if (!hasRoomInExisting(handler, stack)) {
				needEmpty++;
			}
		}
		if (needEmpty <= 1) {
			return true;
		}
		int empty = 0;
		for (int slot = 0; slot < handler.getSlots(); slot++) {
			if (handler.getStackInSlot(slot).isEmpty()) {
				empty++;
			}
		}
		return empty >= needEmpty;
	}

	private static boolean hasRoomInExisting(IItemHandler handler, ItemStack stack) {
		for (int slot = 0; slot < handler.getSlots(); slot++) {
			ItemStack existing = handler.getStackInSlot(slot);
			if (!existing.isEmpty() && ItemStack.isSameItemSameComponents(existing, stack)
					&& existing.getCount() + stack.getCount() <= stack.getMaxStackSize()) {
				return true;
			}
		}
		return false;
	}
}
