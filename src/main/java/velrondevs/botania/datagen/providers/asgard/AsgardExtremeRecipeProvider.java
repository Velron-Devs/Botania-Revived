package velrondevs.botania.datagen.providers.asgard;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import velrondevs.botania.datagen.providers.integration.JsonFileProvider;
import velrondevs.botania.module.asgard.AsgardFlowers;
import velrondevs.botania.module.asgard.AsgardModule;

import java.util.LinkedHashMap;
import java.util.Map;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;
import static velrondevs.botania.datagen.providers.creativecrafting.CreativeCraftingExtremeRecipeProvider.result;
import static velrondevs.botania.datagen.providers.creativecrafting.CreativeCraftingExtremeRecipeProvider.shapedTable;

public class AsgardExtremeRecipeProvider extends JsonFileProvider {
	public static final String CREATE = "create";
	public static final String MECHANICAL_CRAFTING = "create:mechanical_crafting";

	public AsgardExtremeRecipeProvider(PackOutput output) {
		super(output, PackOutput.Target.DATA_PACK, "recipe", "Botania Asgard recipes for the Extreme Crafting Table and Mechanical Crafter");
	}

	@Override
	protected void addFiles(Map<Identifier, JsonObject> files) {
		Map<Character, String> key = new LinkedHashMap<>();
		key.put('I', "avaritia:infinity_ingot");
		key.put('N', "avaritia:neutron_ingot");
		key.put('U', "avaritia:neutron_nugget");
		key.put('X', "avaritia:infinity_catalyst");
		JsonObject extreme = shapedTable(AsgardModule.ID, key, new String[] {
				"   III   ",
				"  IIIII  ",
				"  IIXII  ",
				"  IIIII  ",
				"   III   ",
				" UU N UU ",
				"UUUUNUUUU",
				" UU N UU ",
				"    N    "
		}, result(AsgardFlowers.ASGARDANDELION_ID.toString(), 1));
		files.put(prefix(AsgardModule.ID + "/extreme/asgardandelion"), extreme);
		files.put(prefix(AsgardModule.ID + "/mechanical/asgardandelion"), mechanical(extreme));
	}

	private static JsonObject mechanical(JsonObject extreme) {
		JsonObject recipe = extreme.deepCopy();
		recipe.remove("tier");
		recipe.addProperty("type", MECHANICAL_CRAFTING);
		JsonArray conditions = recipe.getAsJsonArray("neoforge:conditions");
		JsonObject create = new JsonObject();
		create.addProperty("type", "neoforge:mod_loaded");
		create.addProperty("modid", CREATE);
		JsonArray merged = new JsonArray();
		merged.add(create);
		conditions.forEach(merged::add);
		recipe.add("neoforge:conditions", merged);
		recipe.addProperty("accept_mirrored", false);
		recipe.addProperty("category", "misc");
		recipe.addProperty("show_notification", false);
		return recipe;
	}
}
