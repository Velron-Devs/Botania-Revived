package velrondevs.botania.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.block.block_entity.BifrostBlockEntity;
import velrondevs.botania.registry.BotaniaBlockEntities;
import velrondevs.botania.registry.BotaniaItems;

public class BifrostBlock extends PermanentBifrostBlock implements EntityBlock {
	public static final MapCodec<BifrostBlock> CODEC = simpleCodec(BifrostBlock::new);

	public BifrostBlock(Properties builder) {
		super(builder);
	}

	@Override
	protected MapCodec<? extends BifrostBlock> codec() {
		return CODEC;
	}

	@NotNull
	@Override
	public ItemStack getCloneItemStack(@NotNull LevelReader world, @NotNull BlockPos pos, @NotNull BlockState state) {
		return new ItemStack(BotaniaItems.rainbowRod);
	}

	@NotNull
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new BifrostBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (!level.isClientSide) {
			return BotaniaBlock.createTickerHelper(type, BotaniaBlockEntities.BIFROST, BifrostBlockEntity::serverTick);
		}
		return null;
	}
}
