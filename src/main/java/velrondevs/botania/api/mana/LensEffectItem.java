package velrondevs.botania.api.mana;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import velrondevs.botania.api.internal.ManaBurst;

public interface LensEffectItem {

	void apply(ItemStack stack, BurstProperties props, Level level);

	boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack);

	void updateBurst(ManaBurst burst, ItemStack stack);

	boolean doParticles(ManaBurst burst, ItemStack stack);

	default int getManaToTransfer(ManaBurst burst, ItemStack stack, ManaReceiver receiver) {
		return burst.getMana();
	}

}
