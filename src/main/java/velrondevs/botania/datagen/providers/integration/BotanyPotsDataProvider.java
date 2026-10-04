package velrondevs.botania.datagen.providers.integration;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import velrondevs.botania.common.lib.LibMisc;

import java.util.List;
import java.util.Map;

public class BotanyPotsDataProvider extends JsonFileProvider {
	private static final String BOTANY_POTS = "botanypots";
	private static final String RECIPE_ROOT = "recipe/botanypots/";
	private static final String DIRT_TAG = "botanypots:soil/dirt";
	private static final String MUSHROOM_TAG = "botanypots:soil/mushroom";

	private static final String MANA_SOIL = "infused_grass";

	private static final List<String> SOILS = List.of(
			"enchanted_soil", "dry_grass", "golden_grass", "vivid_grass", "scorched_grass", "infused_grass", "mutated_grass");
	private static final List<Float> SOIL_GROWTH = List.of(0.5F, 0.0F, 0.05F, 0.1F, 0.0F, 0.15F, 0.1F);

	private static final List<String> SEED_VARIANTS = List.of("dry", "golden", "vivid", "scorched", "infused", "mutated");

	public BotanyPotsDataProvider(PackOutput output) {
		super(output, PackOutput.Target.DATA_PACK, "", "Botania Botany Pots integration");
	}

	@Override
	protected void addFiles(Map<ResourceLocation, JsonObject> files) {
		addSoils(files);
		addCrops(files);
		addFertilizers(files);
		addInteractions(files);
		addSoilTag(files);
	}

	private static void addSoils(Map<ResourceLocation, JsonObject> files) {
		for (int i = 0; i < SOILS.size(); i++) {
			boolean mana = SOILS.get(i).equals(MANA_SOIL);
			JsonObject soil = recipe(mana ? "botania:mana_soil" : "botanypots:block_derived_soil");
			soil.addProperty("block", botania(SOILS.get(i)));
			if (SOIL_GROWTH.get(i) > 0) {
				soil.addProperty("growth_modifier", SOIL_GROWTH.get(i));
			}
			if (mana) {
				soil.addProperty("mana_growth_bonus", 1.0F);
				soil.addProperty("mana_per_tick", 2);
				soil.addProperty("pool_radius", 3);
			}
			put(files, "soil/" + SOILS.get(i), soil);
		}
	}

	private static void addCrops(Map<ResourceLocation, JsonObject> files) {
		for (DyeColor color : DyeColor.values()) {
			String name = color.getName();
			String petal = botania(name + "_petal");

			JsonObject mystical = crop(name + "_mystical_flower", 1200);
			mystical.add("drops", drops(drop(petal, 2, 1.0F), drop(petal, 1, 0.5F)));
			put(files, "crop/" + name + "_mystical_flower", mystical);

			JsonObject shiny = crop(name + "_shiny_flower", 1800);
			shiny.add("drops", drops(drop(petal, 2, 1.0F), drop(petal, 1, 0.5F), drop("minecraft:glowstone_dust", 1, 0.3F)));
			put(files, "crop/" + name + "_shiny_flower", shiny);

			JsonObject doubleFlower = crop(name + "_double_flower", 2400);
			doubleFlower.add("drops", drops(drop(petal, 4, 1.0F), drop(petal, 2, 0.5F)));
			put(files, "crop/" + name + "_double_flower", doubleFlower);

			JsonObject mushroom = crop(name + "_mushroom", 1200);
			mushroom.add("soil", array(tag(DIRT_TAG), tag(MUSHROOM_TAG), item("minecraft:mycelium"), item("minecraft:podzol"),
					item("minecraft:crimson_nylium"), item("minecraft:warped_nylium")));
			mushroom.add("drops", drops(drop(botania(name + "_mushroom"), 1, 1.0F)));
			put(files, "crop/" + name + "_mushroom", mushroom);
		}
	}

	private static void addFertilizers(Map<ResourceLocation, JsonObject> files) {
		put(files, "fertilizer/floral_fertilizer", fertilizer("botania:fertilizer", 500, 800, "minecraft:item.bone_meal.use"));
		put(files, "fertilizer/mana_powder", fertilizer("botania:mana_powder", 100, 200, "minecraft:block.amethyst_block.chime"));
	}

