package velrondevs.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.registry.BotaniaBlockEntities;

public class GaiaHeadBlockEntity extends SkullBlockEntity {
	public GaiaHeadBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@NotNull
	@Override
	public BlockEntityType<GaiaHeadBlockEntity> getType() {
		return BotaniaBlockEntities.GAIA_HEAD;
	}

}
