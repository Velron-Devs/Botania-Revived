package velrondevs.botania.common.internal_caps;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class NarslimmusComponent extends SerializableComponent {
	public static final String TAG_WORLD_SPAWNED = "botania:world_spawned";
	private boolean naturalSpawned = false;

	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider registries) {
		naturalSpawned = tag.getBoolean(TAG_WORLD_SPAWNED);
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider registries) {
		tag.putBoolean(TAG_WORLD_SPAWNED, naturalSpawned);
	}

	public boolean isNaturalSpawned() {
		return naturalSpawned;
	}

	public void setNaturalSpawn(boolean naturalSpawned) {
		this.naturalSpawned = naturalSpawned;
	}
}
