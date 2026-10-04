package velrondevs.botania.common.internal_caps;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import velrondevs.botania.common.block.SpectralRailBlock;

public class SpectralRailComponent extends SerializableComponent {
	public int floatTicks = 0;

	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider registries) {
		floatTicks = tag.getInt(SpectralRailBlock.TAG_FLOAT_TICKS);
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider registries) {
		tag.putInt(SpectralRailBlock.TAG_FLOAT_TICKS, floatTicks);
	}
}
