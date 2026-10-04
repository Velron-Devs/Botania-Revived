package velrondevs.botania.common.internal_caps;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class TigerseyeComponent extends SerializableComponent {
	private static final String TAG_PACIFIED = "botania:tigerseye_pacified";
	private boolean pacified = false;

	public boolean isPacified() {
		return pacified;
	}

	public void setPacified() {
		this.pacified = true;
	}

	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider registries) {
		this.pacified = tag.getBoolean(TAG_PACIFIED);
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider registries) {
		if (pacified) {
			tag.putBoolean(TAG_PACIFIED, true);
		}
	}
}
