package velrondevs.botania.compat.sophisticated.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.compat.sophisticated.SophisticatedModule;
import velrondevs.botania.compat.sophisticated.SophisticatedOptional;
import velrondevs.botania.module.ModuleTagSink;

import java.util.Map;

public final class BotaniaStorageData {
	private static final String[] FACINGS = {"down", "east", "north", "south", "up", "west"};
	private static final int[] X_ROTATIONS = {180, 90, 90, 90, 0, 90};
	private static final int[] Y_ROTATIONS = {0, 90, 0, 180, 0, 270};

	private BotaniaStorageData() {}

	private enum Source {
		ANY,
		NETHERITE,
		ATM
	}

	public static void addAssets(Map<Identifier, JsonObject> files) {
		for (BotaniaStorageTier tier : BotaniaStorageTier.values()) {
			String barrel = tier.barrelName();
			files.put(id("models/block/" + barrel), barrelModel(tier, "sophisticatedstorage:block/barrel"));
			files.put(id("models/block/flat/" + barrel), barrelModel(tier, "sophisticatedstorage:block/flat/barrel"));
			files.put(id("items/" + barrel), barrelItem(barrel));
			files.put(id("blockstates/" + barrel), barrelState(barrel));
			files.put(id("items/" + tier.chestName()), chestItem());
			files.put(id("blockstates/" + tier.chestName()), chestState());
			files.put(id("models/item/" + tier.upgradeName()), generated("item/" + tier.upgradeName()));
			files.put(id("items/" + tier.upgradeName()), itemModel(botania("item/" + tier.upgradeName())));
		}
		JsonObject source = new JsonObject();
		source.addProperty("type", "neoforge:namespaced_directory");
		source.addProperty("namespace", LibMisc.MOD_ID);
		source.addProperty("source", "entity/chest");
		source.addProperty("prefix", "entity/chest/");
		JsonArray sources = new JsonArray();
		sources.add(source);
		JsonObject atlas = new JsonObject();
		atlas.add("sources", sources);
		files.put(Identifier.withDefaultNamespace("atlases/chests"), atlas);
	}

	public static void addData(Map<Identifier, JsonObject> files) {
		for (BotaniaStorageTier tier : BotaniaStorageTier.values()) {
			String barrel = tier.barrelName();
			String chest = tier.chestName();
			files.put(id("loot_table/blocks/" + barrel), loot(tier, barrel));
			files.put(id("loot_table/blocks/" + chest), loot(tier, chest));
			files.put(id("recipe/sophisticated/" + tier.upgradeName()), tierUpgrade(tier));
			BotaniaStorageTier previous = tier.previous();
			if (previous == null) {
				addUpgrades(files, tier, "sophisticatedstorage:netherite_barrel", "sophisticatedstorage:netherite_chest", "", Source.NETHERITE);
				addUpgrades(files, tier, SophisticatedOptional.ATM_UNOBTAINIUM_BARREL, SophisticatedOptional.ATM_UNOBTAINIUM_CHEST, "atm/", Source.ATM);
			} else {
				addUpgrades(files, tier, botania(previous.barrelName()), botania(previous.chestName()), "", Source.ANY);
			}
		}
	}

	private static void addUpgrades(Map<Identifier, JsonObject> files, BotaniaStorageTier tier, String previousBarrel, String previousChest,
			String folder, Source source) {
		String barrel = tier.barrelName();
		String chest = tier.chestName();
		files.put(id("recipe/sophisticated/" + folder + barrel), upgrade(tier, previousBarrel, barrel, source));
		files.put(id("recipe/sophisticated/" + folder + chest), upgrade(tier, previousChest, chest, source));
		files.put(id("recipe/sophisticated/" + folder + "double_" + chest), doubleChest(tier, previousChest, chest, source));
		files.put(id("advancement/recipes/misc/sophisticated/" + folder + barrel), advancement(tier, source, "sophisticated/" + folder + barrel, previousBarrel));
		files.put(id("advancement/recipes/misc/sophisticated/" + folder + chest), advancement(tier, source, "sophisticated/" + folder + chest, previousChest));
		files.put(id("advancement/recipes/misc/sophisticated/" + folder + "double_" + chest), advancement(tier, source, "sophisticated/" + folder + "double_" + chest, previousChest));
		files.put(id("advancement/recipes/misc/sophisticated/" + folder + tier.upgradeName()), advancement(tier, source, "sophisticated/" + tier.upgradeName(), previousBarrel));
	}

