package velrondevs.botania.compat.sophisticated;

import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.init.ModCoreDataComponents;
import net.p3pp3rf1y.sophisticatedcore.inventory.StatefulComponentItemHandler;

import java.util.function.BiPredicate;
import java.util.function.IntUnaryOperator;

public class UpgradeSlots extends StatefulComponentItemHandler {
	private final BiPredicate<Integer, ItemStack> valid;
	private final IntUnaryOperator limit;
	private final Runnable onChange;

	public UpgradeSlots(ItemStack upgrade, int size, BiPredicate<Integer, ItemStack> valid, IntUnaryOperator limit, Runnable onChange) {
		super(upgrade, ModCoreDataComponents.LENIENT_CONTAINER.get(), size);
		this.valid = valid;
		this.limit = limit;
		this.onChange = onChange;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return stack.isEmpty() || valid.test(slot, stack);
	}

	@Override
	public int getSlotLimit(int slot) {
		return limit.applyAsInt(slot);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		validateSlotIndex(slot);
		if (!ItemStack.matches(stack, stacks.get(slot))) {
			updateContents(stack, slot);
		}
	}

	@Override
	protected void onContentsChanged(int slot, ItemStack oldStack, ItemStack newStack) {
		onChange.run();
	}
}
