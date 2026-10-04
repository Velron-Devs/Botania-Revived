package velrondevs.botania.common.block.decor;

import net.minecraft.world.item.DyeColor;

import velrondevs.botania.common.block.BotaniaBlock;

public class PetalBlock extends BotaniaBlock {

	public final DyeColor color;

	public PetalBlock(DyeColor color, Properties builder) {
		super(builder);
		this.color = color;
	}
}
