package velrondevs.botania.module.botaniaextras.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.module.botaniaextras.item.FrozenStarItem;

public class FrozenStarBlock extends BaseEntityBlock {
	public static final MapCodec<FrozenStarBlock> CODEC = simpleCodec(FrozenStarBlock::new);
	private static final VoxelShape SHAPE = Block.box(4, 4, 4, 12, 12, 12);

	public FrozenStarBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FrozenStarBlockEntity(pos, state);
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide && !player.getAbilities().instabuild
				&& level.getBlockEntity(pos) instanceof FrozenStarBlockEntity star) {
			ItemStack stack = FrozenStarItem.forColor(star.getColor());
			FrozenStarItem.setSize(stack, star.getSize());
			ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
			item.setDeltaMovement(level.random.nextGaussian() * 0.05, level.random.nextGaussian() * 0.05 + 0.2, level.random.nextGaussian() * 0.05);
			item.setDefaultPickUpDelay();
			level.addFreshEntity(item);
		}
		return super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
		if (level.getBlockEntity(pos) instanceof FrozenStarBlockEntity star) {
			return FrozenStarItem.forColor(star.getColor());
		}
		return super.getCloneItemStack(level, pos, state);
	}
}
