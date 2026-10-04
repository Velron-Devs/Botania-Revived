package velrondevs.botania.common.block.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.block.BotaniaWaterloggedBlock;
import velrondevs.botania.common.block.block_entity.corporea.BaseCorporeaBlockEntity;
import velrondevs.botania.common.block.block_entity.corporea.CorporeaCrystalCubeBlockEntity;
import velrondevs.botania.common.item.WandOfTheForestItem;
import velrondevs.botania.registry.BotaniaBlockEntities;

public class CorporeaCrystalCubeBlock extends BotaniaWaterloggedBlock implements EntityBlock {

	private static final VoxelShape SHAPE = box(3.0, 0, 3.0, 13.0, 16, 13.0);

	public CorporeaCrystalCubeBlock(BlockBehaviour.Properties builder) {
		super(builder);
	}

	@Override
	public void attack(BlockState state, Level world, BlockPos pos, Player player) {
		if (!world.isClientSide && world.getBlockEntity(pos) instanceof CorporeaCrystalCubeBlockEntity cube) {
			cube.doRequest(player);
		}
	}

	@NotNull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!stack.isEmpty()) {
			if (stack.getItem() instanceof WandOfTheForestItem && player.isSecondaryUseActive()) {
				return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
			}
			if (!(world.getBlockEntity(pos) instanceof CorporeaCrystalCubeBlockEntity cube)) {
				return ItemInteractionResult.FAIL;
			}
			if (cube.locked) {
				if (!world.isClientSide) {
					player.displayClientMessage(Component.translatable("botaniamisc.crystalCubeLocked"), false);
				}
			} else {
				cube.setRequestTarget(stack);
			}
			return ItemInteractionResult.sidedSuccess(world.isClientSide());
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@NotNull
	@Override
	public BaseCorporeaBlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new CorporeaCrystalCubeBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
		if (!level.isClientSide) {
			return createTickerHelper(type, BotaniaBlockEntities.CORPOREA_CRYSTAL_CUBE, CorporeaCrystalCubeBlockEntity::serverTick);
		}
		return null;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return world.getBlockEntity(pos) instanceof CorporeaCrystalCubeBlockEntity cube
				? cube.getComparatorValue()
				: 0;
	}
}
