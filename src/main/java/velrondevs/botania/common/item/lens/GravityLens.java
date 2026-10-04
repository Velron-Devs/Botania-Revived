package velrondevs.botania.common.item.lens;

import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.mana.BurstProperties;

public class GravityLens extends Lens {

	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		props.gravity = 0.0015F;
		props.ticksBeforeManaLoss *= 1.2F;
	}

}
