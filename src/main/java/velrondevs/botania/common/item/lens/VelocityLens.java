package velrondevs.botania.common.item.lens;

import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.mana.BurstProperties;

public class VelocityLens extends Lens {

	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		props.motionModifier *= 2F;
		props.maxMana *= 0.75F;
		props.ticksBeforeManaLoss /= 3F;
		props.manaLossPerTick *= 2F;
	}

}
