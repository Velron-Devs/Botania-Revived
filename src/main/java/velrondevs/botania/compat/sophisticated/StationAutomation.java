package velrondevs.botania.compat.sophisticated;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;

public final class StationAutomation {
	private StationAutomation() {}

	public static void refill(UpgradeSlots slots, int slot, ItemStack pattern, int target, IItemHandler source) {
		if (pattern.isEmpty()) {
			return;
		}
		ItemStack current = slots.getStackInSlot(slot);
		if (!current.isEmpty() && !ItemStack.isSameItemSameComponents(current, pattern)) {
			return;
		}
		int missing = Math.min(target, Math.min(slots.getSlotLimit(slot), pattern.getMaxStackSize())) - current.getCount();
		if (missing <= 0) {
			return;
		}
		int taken = 0;
		for (int index = 0; index < source.getSlots() && taken < missing; index++) {
			ItemStack stack = source.getStackInSlot(index);
			if (stack.isEmpty() || !ItemStack.isSameItemSameComponents(stack, pattern)) {
				continue;
			}
			ItemStack extracted = source.extractItem(index, missing - taken, false);
			if (!extracted.isEmpty() && ItemStack.isSameItemSameComponents(extracted, pattern)) {
				taken += extracted.getCount();
			}
		}
		if (taken > 0) {
			slots.setStackInSlot(slot, pattern.copyWithCount(current.getCount() + taken));
		}
	}

	public static void flush(UpgradeSlots slots, int slot, IItemHandler target) {
		ItemStack stack = slots.getStackInSlot(slot);
		if (stack.isEmpty()) {
			return;
		}
		ItemStack rest = InventoryHelper.insertIntoInventory(stack.copy(), target, false);
		if (rest.getCount() != stack.getCount()) {
			slots.setStackInSlot(slot, rest.isEmpty() ? ItemStack.EMPTY : rest);
		}
	}
}
