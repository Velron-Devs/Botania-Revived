package velrondevs.botania.api.mana;

import net.minecraft.world.item.DyeColor;

import velrondevs.botania.api.BotaniaAPI;

import java.util.Optional;

public interface ManaPool extends ManaReceiver {

	boolean isOutputtingPower();

	int getMaxMana();

	Optional<DyeColor> getColor();

	void setColor(Optional<DyeColor> color);

}
