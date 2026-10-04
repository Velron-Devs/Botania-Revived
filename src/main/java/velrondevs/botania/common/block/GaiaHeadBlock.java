package velrondevs.botania.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.block.block_entity.GaiaHeadBlockEntity;

public class GaiaHeadBlock extends SkullBlock {
	public static final SkullBlock.Type GAIA_TYPE = registerType(new SkullBlock.Type() {
		@NotNull
		@Override
		public String getSerializedName() {
			return "botania:gaia";
		}
	});
	public static final MapCodec<GaiaHeadBlock> CODEC = simpleCodec(GaiaHeadBlock::new);

	private static SkullBlock.Type registerType(SkullBlock.Type type) {
		SkullBlock.Type.TYPES.put(type.getSerializedName(), type);
		return type;
	}

	public GaiaHeadBlock(Properties builder) {
		super(GAIA_TYPE, builder);
	}

	@NotNull
	@Override
	public MapCodec<? extends GaiaHeadBlock> codec() {
		return CODEC;
	}

	@NotNull
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GaiaHeadBlockEntity(pos, state);
	}
}
