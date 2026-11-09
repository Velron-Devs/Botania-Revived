package velrondevs.botania.compat.sophisticated;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;

public final class StationAutomation {
	private StationAutomation() {}

	public static void refill(UpgradeSlots slots, int slot, ItemStack pattern, int target, ResourceHandler<ItemResource> source) {
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
		int taken = InventoryHelper.extract(source, ItemResource.of(pattern), missing);
		if (taken > 0) {
			slots.setStackInSlot(slot, pattern.copyWithCount(current.getCount() + taken));
		}
	}

	public static void flush(UpgradeSlots slots, int slot, ResourceHandler<ItemResource> target) {
		ItemStack stack = slots.getStackInSlot(slot);
		if (stack.isEmpty()) {
			return;
		}
		int moved = InventoryHelper.insert(target, stack.copy());
		if (moved > 0) {
			slots.setStackInSlot(slot, moved >= stack.getCount() ? ItemStack.EMPTY : stack.copyWithCount(stack.getCount() - moved));
		}
	}
}
