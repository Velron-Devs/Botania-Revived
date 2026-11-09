package velrondevs.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import velrondevs.botania.api.internal.ValueIOHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BotaniaBlockEntity extends BlockEntity {
	public BotaniaBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public static CompoundTag readTag(ValueInput input) {
		return ValueIOHelper.readAll(input);
	}

	protected HolderLookup.Provider registries() {
		return ValueIOHelper.registries(level);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		CompoundTag tag = new CompoundTag();
		saveAdditional(tag, registries());
		ValueIOHelper.writeAll(output, tag);
	}

	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		writePacketNBT(tag, registries);
	}

	@NotNull
	@Override
	public final CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var tag = new CompoundTag();
		writePacketNBT(tag, registries);
		return tag;
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		loadAdditional(readTag(input), input.lookup());
	}

	public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.Provider registries) {
		readPacketNBT(tag, registries);
	}

	public void writePacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {}

	public void readPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
