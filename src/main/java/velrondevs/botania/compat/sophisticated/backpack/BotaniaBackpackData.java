package velrondevs.botania.compat.sophisticated.backpack;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.compat.sophisticated.SophisticatedModule;
import velrondevs.botania.compat.sophisticated.SophisticatedOptional;
import velrondevs.botania.module.ModuleTagSink;

import java.util.Map;

public final class BotaniaBackpackData {
	private static final String BASE_MODEL = "sophisticatedbackpacks:block/backpack";
	private static final String NETHERITE = "sophisticatedbackpacks:netherite_backpack";
	private static final String[] FACINGS = {"east", "north", "south", "west"};
	private static final int[] ROTATIONS = {90, 0, 180, 270};

	private BotaniaBackpackData() {}

	private enum Source {
		ANY,
		NETHERITE,
		ATM
	}

	public static void addAssets(Map<ResourceLocation, JsonObject> files) {
		for (BotaniaBackpackTier tier : BotaniaBackpackTier.values()) {
			String id = tier.id();
			JsonObject model = new JsonObject();
			model.addProperty("parent", BASE_MODEL);
			JsonObject textures = new JsonObject();
			textures.addProperty("clips", botania("block/" + tier.clips()));
			model.add("textures", textures);
			files.put(id("models/block/" + id), model);

			JsonObject item = new JsonObject();
			item.addProperty("parent", botania("block/" + id));
			files.put(id("models/item/" + id), item);

			JsonObject variants = new JsonObject();
			for (int i = 0; i < FACINGS.length; i++) {
				JsonObject variant = new JsonObject();
				variant.addProperty("model", botania("block/" + id));
				if (ROTATIONS[i] != 0) {
					variant.addProperty("y", ROTATIONS[i]);
				}
				variants.add("facing=" + FACINGS[i], variant);
			}
			JsonObject state = new JsonObject();
			state.add("variants", variants);
			files.put(id("blockstates/" + id), state);
		}
	}

	public static void addData(Map<ResourceLocation, JsonObject> files) {
		for (BotaniaBackpackTier tier : BotaniaBackpackTier.values()) {
			String id = tier.id();
			files.put(id("loot_table/blocks/" + id), loot(tier));
			if (tier.previous() == null) {
				files.put(id("recipe/sophisticated/" + id), recipe(tier, NETHERITE, Source.NETHERITE));
				files.put(id("advancement/recipes/misc/sophisticated/" + id), advancement(tier, NETHERITE, Source.NETHERITE, id));
				files.put(id("recipe/sophisticated/atm/" + id), recipe(tier, SophisticatedOptional.ATM_UNOBTAINIUM_BACKPACK, Source.ATM));
				files.put(id("advancement/recipes/misc/sophisticated/atm/" + id), advancement(tier, SophisticatedOptional.ATM_UNOBTAINIUM_BACKPACK, Source.ATM, "atm/" + id));
			} else {
				String previous = botania(tier.previous().id());
				files.put(id("recipe/sophisticated/" + id), recipe(tier, previous, Source.ANY));
				files.put(id("advancement/recipes/misc/sophisticated/" + id), advancement(tier, previous, Source.ANY, id));
			}
		}
	}

