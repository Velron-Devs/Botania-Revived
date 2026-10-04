package velrondevs.botania.common.internal_caps;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import org.jetbrains.annotations.NotNull;

public abstract class SerializableComponent {
	public abstract void readFromNbt(CompoundTag tag, HolderLookup.Provider registries);

	public abstract void writeToNbt(CompoundTag tag, HolderLookup.Provider registries);

	@NotNull
	public final CompoundTag serializeNBT(HolderLookup.Provider registries) {
		var ret = new CompoundTag();
		writeToNbt(ret, registries);
		return ret;
	}

	public final void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt) {
		readFromNbt(nbt, registries);
	}
}
