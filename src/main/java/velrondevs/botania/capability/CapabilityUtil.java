package velrondevs.botania.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.transfer.ItemAccessResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import org.jetbrains.annotations.Nullable;

public final class CapabilityUtil {
	public static class WaterBowlFluidHandler extends ItemAccessResourceHandler<FluidResource> {
		public WaterBowlFluidHandler(ItemAccess access) {
			super(access, 1);
		}

		@Override
		protected FluidResource getResourceFrom(ItemResource accessResource, int index) {
			return accessResource.isEmpty() || accessResource.is(Items.BOWL) ? FluidResource.EMPTY : FluidResource.of(Fluids.WATER);
		}

		@Override
		protected int getAmountFrom(ItemResource accessResource, int index) {
			return getResourceFrom(accessResource, index).isEmpty() ? 0 : FluidType.BUCKET_VOLUME;
		}

		@Override
		protected ItemResource update(ItemResource accessResource, int index, FluidResource newResource, int newAmount) {
			if (newAmount == 0) {
				return ItemResource.of(Items.BOWL);
			}
			return ItemResource.EMPTY;
		}

		@Override
		public boolean isValid(int index, FluidResource resource) {
			return resource.getFluid() == Fluids.WATER;
		}

		@Override
		protected int getCapacity(int index, FluidResource resource) {
			return FluidType.BUCKET_VOLUME;
		}
	}

	public static class ExtrapolatedBucketFluidHandler implements ResourceHandler<FluidResource> {
		public ExtrapolatedBucketFluidHandler(ItemAccess access) {}

		@Override
		public int size() {
			return 1;
		}

		@Override
		public FluidResource getResource(int index) {
			return FluidResource.EMPTY;
		}

		@Override
		public long getAmountAsLong(int index) {
			return 0;
		}

		@Override
		public long getCapacityAsLong(int index, FluidResource resource) {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isValid(int index, FluidResource resource) {
			return true;
		}

		@Override
		public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
			return Math.min(FluidType.BUCKET_VOLUME, amount);
		}

		@Override
		public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
			return 0;
		}
	}

	@Nullable
	public static IItemHandler findItemHandler(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity be, @Nullable Direction side) {
		ResourceHandler<ItemResource> handler = level.getCapability(Capabilities.Item.BLOCK, pos, state, be, side);
		return handler == null ? null : IItemHandler.of(handler);
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
