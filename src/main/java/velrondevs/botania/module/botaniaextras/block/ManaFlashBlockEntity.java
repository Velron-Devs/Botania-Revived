package velrondevs.botania.module.botaniaextras.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.common.block.block_entity.BotaniaBlockEntity;

public class ManaFlashBlockEntity extends BotaniaBlockEntity {
	private static final String TAG_COLOR = "color";
	private static final String TAG_INVISIBLE = "invisible";

	private int color = 0x20FF20;
	private boolean invisible;

	public ManaFlashBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
		setChanged();
	}

	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
		setChanged();
	}

	@Override
	public void writePacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		cmp.putInt(TAG_COLOR, color);
		cmp.putBoolean(TAG_INVISIBLE, invisible);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		color = cmp.getInt(TAG_COLOR);
		invisible = cmp.getBoolean(TAG_INVISIBLE);
	}
}
