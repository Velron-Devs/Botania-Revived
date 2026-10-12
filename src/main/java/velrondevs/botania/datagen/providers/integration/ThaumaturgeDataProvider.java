package velrondevs.botania.datagen.providers.integration;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.integration.thaumaturge.ThaumaturgeIntegration;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ThaumaturgeDataProvider extends JsonFileProvider {
	private static final String GOGGLES = "thaumaturge:goggles_revealing";
	private static final List<String> HELMETS = List.of("manasteel", "elementium", "terrasteel");

	private final boolean assets;
	private final PackOutput packOutput;

	public ThaumaturgeDataProvider(PackOutput output, boolean assets) {
		super(output, assets ? PackOutput.Target.RESOURCE_PACK : PackOutput.Target.DATA_PACK, "", assets ? "Botania Thaumaturge integration models" : "Botania Thaumaturge integration data");
		this.assets = assets;
		this.packOutput = output;
	}

	@Override
	protected void addFiles(Map<ResourceLocation, JsonObject> files) {
		if (assets) {
			addModels(files);
		} else {
			addRecipes(files);
		}
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		CompletableFuture<?> base = super.run(cache);
		if (assets) {
			return base;
		}
		Path root = packOutput.getOutputFolder().resolve(ThaumaturgeIntegration.ASPECT_PACK_PATH);
		return CompletableFuture.allOf(base,
				DataProvider.saveStable(cache, packMeta(), root.resolve("pack.mcmeta")),
				DataProvider.saveStable(cache, aspects(), root.resolve("data/" + ThaumaturgeIntegration.MOD_ID + "/data_maps/item/base_aspects.json")));
	}

	private static JsonObject packMeta() {
		JsonObject pack = new JsonObject();
		pack.addProperty("description", "Botania aspects for Thaumaturge");
		pack.addProperty("pack_format", 48);
		JsonObject json = new JsonObject();
		json.add("pack", pack);
		return json;
	}

	private static void addModels(Map<ResourceLocation, JsonObject> files) {
		for (String tier : HELMETS) {
			JsonObject model = new JsonObject();
			model.addProperty("parent", "minecraft:item/generated");
			JsonObject textures = new JsonObject();
			textures.addProperty("layer0", botania("item/" + tier + "_helmet_revealing"));
			model.add("textures", textures);
			files.put(id("models/item/" + tier + "_helmet_revealing"), model);
		}
	}

	private static void addRecipes(Map<ResourceLocation, JsonObject> files) {
		for (String tier : HELMETS) {
			String result = tier + "_helmet_revealing";
			JsonObject recipe = conditioned();
			recipe.addProperty("type", botania("helm_revealing"));
			recipe.addProperty("category", "equipment");
			JsonArray ingredients = new JsonArray();
			ingredients.add(item(botania(tier + "_helmet")));
			ingredients.add(item(GOGGLES));
			recipe.add("ingredients", ingredients);
			recipe.add("result", result(botania(result)));
			files.put(id("recipe/" + result), recipe);
			files.put(id("advancement/recipes/combat/" + result), advancement(result, botania(tier + "_helmet"), GOGGLES));
		}

		JsonObject upgrade = conditioned();
		upgrade.addProperty("type", botania("armor_upgrade"));
		upgrade.addProperty("category", "equipment");
		JsonObject key = new JsonObject();
		key.add("T", item(botania("livingwood_twig")));
		JsonObject ingots = new JsonObject();
		ingots.addProperty("tag", botania("terrasteel_ingots"));
		key.add("S", ingots);
		key.add("R", item(botania("rune_spring")));
		key.add("A", item(botania("manasteel_helmet_revealing")));
		upgrade.add("key", key);
		JsonArray pattern = new JsonArray();
		pattern.add("TRT");
		pattern.add("SAS");
		pattern.add(" S ");
		upgrade.add("pattern", pattern);
		upgrade.add("result", result(botania("terrasteel_helmet_revealing")));
		files.put(id("recipe/terrasteel_helmet_revealing_upgrade"), upgrade);
		files.put(id("advancement/recipes/combat/terrasteel_helmet_revealing_upgrade"),
				advancement("terrasteel_helmet_revealing_upgrade", botania("manasteel_helmet_revealing"), null));
	}

	private static JsonObject advancement(String recipe, String itemA, String itemB) {
		JsonObject json = conditioned();
		json.addProperty("parent", "minecraft:recipes/root");
		JsonObject criteria = new JsonObject();
		JsonArray first = new JsonArray();
		first.add("has_the_recipe");
		first.add("has_item");
		criteria.add("has_item", hasItem(itemA));
		if (itemB != null) {
			criteria.add("has_goggles", hasItem(itemB));
			first.add("has_goggles");
		}
		JsonObject unlocked = new JsonObject();
		JsonObject unlockedConditions = new JsonObject();
		unlockedConditions.addProperty("recipe", botania(recipe));
		unlocked.add("conditions", unlockedConditions);
		unlocked.addProperty("trigger", "minecraft:recipe_unlocked");
		criteria.add("has_the_recipe", unlocked);
		json.add("criteria", criteria);
		JsonArray requirements = new JsonArray();
		requirements.add(first);
		json.add("requirements", requirements);
		JsonObject rewards = new JsonObject();
		JsonArray recipes = new JsonArray();
		recipes.add(botania(recipe));
		rewards.add("recipes", recipes);
		json.add("rewards", rewards);
		return json;
	}

	private static JsonObject hasItem(String item) {
		JsonObject criterion = new JsonObject();
		JsonObject conditions = new JsonObject();
		JsonArray items = new JsonArray();
		JsonObject predicate = new JsonObject();
		predicate.addProperty("items", item);
		items.add(predicate);
		conditions.add("items", items);
		criterion.add("conditions", conditions);
		criterion.addProperty("trigger", "minecraft:inventory_changed");
		return criterion;
	}

	private static JsonObject aspects() {
		Map<String, String[]> aspects = new LinkedHashMap<>();
		aspects.put("botania:mana_powder", new String[] {"praecantatio:5", "potentia:5"});
		aspects.put("botania:mana_pearl", new String[] {"motus:10", "alienis:5", "praecantatio:10"});
		aspects.put("botania:mana_diamond", new String[] {"vitreus:15", "desiderium:10", "praecantatio:10"});
		aspects.put("botania:mana_string", new String[] {"bestia:5", "fabrico:1", "praecantatio:3"});
		aspects.put("#botania:manasteel_ingots", new String[] {"metallum:15", "praecantatio:5"});
		aspects.put("#botania:manasteel_nuggets", new String[] {"metallum:2", "praecantatio:1"});
		aspects.put("#botania:terrasteel_ingots", new String[] {"metallum:15", "terra:10", "praecantatio:10", "victus:5"});
		aspects.put("#botania:terrasteel_nuggets", new String[] {"metallum:2", "terra:1", "praecantatio:1"});
		aspects.put("#botania:elementium_ingots", new String[] {"metallum:15", "permutatio:10", "praecantatio:10"});
		aspects.put("#botania:elementium_nuggets", new String[] {"metallum:2", "permutatio:1", "praecantatio:1"});
		aspects.put("botania:gaia_ingot", new String[] {"metallum:20", "praecantatio:20", "spiritus:10", "terra:10", "victus:10"});
		aspects.put("botania:pixie_dust", new String[] {"volatus:10", "auram:5", "praecantatio:5"});
		aspects.put("botania:dragonstone", new String[] {"vitreus:15", "praecantatio:10", "alienis:5"});
		aspects.put("botania:life_essence", new String[] {"victus:20", "spiritus:5", "praecantatio:5"});
		aspects.put("botania:redstone_root", new String[] {"herba:5", "potentia:5"});
		aspects.put("botania:living_root", new String[] {"herba:5", "praecantatio:5"});
		aspects.put("botania:ender_air_bottle", new String[] {"aer:10", "alienis:5", "vitreus:2"});
		aspects.put("botania:pebble", new String[] {"terra:1"});
		aspects.put("botania:livingrock", new String[] {"terra:10", "ordo:3", "praecantatio:3"});
		aspects.put("botania:livingwood_log", new String[] {"herba:20", "victus:5", "praecantatio:3"});
		aspects.put("botania:dreamwood_log", new String[] {"herba:20", "aer:5", "praecantatio:5"});
		aspects.put("#botania:petals", new String[] {"herba:2", "praecantatio:1"});
		aspects.put("#botania:mystical_flowers", new String[] {"herba:5", "victus:2", "praecantatio:2"});
		aspects.put("botania:mana_quartz", new String[] {"vitreus:5", "praecantatio:3"});
		aspects.put("botania:rune_water", new String[] {"aqua:10", "praecantatio:5", "ordo:5"});
		aspects.put("botania:rune_fire", new String[] {"ignis:10", "praecantatio:5", "ordo:5"});
		aspects.put("botania:rune_earth", new String[] {"terra:10", "praecantatio:5", "ordo:5"});
		aspects.put("botania:rune_air", new String[] {"aer:10", "praecantatio:5", "ordo:5"});
		aspects.put("botania:rune_spring", new String[] {"herba:10", "victus:5", "praecantatio:5"});
		aspects.put("botania:rune_summer", new String[] {"lux:10", "ignis:5", "praecantatio:5"});
		aspects.put("botania:rune_autumn", new String[] {"perditio:10", "tenebrae:5", "praecantatio:5"});
		aspects.put("botania:rune_winter", new String[] {"gelum:10", "praecantatio:5", "ordo:5"});
		aspects.put("botania:rune_mana", new String[] {"potentia:10", "praecantatio:10", "ordo:5"});
		aspects.put("botania:rune_lust", new String[] {"desiderium:10", "humanus:5", "praecantatio:5"});
		aspects.put("botania:rune_gluttony", new String[] {"victus:10", "desiderium:5", "praecantatio:5"});
		aspects.put("botania:rune_greed", new String[] {"desiderium:10", "metallum:5", "praecantatio:5"});
		aspects.put("botania:rune_sloth", new String[] {"vacuos:10", "motus:5", "praecantatio:5"});
		aspects.put("botania:rune_wrath", new String[] {"aversio:10", "ignis:5", "praecantatio:5"});
		aspects.put("botania:rune_envy", new String[] {"desiderium:10", "aversio:5", "praecantatio:5"});
		aspects.put("botania:rune_pride", new String[] {"praemunio:10", "spiritus:5", "praecantatio:5"});

		JsonObject values = new JsonObject();
		aspects.forEach((target, list) -> {
			JsonArray entries = new JsonArray();
			for (String entry : list) {
				String[] parts = entry.split(":");
				JsonObject aspect = new JsonObject();
				aspect.addProperty("aspect", ThaumaturgeIntegration.MOD_ID + ":" + parts[0]);
				aspect.addProperty("amount", Integer.parseInt(parts[1]));
				entries.add(aspect);
			}
			values.add(target, entries);
		});
		JsonObject root = new JsonObject();
		root.add("values", values);
		return root;
	}

	private static JsonObject conditioned() {
		JsonObject json = new JsonObject();
		JsonArray conditions = new JsonArray();
		JsonObject condition = new JsonObject();
		condition.addProperty("type", "neoforge:mod_loaded");
		condition.addProperty("modid", ThaumaturgeIntegration.MOD_ID);
		conditions.add(condition);
		json.add("neoforge:conditions", conditions);
		return json;
	}

	private static JsonObject item(String id) {
		JsonObject json = new JsonObject();
		json.addProperty("item", id);
		return json;
	}

	private static JsonObject result(String id) {
		JsonObject json = new JsonObject();
		json.addProperty("count", 1);
		json.addProperty("id", id);
		return json;
	}

	private static String botania(String path) {
		return LibMisc.MOD_ID + ":" + path;
	}

	private static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, path);
	}
}