	public static void addBackTags(ModuleTagSink<Item> sink) {
		var curios = sink.tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("curios", "back")));
		var accessories = sink.tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("accessories", "back")));
		for (BotaniaBackpackTier tier : BotaniaBackpackTier.values()) {
			curios.addOptional(id(tier.id()));
			accessories.addOptional(id(tier.id()));
		}
	}

	private static JsonObject loot(BotaniaBackpackTier tier) {
		String id = tier.id();
		JsonObject json = conditioned(tier, Source.ANY);
		json.addProperty("type", "minecraft:block");
		JsonObject function = new JsonObject();
		function.addProperty("function", "sophisticatedbackpacks:copy_backpack_data");
		JsonArray functions = new JsonArray();
		functions.add(function);
		JsonObject entry = new JsonObject();
		entry.addProperty("type", "minecraft:item");
		entry.addProperty("name", botania(id));
		JsonArray entries = new JsonArray();
		entries.add(entry);
		JsonObject pool = new JsonObject();
		pool.addProperty("bonus_rolls", 0.0);
		pool.add("entries", entries);
		pool.add("functions", functions);
		pool.addProperty("name", "main");
		pool.addProperty("rolls", 1.0);
		JsonArray pools = new JsonArray();
		pools.add(pool);
		json.add("pools", pools);
		json.addProperty("random_sequence", botania("blocks/" + id));
		return json;
	}

	private static JsonObject recipe(BotaniaBackpackTier tier, String base, Source source) {
		JsonObject json = conditioned(tier, source);
		json.addProperty("type", "sophisticatedbackpacks:backpack_upgrade");
		json.addProperty("category", "misc");
		JsonArray pattern = new JsonArray();
		pattern.add(" I ");
		pattern.add("IBI");
		pattern.add(" I ");
		json.add("pattern", pattern);
		JsonObject key = new JsonObject();
		key.add("B", item(base));
		key.add("I", ingot(tier));
		json.add("key", key);
		JsonObject result = new JsonObject();
		result.addProperty("count", 1);
		result.addProperty("id", botania(tier.id()));
		json.add("result", result);
		return json;
	}

	private static JsonObject ingot(BotaniaBackpackTier tier) {
		JsonObject json = new JsonObject();
		json.addProperty(tier.ingotIsTag() ? "tag" : "item", tier.ingot());
		return json;
	}

	private static JsonObject advancement(BotaniaBackpackTier tier, String base, Source source, String name) {
		String recipe = botania("sophisticated/" + name);
		JsonObject json = conditioned(tier, source);
		json.addProperty("parent", "minecraft:recipes/root");
		JsonObject unlockedConditions = new JsonObject();
		unlockedConditions.addProperty("recipe", recipe);
		JsonObject unlocked = new JsonObject();
		unlocked.add("conditions", unlockedConditions);
		unlocked.addProperty("trigger", "minecraft:recipe_unlocked");
		JsonObject predicate = new JsonObject();
		predicate.addProperty("items", base);
		JsonArray predicates = new JsonArray();
		predicates.add(predicate);
		JsonObject hasConditions = new JsonObject();
		hasConditions.add("items", predicates);
		JsonObject has = new JsonObject();
		has.add("conditions", hasConditions);
		has.addProperty("trigger", "minecraft:inventory_changed");
		JsonObject criteria = new JsonObject();
		criteria.add("has_the_recipe", unlocked);
		criteria.add("has_item", has);
		json.add("criteria", criteria);
		JsonArray any = new JsonArray();
		any.add("has_the_recipe");
		any.add("has_item");
		JsonArray requirements = new JsonArray();
		requirements.add(any);
		json.add("requirements", requirements);
		JsonArray recipes = new JsonArray();
		recipes.add(recipe);
		JsonObject rewards = new JsonObject();
		rewards.add("recipes", recipes);
		json.add("rewards", rewards);
		return json;
	}

	private static JsonObject item(String id) {
		JsonObject json = new JsonObject();
		json.addProperty("item", id);
		return json;
	}

	private static JsonObject conditioned(BotaniaBackpackTier tier, Source source) {
		JsonObject json = new JsonObject();
		JsonArray conditions = new JsonArray();
		SophisticatedOptional.addTierCondition(conditions, tier.requiredMod());
		if (source == Source.ATM) {
			conditions.add(SophisticatedOptional.modLoaded(SophisticatedOptional.ATM_TIERS));
		} else if (source == Source.NETHERITE) {
			conditions.add(SophisticatedOptional.notModLoaded(SophisticatedOptional.ATM_TIERS));
		}
		conditions.add(condition("neoforge:mod_loaded", "modid", SophisticatedModule.CORE));
		conditions.add(condition("neoforge:mod_loaded", "modid", SophisticatedModule.BACKPACKS));
		conditions.add(condition(LibMisc.MOD_ID + ":module_enabled", "module", SophisticatedModule.ID));
		JsonObject enabled = new JsonObject();
		enabled.addProperty("type", BackpacksEnabledCondition.ID.toString());
		conditions.add(enabled);
		json.add("neoforge:conditions", conditions);
		return json;
	}

	private static JsonObject condition(String type, String field, String value) {
		JsonObject json = new JsonObject();
		json.addProperty("type", type);
		json.addProperty(field, value);
		return json;
	}

	private static String botania(String path) {
		return LibMisc.MOD_ID + ":" + path;
	}

	private static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, path);
	}
}
