package velrondevs.botania.datagen.providers.asgard;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import velrondevs.botania.datagen.providers.integration.JsonFileProvider;
import velrondevs.botania.module.asgard.AsgardModule;

import java.util.Map;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class AsgardExtremeRecipeProvider extends JsonFileProvider {
	public static final String AVARITIA = "avaritia";
	public static final String CREATE = "create";
	public static final String SHAPED_TABLE = "avaritia:shaped_table";
	public static final String MECHANICAL_CRAFTING = "create:mechanical_crafting";
	public static final int TIER_EXTREME = 4;
	public static final String[] PATTERN = {
			"   III   ",
			"  IIIII  ",
			"  IIXII  ",
			"  IIIII  ",
			"   III   ",
			" UU N UU ",
			"UUUUNUUUU",
			" UU N UU ",
			"    N    "
	};

	public AsgardExtremeRecipeProvider(PackOutput output) {
		super(output, PackOutput.Target.DATA_PACK, "recipe", "Botania Asgard recipes for the Extreme Crafting Table and Mechanical Crafter");
	}

	@Override
	protected void addFiles(Map<ResourceLocation, JsonObject> files) {
		JsonObject extreme = recipe(SHAPED_TABLE, AVARITIA);
		extreme.addProperty("tier", TIER_EXTREME);
		files.put(id("extreme/asgardandelion"), extreme);

		JsonObject mechanical = recipe(MECHANICAL_CRAFTING, CREATE, AVARITIA);
		mechanical.addProperty("accept_mirrored", false);
		mechanical.addProperty("category", "misc");
		mechanical.addProperty("show_notification", false);
		files.put(id("mechanical/asgardandelion"), mechanical);
	}

	private static ResourceLocation id(String path) {
		return prefix(AsgardModule.ID + "/" + path);
	}

	private static JsonObject recipe(String type, String... modids) {
		JsonObject recipe = new JsonObject();
		recipe.add("neoforge:conditions", conditions(modids));
		recipe.addProperty("type", type);
		JsonObject key = new JsonObject();
		key.add("I", item("avaritia:infinity_ingot"));
		key.add("N", item("avaritia:neutron_ingot"));
		key.add("U", item("avaritia:neutron_nugget"));
		key.add("X", item("avaritia:infinity_catalyst"));
		recipe.add("key", key);
		JsonArray rows = new JsonArray();
		for (String row : PATTERN) {
			rows.add(row);
		}
		recipe.add("pattern", rows);
		JsonObject result = new JsonObject();
		result.addProperty("count", 1);
		result.addProperty("id", "botania:asgardandelion");
		recipe.add("result", result);
		return recipe;
	}

	private static JsonObject item(String id) {
		JsonObject ingredient = new JsonObject();
		ingredient.addProperty("item", id);
		return ingredient;
	}

	private static JsonArray conditions(String... modids) {
		JsonArray values = new JsonArray();
		for (String modid : modids) {
			JsonObject loaded = new JsonObject();
			loaded.addProperty("type", "neoforge:mod_loaded");
			loaded.addProperty("modid", modid);
			values.add(loaded);
		}
		JsonObject enabled = new JsonObject();
		enabled.addProperty("type", "botania:module_enabled");
		enabled.addProperty("module", AsgardModule.ID);
		values.add(enabled);
		JsonObject and = new JsonObject();
		and.addProperty("type", "neoforge:and");
		and.add("values", values);
		JsonArray conditions = new JsonArray();
		conditions.add(and);
		return conditions;
	}
}
