package velrondevs.botania.api.block;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public interface PetalApothecary {
	enum State implements StringRepresentable {
		EMPTY,
		WATER,
		LAVA;

		@NotNull
		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}

		public Fluid asVanilla() {
			return switch (this) {
				case EMPTY -> Fluids.EMPTY;
				case WATER -> Fluids.WATER;
				case LAVA -> Fluids.LAVA;
			};
		}
	}

	void setFluid(State fluid);

	State getFluid();

	default BlockEntity blockEntity() {
		return (BlockEntity) this;
	}
}
