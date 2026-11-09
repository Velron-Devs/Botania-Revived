package velrondevs.botania.compat.sophisticated;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.p3pp3rf1y.sophisticatedcore.init.ModCoreDataComponents;
import net.p3pp3rf1y.sophisticatedcore.inventory.ComponentItemStacksHandler;
import net.p3pp3rf1y.sophisticatedcore.inventory.ISlotStackAccessor;

import java.util.function.BiPredicate;
import java.util.function.IntUnaryOperator;

public class UpgradeSlots extends ComponentItemStacksHandler implements ISlotStackAccessor {
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
	public boolean isValid(int index, ItemResource resource) {
		return resource.isEmpty() || valid.test(index, resource.toStack());
	}

	@Override
	protected int getCapacity(int index, ItemResource resource) {
		int max = limit.applyAsInt(index);
		return resource.isEmpty() ? max : Math.min(max, resource.getMaxStackSize());
	}

	@Override
	protected void onContentsChanged(int index, ItemStack previousContents) {
		super.onContentsChanged(index, previousContents);
		onChange.run();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return super.getStackInSlot(slot);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		set(slot, ItemResource.of(stack), stack.getCount());
	}

	public int getSlots() {
		return size();
	}

	public boolean isItemValid(int slot, ItemStack stack) {
		return isValid(slot, ItemResource.of(stack));
	}

	public int getSlotLimit(int slot) {
		return limit.applyAsInt(slot);
	}
}
