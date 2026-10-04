package velrondevs.botania.api.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface ManaReceiver {
	Level getManaReceiverLevel();

	BlockPos getManaReceiverPos();

	int getCurrentMana();

	boolean isFull();

	void receiveMana(int mana);

	boolean canReceiveManaFromBursts();

}
