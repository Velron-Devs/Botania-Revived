package velrondevs.botania.integration.jade;

import net.minecraft.resources.ResourceLocation;

import velrondevs.botania.api.BotaniaAPI;

public final class BotaniaJadeIds {
	private BotaniaJadeIds() {}

	public static final ResourceLocation ENABLED = id("enabled");
	public static final ResourceLocation SHOW_NUMBERS = id("show_numbers");
	public static final ResourceLocation COMPACT_NUMBERS = id("compact_numbers");
	public static final ResourceLocation NATIVE_COLORS = id("native_colors");
	public static final ResourceLocation MANA_BAR_COLOR = id("mana_bar_color");
	public static final ResourceLocation PROGRESS_BAR_COLOR = id("progress_bar_color");
	public static final ResourceLocation SHOW_BINDING = id("show_binding");

	public static final ResourceLocation MANA_POOL = id("mana_pool");
	public static final ResourceLocation MANA_POOL_RECIPE = id("mana_pool.recipe");
	public static final ResourceLocation MANA_SPREADER = id("mana_spreader");
	public static final ResourceLocation GENERATING_FLOWER = id("generating_flower");
	public static final ResourceLocation GENERATING_FLOWER_TIMERS = id("generating_flower.timers");
	public static final ResourceLocation FUNCTIONAL_FLOWER = id("functional_flower");
	public static final ResourceLocation RUNIC_ALTAR = id("runic_altar");
	public static final ResourceLocation TERRA_PLATE = id("terra_plate");
	public static final ResourceLocation PETAL_APOTHECARY = id("petal_apothecary");
	public static final ResourceLocation BREWERY = id("brewery");
	public static final ResourceLocation MANA_ENCHANTER = id("mana_enchanter");
	public static final ResourceLocation ALFHEIM_PORTAL = id("alfheim_portal");
	public static final ResourceLocation MANA_STORAGE = id("mana_storage");
	public static final ResourceLocation MANA_SPARK = id("mana_spark");

	private static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(BotaniaAPI.MODID, path);
	}
}
