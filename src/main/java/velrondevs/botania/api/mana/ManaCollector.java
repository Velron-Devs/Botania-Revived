package velrondevs.botania.api.mana;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.internal.ManaBurst;

public interface ManaCollector extends ManaReceiver {

	void onClientDisplayTick();

	float getManaYieldMultiplier(ManaBurst burst);

	int getMaxMana();

}
