package velrondevs.botania.datagen.providers.creativecrafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import velrondevs.botania.common.item.ManaTabletItem;
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

	public CreativeCraftingExtremeRecipeProvider(PackOutput output) {
		super(output, PackOutput.Target.DATA_PACK, "recipe", "Botania Creative Crafting recipes for the Re-Avaritia Extreme Crafting Table");
	}

	@Override
	protected void addFiles(Map<Identifier, JsonObject> files) {
		files.put(id("creative_pool_extreme"), creativePool());
		files.put(id("creative_mana_tablet_extreme"), creativeTablet());
		files.put(id("creative_corporea_spark_extreme"), creativeSpark());
		files.put(id("infrangible_platform_extreme"), infrangiblePlatform());
	}

	private static Identifier id(String name) {
		return prefix(CreativeCraftingModule.ID + "/" + name);
	}

	private static JsonObject creativePool() {
		Map<Character, String> key = new LinkedHashMap<>();
		key.put('N', botania("terrasteel_block"));
		key.put('X', botania("life_essence"));
		key.put('C', botania("mana_pool"));
		key.put('E', botania("dragonstone_block"));
		key.put('Y', botania("fabulous_pool"));
		key.put('F', botania("mana_tablet"));
		return shapedTable(CreativeCraftingModule.ID, key, new String[] {
				"NNNNNNNNN",
				"NXCXYXCXN",
				"NCXEYEXCN",
				"NXEEYEEXN",
				"YYYYFYYYY",
				"NXEEYEEXN",
				"NCXEYEXCN",
				"NXCXYXCXN",
				"NNNNNNNNN"
		}, result(botania("creative_pool"), 1));
	}

	private static JsonObject creativeTablet() {
		Map<Character, String> key = new LinkedHashMap<>();
		key.put('A', botania("elementium_block"));
		key.put('B', botania("rune_envy"));
		key.put('C', botania("rune_gluttony"));
		key.put('D', botania("rune_winter"));
		key.put('E', botania("rune_lust"));
		key.put('F', botania("rune_pride"));
		key.put('G', botania("rune_wrath"));
		key.put('H', botania("rune_greed"));
		key.put('I', botania("rune_sloth"));
		key.put('J', botania("infinite_fruit"));
		key.put('K', botania("flight_tiara"));
		key.put('L', botania("king_key"));
		key.put('M', botania("flugel_eye"));
		key.put('N', botania("odin_ring"));
		key.put('O', botania("spawner_mover"));
		key.put('P', botania("mana_mirror"));
		key.put('Q', botania("thor_ring"));
		key.put('R', botania("mana_tablet"));
		key.put('S', botania("dice"));
		key.put('T', botania("fabulous_pool"));
		key.put('U', botania("terrasteel_block"));
		JsonObject result = result(botania("mana_tablet"), 1);
		JsonObject data = new JsonObject();
		data.addProperty("creative", true);
		data.addProperty("mana", ManaTabletItem.MAX_MANA);
		JsonObject components = new JsonObject();
		components.add("minecraft:custom_data", data);
		components.addProperty("minecraft:rarity", "epic");
		result.add("components", components);
		return shapedTable(CreativeCraftingModule.ID, key, new String[] {
				"BAAACAAAD",
				"ATTJKLTTA",
				"ATUUMUUTA",
				"ANUOPOUQA",
				"EUUPRPUUF",
				"ASUOPOUSA",
				"ATUUUUUTA",
				"ATTSISTTA",
				"GAAAHAAAA"
		}, result);
	}

	private static JsonObject creativeSpark() {
		Map<Character, String> key = new LinkedHashMap<>();
		key.put('E', botania("elementium_block"));
		key.put('R', botania("rune_mana"));
		key.put('L', botania("life_essence"));
		key.put('P', botania("pixie_dust"));
		key.put('S', botania("corporea_spark_master"));
		return shapedTable(CreativeCraftingModule.ID, key, new String[] {
				"E R E",
				" L L ",
				"P S P",
				" L L ",
				"E P E"
		}, result(botania("corporea_spark_creative"), 1));
	}

	private static JsonObject infrangiblePlatform() {
		Map<Character, String> key = new LinkedHashMap<>();
		key.put('T', botania("terrasteel_block"));
		key.put('A', botania("abstruse_platform"));
		key.put('D', botania("dragonstone_block"));
		return shapedTable(CreativeCraftingModule.ID, key, new String[] {
				"TAT",
				"ADA",
				"TAT"
		}, result(botania("infrangible_platform"), 4));
	}

	public static String botania(String path) {
		return LibMisc.MOD_ID + ":" + path;
	}

	public static JsonObject shapedTable(String module, Map<Character, String> key, String[] pattern, JsonObject result) {
		JsonObject recipe = new JsonObject();
		recipe.add("neoforge:conditions", conditions(module));
		recipe.addProperty("type", SHAPED_TABLE);
		JsonObject keys = new JsonObject();
		key.forEach((symbol, item) -> keys.addProperty(String.valueOf(symbol), item));
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

	public static JsonObject result(String item, int count) {
		JsonObject result = new JsonObject();
		result.addProperty("id", item);
		if (count > 1) {
			result.addProperty("count", count);
		}
		return result;
	}

	private static JsonArray conditions(String module) {
		JsonObject loaded = new JsonObject();
		loaded.addProperty("type", "neoforge:mod_loaded");
		loaded.addProperty("modid", AVARITIA);
		JsonObject enabled = new JsonObject();
		enabled.addProperty("type", LibMisc.MOD_ID + ":module_enabled");
		enabled.addProperty("module", module);
		JsonArray conditions = new JsonArray();
		conditions.add(loaded);
		conditions.add(enabled);
		return conditions;
	}
}
