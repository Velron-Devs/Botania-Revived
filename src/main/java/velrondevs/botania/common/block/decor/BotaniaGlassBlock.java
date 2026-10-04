package velrondevs.botania.common.block.decor;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.level.block.TransparentBlock;

public class BotaniaGlassBlock extends TransparentBlock {
	public static final MapCodec<BotaniaGlassBlock> CODEC = simpleCodec(BotaniaGlassBlock::new);

	public BotaniaGlassBlock(Properties props) {
		super(props);
	}

	@Override
	protected MapCodec<? extends BotaniaGlassBlock> codec() {
		return CODEC;
	}
}
