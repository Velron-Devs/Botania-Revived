package velrondevs.botania.common.block.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.block.BotaniaBlock;
import velrondevs.botania.common.block.block_entity.mana.PowerGeneratorBlockEntity;
import velrondevs.botania.registry.BotaniaBlockEntities;

public class PowerGeneratorBlock extends BotaniaBlock implements EntityBlock {

	public PowerGeneratorBlock(Properties builder) {
		super(builder);
	}

	@NotNull
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new PowerGeneratorBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (!level.isClientSide) {
			return createTickerHelper(type, BotaniaBlockEntities.FLUXFIELD, PowerGeneratorBlockEntity::serverTick);
		}
		return null;
	}
}
