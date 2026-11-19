package velrondevs.botania.capability;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import velrondevs.botania.api.block.PetalApothecary;
import velrondevs.botania.common.block.block_entity.PetalApothecaryBlockEntity;

public class ApothecaryFluidHandler implements ResourceHandler<FluidResource> {
	private record Snapshot(PetalApothecary.State full, PetalApothecary.State bufferFluid, int bufferAmount) {}

	private final PetalApothecaryBlockEntity apothecary;
	private PetalApothecary.State full;
	private PetalApothecary.State bufferFluid;
	private int bufferAmount;

	private final SnapshotJournal<Snapshot> journal = new SnapshotJournal<>() {
		@Override
		protected Snapshot createSnapshot() {
			return new Snapshot(full, bufferFluid, bufferAmount);
		}

		@Override
		protected void revertToSnapshot(Snapshot snapshot) {
			full = snapshot.full();
			bufferFluid = snapshot.bufferFluid();
			bufferAmount = snapshot.bufferAmount();
		}

		@Override
		protected void onRootCommit(Snapshot originalState) {
			Level level = apothecary.getLevel();
			if (level == null || level.isClientSide()) {
				return;
			}
			apothecary.setBuffer(bufferFluid, bufferAmount);
			if (full != apothecary.getFluid()) {
				apothecary.setFluid(full);
			}
		}
	};

	public ApothecaryFluidHandler(PetalApothecaryBlockEntity apothecary) {
		this.apothecary = apothecary;
		sync();
	}

	private void sync() {
		full = apothecary.getFluid();
		bufferFluid = apothecary.getBufferFluid();
		bufferAmount = apothecary.getBufferAmount();
	}

	private static PetalApothecary.State stateOf(Fluid fluid) {
		if (fluid == Fluids.WATER) {
			return PetalApothecary.State.WATER;
		}
		if (fluid == Fluids.LAVA) {
			return PetalApothecary.State.LAVA;
		}
		return PetalApothecary.State.EMPTY;
	}

	private static Fluid fluidOf(PetalApothecary.State state) {
		return switch (state) {
			case WATER -> Fluids.WATER;
			case LAVA -> Fluids.LAVA;
			default -> Fluids.EMPTY;
		};
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public FluidResource getResource(int index) {
		sync();
		Fluid fluid = fluidOf(full != PetalApothecary.State.EMPTY ? full : bufferFluid);
		return fluid == Fluids.EMPTY ? FluidResource.EMPTY : FluidResource.of(fluid);
	}

	@Override
	public long getAmountAsLong(int index) {
		sync();
		if (full != PetalApothecary.State.EMPTY) {
			return FluidType.BUCKET_VOLUME;
		}
		return bufferAmount;
	}

	@Override
	public long getCapacityAsLong(int index, FluidResource resource) {
		return FluidType.BUCKET_VOLUME;
	}

	@Override
	public boolean isValid(int index, FluidResource resource) {
		return stateOf(resource.getFluid()) != PetalApothecary.State.EMPTY;
	}

	@Override
	public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
		sync();
		PetalApothecary.State state = stateOf(resource.getFluid());
		if (state == PetalApothecary.State.EMPTY || amount <= 0 || full != PetalApothecary.State.EMPTY
				|| bufferAmount > 0 && bufferFluid != state) {
			return 0;
		}
		int accepted = Math.min(amount, FluidType.BUCKET_VOLUME - bufferAmount);
		if (accepted <= 0) {
			return 0;
		}
		journal.updateSnapshots(transaction);
		if (bufferAmount + accepted >= FluidType.BUCKET_VOLUME) {
			bufferFluid = PetalApothecary.State.EMPTY;
			bufferAmount = 0;
			full = state;
		} else {
			bufferFluid = state;
			bufferAmount += accepted;
		}
		return accepted;
	}

	@Override
	public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
		sync();
		if (resource.isEmpty() || amount <= 0) {
			return 0;
		}
		PetalApothecary.State state = stateOf(resource.getFluid());
		if (full != PetalApothecary.State.EMPTY) {
			if (state != full || amount < FluidType.BUCKET_VOLUME || !apothecary.isEmpty()) {
				return 0;
			}
			journal.updateSnapshots(transaction);
			full = PetalApothecary.State.EMPTY;
			return FluidType.BUCKET_VOLUME;
		}
		if (state != bufferFluid || bufferAmount <= 0) {
			return 0;
		}
		int drained = Math.min(amount, bufferAmount);
		journal.updateSnapshots(transaction);
		bufferAmount -= drained;
		if (bufferAmount == 0) {
			bufferFluid = PetalApothecary.State.EMPTY;
		}
		return drained;
	}
}
