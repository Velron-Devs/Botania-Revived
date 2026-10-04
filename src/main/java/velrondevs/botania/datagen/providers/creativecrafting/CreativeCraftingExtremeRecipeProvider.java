package velrondevs.botania.datagen.providers.creativecrafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.datagen.providers.integration.JsonFileProvider;
import velrondevs.botania.module.creativecrafting.CreativeCraftingModule;

import java.util.LinkedHashMap;
import java.util.Map;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class CreativeCraftingExtremeRecipeProvider extends JsonFileProvider {
	public static final String AVARITIA = "avaritia";
	public static final String SHAPED_TABLE = "avaritia:shaped_table";
	public static final int TIER_EXTREME = 4;
	public static final int TABLET_MANA = 500000;

	public CreativeCraftingExtremeRecipeProvider(PackOutput output) {
		super(output, PackOutput.Target.DATA_PACK, "recipe", "Botania Creative Crafting recipes for the Re-Avaritia Extreme Crafting Table");
	}

	@Override
	protected void addFiles(Map<ResourceLocation, JsonObject> files) {
		files.put(id("creative_pool_extreme"), creativePool());
		files.put(id("creative_mana_tablet_extreme"), creativeTablet());
		files.put(id("creative_corporea_spark_extreme"), creativeSpark());
		files.put(id("infrangible_platform_extreme"), infrangiblePlatform());
	}

	private static ResourceLocation id(String name) {
		return prefix(CreativeCraftingModule.ID + "/" + name);
	}

	private static JsonObject creativePool() {
		Map<Character, String> key = new LinkedHashMap<>();
		key.put('N', "terrasteel_block");
		key.put('X', "life_essence");
		key.put('C', "mana_pool");
		key.put('E', "dragonstone_block");
		key.put('Y', "fabulous_pool");
		key.put('F', "mana_tablet");
		return shaped(key, new String[] {
				"NNNNNNNNN",
				"NXCXYXCXN",
				"NCXEYEXCN",
				"NXEEYEEXN",
				"YYYYFYYYY",
				"NXEEYEEXN",
				"NCXEYEXCN",
				"NXCXYXCXN",
				"NNNNNNNNN"
		}, result("creative_pool", 1, false));
	}

	private static JsonObject creativeTablet() {
		Map<Character, String> key = new LinkedHashMap<>();
		key.put('A', "elementium_block");
		key.put('B', "rune_envy");
		key.put('C', "rune_gluttony");
		key.put('D', "rune_winter");
		key.put('E', "rune_lust");
		key.put('F', "rune_pride");
		key.put('G', "rune_wrath");
		key.put('H', "rune_greed");
		key.put('I', "rune_sloth");
		key.put('J', "infinite_fruit");
		key.put('K', "flight_tiara");
		key.put('L', "king_key");
		key.put('M', "flugel_eye");
		key.put('N', "odin_ring");
		key.put('O', "spawner_mover");
		key.put('P', "mana_mirror");
		key.put('Q', "thor_ring");
		key.put('R', "mana_tablet");
		key.put('S', "dice");
		key.put('T', "fabulous_pool");
		key.put('U', "terrasteel_block");
		return shaped(key, new String[] {
				"BAAACAAAD",
				"ATTJKLTTA",
				"ATUUMUUTA",
				"ANUOPOUQA",
				"EUUPRPUUF",
				"ASUOPOUSA",
				"ATUUUUUTA",
				"ATTSISTTA",
				"GAAAHAAAA"
		}, result("mana_tablet", 1, true));
	}

	private static JsonObject creativeSpark() {
		Map<Character, String> key = new LinkedHashMap<>();
		key.put('E', "elementium_block");
		key.put('R', "rune_mana");
		key.put('L', "life_essence");
		key.put('P', "pixie_dust");
		key.put('S', "corporea_spark_master");
		return shaped(key, new String[] {
				"E R E",
				" L L ",
				"P S P",
				" L L ",
				"E P E"
		}, result("corporea_spark_creative", 1, false));
	}

	private static JsonObject infrangiblePlatform() {
		Map<Character, String> key = new LinkedHashMap<>();
		key.put('T', "terrasteel_block");
		key.put('A', "abstruse_platform");
		key.put('D', "dragonstone_block");
		return shaped(key, new String[] {
				"TAT",
				"ADA",
				"TAT"
		}, result("infrangible_platform", 4, false));
	}

	private static JsonObject shaped(Map<Character, String> key, String[] pattern, JsonObject result) {
		JsonObject recipe = new JsonObject();
		recipe.add("neoforge:conditions", conditions());
		recipe.addProperty("type", SHAPED_TABLE);
		JsonObject keys = new JsonObject();
		key.forEach((symbol, item) -> {
			JsonObject ingredient = new JsonObject();
			ingredient.addProperty("item", LibMisc.MOD_ID + ":" + item);
			keys.add(String.valueOf(symbol), ingredient);
		});
		recipe.add("key", keys);
		JsonArray rows = new JsonArray();
		for (String row : pattern) {
			rows.add(row);
		}
		recipe.add("pattern", rows);
		recipe.add("result", result);
		recipe.addProperty("tier", TIER_EXTREME);
		return recipe;
	}

	private static JsonObject result(String item, int count, boolean creativeTablet) {
		JsonObject result = new JsonObject();
		result.addProperty("count", count);
		result.addProperty("id", LibMisc.MOD_ID + ":" + item);
		if (creativeTablet) {
			JsonObject data = new JsonObject();
			data.addProperty("creative", true);
			data.addProperty("mana", TABLET_MANA);
			JsonObject components = new JsonObject();
			components.add("minecraft:custom_data", data);
			result.add("components", components);
		}
		return result;
	}

	private static JsonArray conditions() {
		JsonObject loaded = new JsonObject();
		loaded.addProperty("type", "neoforge:mod_loaded");
		loaded.addProperty("modid", AVARITIA);
		JsonObject enabled = new JsonObject();
		enabled.addProperty("type", "botania:module_enabled");
		enabled.addProperty("module", CreativeCraftingModule.ID);
		JsonArray values = new JsonArray();
		values.add(loaded);
		values.add(enabled);
		JsonObject and = new JsonObject();
		and.addProperty("type", "neoforge:and");
		and.add("values", values);
		JsonArray conditions = new JsonArray();
		conditions.add(and);
		return conditions;
	}
}
