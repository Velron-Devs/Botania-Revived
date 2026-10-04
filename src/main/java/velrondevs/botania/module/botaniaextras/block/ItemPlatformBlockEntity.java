package velrondevs.botania.module.botaniaextras.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.api.internal.VanillaPacketDispatcher;
import velrondevs.botania.common.block.block_entity.ExposedSimpleInventoryBlockEntity;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlockEntities;

public class ItemPlatformBlockEntity extends ExposedSimpleInventoryBlockEntity {
	public ItemPlatformBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaExtrasBlockEntities.ITEM_PLATFORM, pos, state);
	}

	public ItemStack getDisplayed() {
		return getItemHandler().getItem(0);
	}

	@Override
	protected SimpleContainer createItemHandler() {
		return new SimpleContainer(1) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}
		};
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (level != null && !level.isClientSide) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
		}
	}
}
