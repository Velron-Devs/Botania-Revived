package velrondevs.botania.common.item.lens;

import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.mana.BurstProperties;

public class EfficiencyLens extends Lens {

	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		props.manaLossPerTick /= 5F;
		props.ticksBeforeManaLoss *= 1.1F;
	}

}