	public static void addBlockTags(ModuleTagSink<Block> sink) {
		var axe = sink.tag(BlockTags.MINEABLE_WITH_AXE);
		var barrels = sink.tag(TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "barrels")));
		var chests = sink.tag(TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "chests")));
		for (BotaniaStorageTier tier : BotaniaStorageTier.values()) {
			BuiltInRegistries.BLOCK.getOptional(id(tier.barrelName())).ifPresent(axe::addOptional);
			BuiltInRegistries.BLOCK.getOptional(id(tier.chestName())).ifPresent(axe::addOptional);
			BuiltInRegistries.BLOCK.getOptional(id(tier.barrelName())).ifPresent(barrels::addOptional);
			BuiltInRegistries.BLOCK.getOptional(id(tier.chestName())).ifPresent(chests::addOptional);
		}
	}

	public static void addItemTags(ModuleTagSink<Item> sink) {
		var barrels = sink.tag(ItemTags.create(Identifier.fromNamespaceAndPath("c", "barrels")));
		var chests = sink.tag(ItemTags.create(Identifier.fromNamespaceAndPath("c", "chests")));
		var all = sink.tag(ItemTags.create(Identifier.fromNamespaceAndPath(SophisticatedModule.STORAGE, "all_storage")));
		for (BotaniaStorageTier tier : BotaniaStorageTier.values()) {
			BuiltInRegistries.ITEM.getOptional(id(tier.barrelName())).ifPresent(barrels::addOptional);
			BuiltInRegistries.ITEM.getOptional(id(tier.chestName())).ifPresent(chests::addOptional);
			BuiltInRegistries.ITEM.getOptional(id(tier.barrelName())).ifPresent(all::addOptional);
			BuiltInRegistries.ITEM.getOptional(id(tier.chestName())).ifPresent(all::addOptional);
		}
	}

	private static JsonObject barrelModel(BotaniaStorageTier tier, String parent) {
		JsonObject model = new JsonObject();
		model.addProperty("loader", "sophisticatedstorage:barrel");
		model.addProperty("parent", parent);
		JsonObject textures = new JsonObject();
		textures.addProperty("top", botania("block/" + tier.barrelName() + "_top"));
		textures.addProperty("side", botania("block/" + tier.barrelName() + "_side"));
		textures.addProperty("bottom", botania("block/" + tier.barrelName() + "_bottom"));
		textures.addProperty("top_trim", botania("block/" + tier.barrelName() + "_top"));
		JsonObject part = new JsonObject();
		part.add("textures", textures);
		JsonObject parts = new JsonObject();
		parts.add("tier", part);
		model.add("model_parts", parts);
		return model;
	}

	private static JsonObject barrelState(String barrel) {
		JsonObject variants = new JsonObject();
		for (boolean flat : new boolean[]{false, true}) {
			for (int i = 0; i < FACINGS.length; i++) {
				JsonObject variant = new JsonObject();
				if (X_ROTATIONS[i] != 0) {
					variant.addProperty("x", X_ROTATIONS[i]);
				}
				if (Y_ROTATIONS[i] != 0) {
					variant.addProperty("y", Y_ROTATIONS[i]);
				}
				variant.addProperty("type", "sophisticatedstorage:barrel_blockstate_model_loader");
				variant.addProperty("model", botania((flat ? "block/flat/" : "block/") + barrel));
				variants.add("facing=" + FACINGS[i] + ",flat_top=" + flat, variant);
			}
		}
		JsonObject state = new JsonObject();
		state.add("variants", variants);
		return state;
	}

	private static JsonObject chestState() {
		JsonObject variant = new JsonObject();
		variant.addProperty("type", "sophisticatedstorage:chest_model_loader");
		JsonObject variants = new JsonObject();
		variants.add("", variant);
		JsonObject state = new JsonObject();
		state.add("variants", variants);
		return state;
	}

	private static JsonObject barrelItem(String barrel) {
		JsonObject model = new JsonObject();
		model.addProperty("type", "sophisticatedstorage:barrel");
		model.addProperty("flat_top_model", botania("block/flat/" + barrel));
		model.addProperty("model", botania("block/" + barrel));
		JsonArray tints = new JsonArray();
		tints.add(tint("sophisticatedstorage:main"));
		tints.add(tint("sophisticatedstorage:accent"));
		model.add("tints", tints);
		JsonObject json = new JsonObject();
		json.add("model", model);
		return json;
	}

	private static JsonObject tint(String type) {
		JsonObject json = new JsonObject();
		json.addProperty("type", type);
		json.addProperty("default", -1);
		return json;
	}

	private static JsonObject chestItem() {
		JsonObject special = new JsonObject();
		special.addProperty("type", "sophisticatedstorage:chest");
		JsonObject model = new JsonObject();
		model.addProperty("type", "minecraft:special");
		model.addProperty("base", "sophisticatedstorage:item/chest");
		model.add("model", special);
		JsonObject json = new JsonObject();
		json.add("model", model);
		return json;
	}

	private static JsonObject itemModel(String model) {
		JsonObject reference = new JsonObject();
		reference.addProperty("type", "minecraft:model");
		reference.addProperty("model", model);
		JsonObject json = new JsonObject();
		json.add("model", reference);
		return json;
	}

	private static JsonObject generated(String texture) {
		JsonObject json = parent("minecraft:item/generated");
		JsonObject textures = new JsonObject();
		textures.addProperty("layer0", botania(texture));
		json.add("textures", textures);
		return json;
	}

	private static JsonObject parent(String parent) {
		JsonObject json = new JsonObject();
		json.addProperty("parent", parent);
		return json;
	}

	private static JsonObject loot(BotaniaStorageTier tier, String name) {
		JsonObject json = conditioned(tier, Source.ANY);
		json.addProperty("type", "minecraft:block");
		JsonObject copyName = new JsonObject();
		copyName.addProperty("function", "minecraft:copy_components");
		JsonArray include = new JsonArray();
		include.add("minecraft:custom_name");
		copyName.add("include", include);
		copyName.addProperty("source", "block_entity");
		JsonObject copyData = new JsonObject();
		copyData.addProperty("function", "sophisticatedstorage:copy_storage_data");
		JsonArray functions = new JsonArray();
		functions.add(copyName);
		functions.add(copyData);
		JsonObject entry = new JsonObject();
		entry.addProperty("type", "minecraft:item");
		entry.addProperty("name", botania(name));
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
		json.addProperty("random_sequence", botania("blocks/" + name));
		return json;
	}

	private static JsonObject upgrade(BotaniaStorageTier tier, String previous, String result, Source source) {
		JsonObject json = conditioned(tier, source);
		json.addProperty("type", "sophisticatedstorage:storage_tier_upgrade");
		json.addProperty("category", "misc");
		JsonArray pattern = new JsonArray();
		pattern.add("III");
		pattern.add("ISI");
		pattern.add("III");
		json.add("pattern", pattern);
		JsonObject key = new JsonObject();
		key.add("I", ingot(tier));
		key.add("S", item(previous));
		json.add("key", key);
		json.add("result", output(result));
		return json;
	}

	private static JsonObject tierUpgrade(BotaniaStorageTier tier) {
		JsonObject json = conditioned(tier, Source.ANY);
		json.addProperty("type", "minecraft:crafting_shaped");
		json.addProperty("category", "misc");
		JsonArray pattern = new JsonArray();
		pattern.add("III");
		pattern.add("ILI");
		pattern.add("III");
		json.add("pattern", pattern);
		JsonObject key = new JsonObject();
		key.add("I", ingot(tier));
		key.add("L", item("minecraft:lever"));
		json.add("key", key);
		json.add("result", output(tier.upgradeName()));
		return json;
	}

	private static JsonObject doubleChest(BotaniaStorageTier tier, String previous, String result, Source source) {
		JsonObject json = conditioned(tier, source);
		json.addProperty("type", "sophisticatedstorage:double_chest_tier_upgrade_shapeless");
		json.addProperty("category", "misc");
		JsonArray ingredients = new JsonArray();
		ingredients.add(item(previous));
		ingredients.add(ingot(tier));
		ingredients.add(ingot(tier));
		json.add("ingredients", ingredients);
		json.add("result", output(result));
		return json;
	}

	private static JsonObject output(String result) {
		JsonObject output = new JsonObject();
		output.addProperty("count", 1);
		output.addProperty("id", botania(result));
		return output;
	}

	private static JsonElement ingot(BotaniaStorageTier tier) {
		return tier.ingotIsTag() ? tag(tier.ingot()) : item(tier.ingot());
	}

	private static JsonObject advancement(BotaniaStorageTier tier, Source source, String recipe, String trigger) {
		JsonObject json = conditioned(tier, source);
		json.addProperty("parent", "minecraft:recipes/root");
		JsonObject unlockedConditions = new JsonObject();
		unlockedConditions.addProperty("recipe", botania(recipe));
		JsonObject unlocked = new JsonObject();
		unlocked.add("conditions", unlockedConditions);
		unlocked.addProperty("trigger", "minecraft:recipe_unlocked");
		JsonObject predicate = new JsonObject();
		predicate.addProperty("items", trigger);
		JsonArray items = new JsonArray();
		items.add(predicate);
		JsonObject hasConditions = new JsonObject();
		hasConditions.add("items", items);
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
		recipes.add(botania(recipe));
		JsonObject rewards = new JsonObject();
		rewards.add("recipes", recipes);
		json.add("rewards", rewards);
		return json;
	}

	private static JsonObject conditioned(BotaniaStorageTier tier, Source source) {
		JsonObject json = new JsonObject();
		JsonArray conditions = new JsonArray();
		SophisticatedOptional.addTierCondition(conditions, tier.requiredMod());
		if (source == Source.ATM) {
			conditions.add(SophisticatedOptional.modLoaded(SophisticatedOptional.ATM_TIERS));
		} else if (source == Source.NETHERITE) {
			conditions.add(SophisticatedOptional.notModLoaded(SophisticatedOptional.ATM_TIERS));
		}
		conditions.add(condition("neoforge:mod_loaded", "modid", SophisticatedModule.CORE));
		conditions.add(condition("neoforge:mod_loaded", "modid", SophisticatedModule.STORAGE));
		conditions.add(condition(LibMisc.MOD_ID + ":module_enabled", "module", SophisticatedModule.ID));
		JsonObject enabled = new JsonObject();
		enabled.addProperty("type", StorageEnabledCondition.ID.toString());
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

	private static JsonElement item(String id) {
		return new JsonPrimitive(id);
	}

	private static JsonElement tag(String id) {
		return new JsonPrimitive("#" + id);
	}

	private static String botania(String path) {
		return LibMisc.MOD_ID + ":" + path;
	}

	private static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(LibMisc.MOD_ID, path);
	}
}
