package velrondevs.botania.module.botaniaextras.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.common.block.block_entity.BotaniaBlockEntity;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlockEntities;
import velrondevs.botania.module.botaniaextras.item.FrozenStarItem;

public class FrozenStarBlockEntity extends BotaniaBlockEntity {
	private static final String TAG_COLOR = "color";
	private static final String TAG_SIZE = "size";

	private int color = FrozenStarItem.RAINBOW;
	private float size = FrozenStarItem.DEFAULT_SIZE;

	public FrozenStarBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaExtrasBlockEntities.FROZEN_STAR, pos, state);
	}

	public int getColor() {
		return color;
	}

	public float getSize() {
		return size;
	}

	public void setStar(int color, float size) {
		this.color = color;
		this.size = size;
		setChanged();
		if (level != null && !level.isClientSide) {
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
		}
	}

	@Override
	public void writePacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		cmp.putInt(TAG_COLOR, color);
		cmp.putFloat(TAG_SIZE, size);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		color = cmp.getInt(TAG_COLOR);
		size = cmp.contains(TAG_SIZE) ? cmp.getFloat(TAG_SIZE) : FrozenStarItem.DEFAULT_SIZE;
	}
}
