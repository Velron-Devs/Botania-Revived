package velrondevs.botania.common.item.lens;

import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.mana.BurstProperties;

public class ResistanceLens extends Lens {

	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		props.ticksBeforeManaLoss *= 2.25F;
		props.motionModifier *= 0.8F;
	}

}
