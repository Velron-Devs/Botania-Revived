package velrondevs.botania.integration.jade;

import net.minecraft.resources.Identifier;

import velrondevs.botania.api.BotaniaAPI;

public final class BotaniaJadeIds {
	private BotaniaJadeIds() {}

	public static final Identifier ENABLED = id("enabled");
	public static final Identifier SHOW_NUMBERS = id("show_numbers");
	public static final Identifier COMPACT_NUMBERS = id("compact_numbers");
	public static final Identifier NATIVE_COLORS = id("native_colors");
	public static final Identifier MANA_BAR_COLOR = id("mana_bar_color");
	public static final Identifier PROGRESS_BAR_COLOR = id("progress_bar_color");
	public static final Identifier SHOW_BINDING = id("show_binding");

	public static final Identifier MANA_POOL = id("mana_pool");
	public static final Identifier MANA_POOL_RECIPE = id("mana_pool.recipe");
	public static final Identifier MANA_SPREADER = id("mana_spreader");
	public static final Identifier GENERATING_FLOWER = id("generating_flower");
	public static final Identifier GENERATING_FLOWER_TIMERS = id("generating_flower.timers");
	public static final Identifier FUNCTIONAL_FLOWER = id("functional_flower");
	public static final Identifier RUNIC_ALTAR = id("runic_altar");
	public static final Identifier TERRA_PLATE = id("terra_plate");
	public static final Identifier PETAL_APOTHECARY = id("petal_apothecary");
	public static final Identifier BREWERY = id("brewery");
	public static final Identifier MANA_ENCHANTER = id("mana_enchanter");
	public static final Identifier ALFHEIM_PORTAL = id("alfheim_portal");
	public static final Identifier MANA_STORAGE = id("mana_storage");
	public static final Identifier MANA_SPARK = id("mana_spark");

	private static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(BotaniaAPI.MODID, path);
	}
}
