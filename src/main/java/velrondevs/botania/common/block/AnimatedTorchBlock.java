package velrondevs.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.block.block_entity.AnimatedTorchBlockEntity;
import velrondevs.botania.registry.BotaniaBlockEntities;

public class AnimatedTorchBlock extends BotaniaWaterloggedBlock implements EntityBlock {

	private static final VoxelShape SHAPE = box(0, 0, 0, 16, 4, 16);

	public AnimatedTorchBlock(Properties builder) {
		super(builder);
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult hit) {
		if (playerIn.isSecondaryUseActive() && worldIn.getBlockEntity(pos) instanceof AnimatedTorchBlockEntity torch) {
			torch.handRotate();
			return ItemInteractionResult.sidedSuccess(worldIn.isClientSide());
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		if (world.getBlockEntity(pos) instanceof AnimatedTorchBlockEntity torch) {
			torch.onPlace(entity);
		}
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
		return getSignal(state, level, pos, side);
	}

	@Override
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
		if (!(level.getBlockEntity(pos) instanceof AnimatedTorchBlockEntity torch)) {
			return 0;
		}

		if (torch.rotating || !torch.directionInitialized) {
			return 0;
		}

		if (AnimatedTorchBlockEntity.SIDES[torch.side] == side) {
			return 15;
		}

		return 0;
	}

	@NotNull
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@NotNull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new AnimatedTorchBlockEntity(pos, state);
	}

	@org.jetbrains.annotations.Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, BotaniaBlockEntities.ANIMATED_TORCH, AnimatedTorchBlockEntity::commonTick);
	}

	@Override
	public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {

		world.blockUpdated(pos, this);
		if (world instanceof ServerLevel level) {
			for (Direction e : AnimatedTorchBlockEntity.SIDES) {
				level.updateNeighborsAtExceptFromFacing(pos.relative(e), state.getBlock(), e.getOpposite());
			}
		}
		super.destroy(world, pos, state);
	}

}
