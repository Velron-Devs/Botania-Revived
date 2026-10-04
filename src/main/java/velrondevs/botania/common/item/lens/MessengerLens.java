package velrondevs.botania.common.item.lens;

import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.mana.BurstProperties;

public class MessengerLens extends Lens {

	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		props.maxMana /= 5;
		props.ticksBeforeManaLoss *= 3;
		props.motionModifier *= 3F;
	}

}
