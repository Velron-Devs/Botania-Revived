package velrondevs.botania.common.internal_caps;

import velrondevs.botania.common.helper.ItemNBTHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class LooniumComponent extends SerializableComponent {
	protected static final String TAG_TO_DROP = "toDrop";
	protected static final String TAG_OVERRIDE_DROP = "overrideDrop";
	protected static final String TAG_SLOW_DESPAWN = "slowDespawn";
	private ItemStack toDrop = ItemStack.EMPTY;
	private boolean overrideDrop;
	private boolean slowDespawn;

	public ItemStack getDrop() {
		return toDrop;
	}

	public void setDrop(ItemStack stack) {
		this.toDrop = stack;
	}

	public boolean isOverrideDrop() {
		return overrideDrop;
	}

	public void setOverrideDrop(boolean overrideDrop) {
		this.overrideDrop = overrideDrop;
	}

	public boolean isSlowDespawn() {
		return slowDespawn;
	}

	public void setSlowDespawn(boolean slowDespawn) {
		this.slowDespawn = slowDespawn;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		return obj instanceof LooniumComponent component && ItemStack.matches(component.toDrop, toDrop)
				&& component.overrideDrop == overrideDrop && component.slowDespawn == slowDespawn;
	}

	@Override
	public int hashCode() {
		return Objects.hash(toDrop.hashCode(), overrideDrop, slowDespawn);
	}

	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider registries) {
		if (tag.contains(TAG_TO_DROP)) {
			setDrop(ItemNBTHelper.parseStack(registries, tag.get(TAG_TO_DROP)));
		} else {
			setDrop(ItemStack.EMPTY);
		}
		if (tag.contains(TAG_OVERRIDE_DROP)) {
			setOverrideDrop(tag.getBooleanOr(TAG_OVERRIDE_DROP, false));
		}
		if (tag.contains(TAG_SLOW_DESPAWN)) {
			setSlowDespawn(tag.getBooleanOr(TAG_SLOW_DESPAWN, false));
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider registries) {
		if (isOverrideDrop()) {
			if (!getDrop().isEmpty()) {
				tag.put(TAG_TO_DROP, ItemNBTHelper.saveStack(registries, getDrop()));
			}
			tag.putBoolean(TAG_OVERRIDE_DROP, true);
		}
		if (isSlowDespawn()) {
			tag.putBoolean(TAG_SLOW_DESPAWN, true);
		}
	}
}
