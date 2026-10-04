package velrondevs.botania.common.block.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.block.BotaniaBlock;
import velrondevs.botania.common.block.OpenCrateBlock;
import velrondevs.botania.common.block.block_entity.mana.SpreaderTurntableBlockEntity;
import velrondevs.botania.registry.BotaniaBlockEntities;

public class SpreaderTurntableBlock extends BotaniaBlock implements EntityBlock {

	public SpreaderTurntableBlock(Properties builder) {
		super(builder);
	}

	@NotNull
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new SpreaderTurntableBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, BotaniaBlockEntities.TURNTABLE, SpreaderTurntableBlockEntity::commonTick);
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		if (world.hasNeighborSignal(pos) && rand.nextDouble() < 0.2) {
			OpenCrateBlock.redstoneParticlesOnFullBlock(world, pos, rand);
		}
	}
}
