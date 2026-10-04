package velrondevs.botania.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.block.block_entity.GaiaHeadBlockEntity;

public class WallGaiaHeadBlock extends WallSkullBlock {
	public static final MapCodec<WallGaiaHeadBlock> CODEC = simpleCodec(WallGaiaHeadBlock::new);

	public WallGaiaHeadBlock(Properties builder) {
		super(GaiaHeadBlock.GAIA_TYPE, builder);
	}

	@NotNull
	@Override
	public MapCodec<? extends WallGaiaHeadBlock> codec() {
		return CODEC;
	}

	@NotNull
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GaiaHeadBlockEntity(pos, state);
	}
}