	private static void addInteractions(Map<ResourceLocation, JsonObject> files) {
		put(files, "interaction/overgrowth_seed_to_enchanted_soil",
				interaction("botania:overgrowth_seed", "minecraft:grass_block", "botania:enchanted_soil", "minecraft:item.bone_meal.use"));
		for (String variant : SEED_VARIANTS) {
			put(files, "interaction/" + variant + "_seeds_to_" + variant + "_grass",
					interaction(botania(variant + "_seeds"), "minecraft:dirt", botania(variant + "_grass"), "minecraft:item.bone_meal.use"));
		}
		put(files, "interaction/grass_seeds_to_grass_block",
				interaction("botania:grass_seeds", "minecraft:dirt", "minecraft:grass_block", "minecraft:item.bone_meal.use"));
		put(files, "interaction/podzol_seeds_to_podzol",
				interaction("botania:podzol_seeds", "minecraft:dirt", "minecraft:podzol", "minecraft:item.bone_meal.use"));
		put(files, "interaction/mycelium_seeds_to_mycelium",
				interaction("botania:mycelium_seeds", "minecraft:dirt", "minecraft:mycelium", "minecraft:item.bone_meal.use"));
	}

	private static void addSoilTag(Map<ResourceLocation, JsonObject> files) {
		JsonArray values = new JsonArray();
		for (String soil : SOILS) {
			values.add(botania(soil));
		}
		JsonObject tag = new JsonObject();
		tag.addProperty("replace", false);
		tag.add("values", values);
		files.put(ResourceLocation.fromNamespaceAndPath(BOTANY_POTS, "tags/item/soil/dirt"), tag);
	}

	private static JsonObject crop(String block, int growTime) {
		JsonObject crop = recipe("botanypots:block_derived_crop");
		crop.addProperty("block", botania(block));
		crop.addProperty("grow_time", growTime);
		return crop;
	}

	private static JsonObject fertilizer(String held, int min, int max, String sound) {
		JsonObject fertilizer = recipe("botanypots:fertilizer");
		fertilizer.add("held_item", item(held));
		JsonObject growth = new JsonObject();
		growth.addProperty("type", "botanypots:ranged");
		growth.addProperty("min", min);
		growth.addProperty("max", max);
		fertilizer.add("growth", growth);
		fertilizer.add("sound_effect", sound(sound));
		return fertilizer;
	}

	private static JsonObject interaction(String held, String soil, String newSoil, String sound) {
		JsonObject interaction = recipe("botanypots:pot_interaction");
		interaction.add("held_item", item(held));
		interaction.addProperty("consume_held", true);
		interaction.add("soil_item", item(soil));
		JsonObject result = new JsonObject();
		result.addProperty("id", newSoil);
		interaction.add("new_soil", result);
		interaction.add("sound_effect", sound(sound));
		return interaction;
	}

	private static JsonObject sound(String id) {
		JsonObject sound = new JsonObject();
		sound.addProperty("id", id);
		sound.addProperty("category", "BLOCKS");
		return sound;
	}

	private static JsonArray drops(JsonObject... entries) {
		JsonArray items = new JsonArray();
		for (JsonObject entry : entries) {
			items.add(entry);
		}
		JsonObject provider = new JsonObject();
		provider.addProperty("type", "botanypots:items");
		provider.add("items", items);
		JsonArray list = new JsonArray();
		list.add(provider);
		return list;
	}

	private static JsonObject drop(String id, int count, float chance) {
		JsonObject result = new JsonObject();
		result.addProperty("id", id);
		result.addProperty("count", count);
		JsonObject drop = new JsonObject();
		drop.add("result", result);
		if (chance < 1.0F) {
			drop.addProperty("chance", chance);
		}
		return drop;
	}

	private static JsonObject recipe(String type) {
		JsonObject condition = new JsonObject();
		condition.addProperty("type", "neoforge:mod_loaded");
		condition.addProperty("modid", BOTANY_POTS);
		JsonArray conditions = new JsonArray();
		conditions.add(condition);
		JsonObject recipe = new JsonObject();
		recipe.add("neoforge:conditions", conditions);
		recipe.addProperty("type", type);
		return recipe;
	}

	private static JsonObject item(String id) {
		JsonObject ingredient = new JsonObject();
		ingredient.addProperty("item", id);
		return ingredient;
	}

	private static JsonObject tag(String id) {
		JsonObject ingredient = new JsonObject();
		ingredient.addProperty("tag", id);
		return ingredient;
	}

	private static JsonArray array(JsonObject... entries) {
		JsonArray array = new JsonArray();
		for (JsonObject entry : entries) {
			array.add(entry);
		}
		return array;
	}

	private static String botania(String path) {
		return LibMisc.MOD_ID + ":" + path;
	}

	private static void put(Map<ResourceLocation, JsonObject> files, String path, JsonObject json) {
		files.put(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, RECIPE_ROOT + path), json);
	}
}
