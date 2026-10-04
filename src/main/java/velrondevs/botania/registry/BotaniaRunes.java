package velrondevs.botania.registry;

import velrondevs.botania.common.lib.LibItemNames;
import velrondevs.botania.event.BotaniaEvents;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaRunes {
	public static void registerDefaults(BotaniaEvents.RegisterRunes event) {
		event.register(prefix(LibItemNames.RUNE_WATER), BotaniaItems.runeWater);
		event.register(prefix(LibItemNames.RUNE_FIRE), BotaniaItems.runeFire);
		event.register(prefix(LibItemNames.RUNE_EARTH), BotaniaItems.runeEarth);
		event.register(prefix(LibItemNames.RUNE_AIR), BotaniaItems.runeAir);
		event.register(prefix(LibItemNames.RUNE_SPRING), BotaniaItems.runeSpring);
		event.register(prefix(LibItemNames.RUNE_SUMMER), BotaniaItems.runeSummer);
		event.register(prefix(LibItemNames.RUNE_AUTUMN), BotaniaItems.runeAutumn);
		event.register(prefix(LibItemNames.RUNE_WINTER), BotaniaItems.runeWinter);
		event.register(prefix(LibItemNames.RUNE_MANA), BotaniaItems.runeMana);
		event.register(prefix(LibItemNames.RUNE_LUST), BotaniaItems.runeLust);
		event.register(prefix(LibItemNames.RUNE_GLUTTONY), BotaniaItems.runeGluttony);
		event.register(prefix(LibItemNames.RUNE_GREED), BotaniaItems.runeGreed);
		event.register(prefix(LibItemNames.RUNE_SLOTH), BotaniaItems.runeSloth);
		event.register(prefix(LibItemNames.RUNE_WRATH), BotaniaItems.runeWrath);
		event.register(prefix(LibItemNames.RUNE_ENVY), BotaniaItems.runeEnvy);
		event.register(prefix(LibItemNames.RUNE_PRIDE), BotaniaItems.runePride);
	}

	private BotaniaRunes() {}
}
