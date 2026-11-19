package velrondevs.botania.capability;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.block.PetalApothecary;
import velrondevs.botania.common.block.block_entity.PetalApothecaryBlockEntity;

/**
 * Exposes the Petal Apothecary as a one-bucket tank so pipes and pumps can fill it with water or lava.
 * Pipes usually move a few mB per tick, so partial amounts are buffered until they add up to a full bucket.
 */
public class ApothecaryFluidHandler implements IFluidHandler {
	private final PetalApothecaryBlockEntity apothecary;

	public ApothecaryFluidHandler(PetalApothecaryBlockEntity apothecary) {
		this.apothecary = apothecary;
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
	public int getTanks() {
		return 1;
	}

	@NotNull
	@Override
	public FluidStack getFluidInTank(int tank) {
		Fluid fluid = fluidOf(apothecary.getFluid());
		if (fluid != Fluids.EMPTY) {
			return new FluidStack(fluid, FluidType.BUCKET_VOLUME);
		}
		fluid = fluidOf(apothecary.getBufferFluid());
		return fluid == Fluids.EMPTY ? FluidStack.EMPTY : new FluidStack(fluid, apothecary.getBufferAmount());
	}

	@Override
	public int getTankCapacity(int tank) {
		return FluidType.BUCKET_VOLUME;
	}

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
		return stateOf(stack.getFluid()) != PetalApothecary.State.EMPTY;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		Level level = apothecary.getLevel();
		PetalApothecary.State state = stateOf(resource.getFluid());
		if (level == null || state == PetalApothecary.State.EMPTY || resource.isEmpty()
				|| apothecary.getFluid() != PetalApothecary.State.EMPTY) {
			return 0;
		}
		int stored = apothecary.getBufferAmount();
		if (stored > 0 && apothecary.getBufferFluid() != state) {
			return 0;
		}
		int accepted = Math.min(resource.getAmount(), FluidType.BUCKET_VOLUME - stored);
		if (accepted > 0 && action.execute() && !level.isClientSide) {
			if (stored + accepted >= FluidType.BUCKET_VOLUME) {
				apothecary.setBuffer(PetalApothecary.State.EMPTY, 0);
				apothecary.setFluid(state);
			} else {
				apothecary.setBuffer(state, stored + accepted);
			}
		}
		return accepted;
	}

	@NotNull
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (resource.isEmpty()) {
			return FluidStack.EMPTY;
		}
		PetalApothecary.State held = apothecary.getFluid() != PetalApothecary.State.EMPTY ? apothecary.getFluid() : apothecary.getBufferFluid();
		if (stateOf(resource.getFluid()) != held) {
			return FluidStack.EMPTY;
		}
		return drain(resource.getAmount(), action);
	}

	@NotNull
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		Level level = apothecary.getLevel();
		if (level == null || maxDrain <= 0) {
			return FluidStack.EMPTY;
		}
		Fluid fluid = fluidOf(apothecary.getFluid());
		if (fluid != Fluids.EMPTY) {
			// Never drain while petals are inside, so a pipe can't wipe a half-made recipe
			if (maxDrain < FluidType.BUCKET_VOLUME || !apothecary.isEmpty()) {
				return FluidStack.EMPTY;
			}
			if (action.execute() && !level.isClientSide) {
				apothecary.setFluid(PetalApothecary.State.EMPTY);
			}
			return new FluidStack(fluid, FluidType.BUCKET_VOLUME);
		}
		fluid = fluidOf(apothecary.getBufferFluid());
		int stored = apothecary.getBufferAmount();
		if (fluid == Fluids.EMPTY || stored <= 0) {
			return FluidStack.EMPTY;
		}
		int drained = Math.min(maxDrain, stored);
		if (action.execute() && !level.isClientSide) {
			apothecary.setBuffer(apothecary.getBufferFluid(), stored - drained);
		}
		return new FluidStack(fluid, drained);
	}
}
