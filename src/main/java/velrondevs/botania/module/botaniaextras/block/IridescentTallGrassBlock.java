package velrondevs.botania.module.botaniaextras.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.DoublePlantBlock;

import org.jetbrains.annotations.Nullable;

public class IridescentTallGrassBlock extends DoublePlantBlock {
	public static final MapCodec<IridescentTallGrassBlock> CODEC = simpleCodec(p -> new IridescentTallGrassBlock(null, p));

	@Nullable
	private final DyeColor color;

	public IridescentTallGrassBlock(@Nullable DyeColor color, Properties properties) {
		super(properties);
		this.color = color;
	}

	@Nullable
	public DyeColor getColor() {
		return color;
	}

	@Override
	public MapCodec<? extends DoublePlantBlock> codec() {
		return CODEC;
	}
}
