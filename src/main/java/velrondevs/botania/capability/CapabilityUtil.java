package velrondevs.botania.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CapabilityUtil {
	public static class WaterBowlFluidHandler implements IFluidHandlerItem {
		private ItemStack container;

		public WaterBowlFluidHandler(ItemStack stack) {
			this.container = stack;
		}

		private boolean isFull() {
			return !container.is(Items.BOWL);
		}

		@NotNull
		@Override
		public ItemStack getContainer() {
			return container;
		}

		@Override
		public int getTanks() {
			return 1;
		}

		@NotNull
		@Override
		public FluidStack getFluidInTank(int tank) {
			return isFull() ? new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME) : FluidStack.EMPTY;
		}

		@Override
		public int getTankCapacity(int tank) {
			return FluidType.BUCKET_VOLUME;
		}

		@Override
		public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
			return stack.getFluid() == Fluids.WATER;
		}

		@Override
		public int fill(FluidStack resource, FluidAction action) {
			return 0;
		}

		@NotNull
		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			if (resource.isEmpty() || resource.getFluid() != Fluids.WATER) {
				return FluidStack.EMPTY;
			}
			return drain(resource.getAmount(), action);
		}

		@NotNull
		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			if (container.getCount() != 1 || maxDrain < FluidType.BUCKET_VOLUME || !isFull()) {
				return FluidStack.EMPTY;
			}
			if (action.execute()) {
				container = new ItemStack(Items.BOWL);
			}
			return new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME);
		}
	}

	public static class ExtrapolatedBucketFluidHandler implements IFluidHandlerItem {
		private final ItemStack container;

		public ExtrapolatedBucketFluidHandler(ItemStack container) {
			this.container = container;
		}

		@NotNull
		@Override
		public ItemStack getContainer() {
			return container;
		}

		@Override
		public int getTanks() {
			return 1;
		}

		@NotNull
		@Override
		public FluidStack getFluidInTank(int tank) {
			return FluidStack.EMPTY;
		}

		@Override
		public int getTankCapacity(int tank) {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
			return true;
		}

		@Override
		public int fill(FluidStack resource, FluidAction action) {
			return Math.min(FluidType.BUCKET_VOLUME, resource.getAmount());
		}

		@NotNull
		@Override
		public FluidStack drain(FluidStack resource, FluidAction action) {
			return FluidStack.EMPTY;
		}

		@NotNull
		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			return FluidStack.EMPTY;
		}
	}

	@Nullable
	public static <T> T findCapability(BlockCapability<T, Void> capability, Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be) {
		return level.getCapability(capability, pos, state, be, null);
	}

	@Nullable
	public static <T> T findCapability(BlockCapability<T, Direction> capability, Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be, @Nullable Direction direction) {
		return level.getCapability(capability, pos, state, be, direction);
	}

	private CapabilityUtil() {}
}
