package velrondevs.botania.module.botaniaextras.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemPlatformBlock extends BaseEntityBlock {
	public static final MapCodec<ItemPlatformBlock> CODEC = simpleCodec(ItemPlatformBlock::new);
	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 8, 16);

	public ItemPlatformBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!(level.getBlockEntity(pos) instanceof ItemPlatformBlockEntity platform)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
		ItemStack held = platform.getItemHandler().getItem(0);
		if (!held.isEmpty()) {
			return take(level, pos, player, platform, held) ? ItemInteractionResult.sidedSuccess(level.isClientSide) : ItemInteractionResult.FAIL;
		}
		if (stack.isEmpty()) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
		if (!level.isClientSide) {
			platform.getItemHandler().setItem(0, stack.copyWithCount(1));
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		}
		return ItemInteractionResult.sidedSuccess(level.isClientSide);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (level.getBlockEntity(pos) instanceof ItemPlatformBlockEntity platform) {
			ItemStack held = platform.getItemHandler().getItem(0);
			if (!held.isEmpty() && take(level, pos, player, platform, held)) {
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
		}
		return InteractionResult.PASS;
	}

	private static boolean take(Level level, BlockPos pos, Player player, ItemPlatformBlockEntity platform, ItemStack held) {
		if (!level.isClientSide) {
			player.getInventory().placeItemBackInInventory(held.copy());
			platform.getItemHandler().setItem(0, ItemStack.EMPTY);
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		}
		return true;
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof ItemPlatformBlockEntity platform) {
			return AbstractContainerMenu.getRedstoneSignalFromContainer(platform.getItemHandler());
		}
		return 0;
	}

	@Override
	protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof ItemPlatformBlockEntity platform) {
			Containers.dropContents(level, pos, platform.getItemHandler());
			level.updateNeighbourForOutputSignal(pos, this);
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ItemPlatformBlockEntity(pos, state);
	}
}
