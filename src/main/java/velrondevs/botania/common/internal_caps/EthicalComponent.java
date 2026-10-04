package velrondevs.botania.common.internal_caps;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.PrimedTnt;

import velrondevs.botania.common.helper.EthicalTntHelper;

public class EthicalComponent extends SerializableComponent {
	protected static final String TAG_UNETHICAL = "botania:unethical";
	protected boolean unethical;

	public EthicalComponent(PrimedTnt entity) {
		if (!entity.level().isClientSide()) {
			EthicalTntHelper.addTrackedTntEntity(entity);
		}
	}

	public final boolean isUnethical() {
		return unethical;
	}

	public final void markUnethical() {
		unethical = true;
	}

	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider registries) {
		unethical = tag.getBoolean(TAG_UNETHICAL);
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider registries) {
		tag.putBoolean(TAG_UNETHICAL, unethical);
	}
}
