package velrondevs.botania.common.item.lens;

import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.mana.BurstProperties;

public class PotencyLens extends Lens {

	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		props.maxMana *= 2;
		props.motionModifier *= 0.85F;
		props.manaLossPerTick *= 2F;
	}

}
