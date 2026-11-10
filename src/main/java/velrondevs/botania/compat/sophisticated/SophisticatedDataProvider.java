package velrondevs.botania.compat.sophisticated;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.compat.sophisticated.backpack.BotaniaBackpackData;
import velrondevs.botania.compat.sophisticated.storage.BotaniaStorageData;
import velrondevs.botania.datagen.providers.integration.JsonFileProvider;

import java.util.List;
import java.util.Map;

public final class SophisticatedDataProvider extends JsonFileProvider {
	private static final String BASES_TAG = LibMisc.MOD_ID + ":sophisticated_upgrade_bases";
	private static final String NETHERITE_STACKS = "sophisticated_netherite_stack_upgrades";
	private static final String UNOBTAINIUM_STACKS = "sophisticated_unobtainium_stack_upgrades";
	private static final List<String> ITEMS = List.of(
			SophisticatedItems.MANASTEEL_STACK, SophisticatedItems.ELEMENTIUM_STACK, SophisticatedItems.TERRASTEEL_STACK,
			SophisticatedItems.GAIA_STACK, SophisticatedItems.ALFSTEEL_STACK, SophisticatedItems.MANA_PILE, SophisticatedItems.MANA_LINK,
			SophisticatedItems.RUNIC_ALTAR, SophisticatedItems.ELVEN_PORTAL, SophisticatedItems.BREWERY, SophisticatedItems.PETAL_APOTHECARY,
			SophisticatedItems.TERRA_PLATE);

	private final boolean assets;

	private SophisticatedDataProvider(PackOutput output, boolean assets) {
		super(output, assets ? PackOutput.Target.RESOURCE_PACK : PackOutput.Target.DATA_PACK, "",
				assets ? "Botania Sophisticated models" : "Botania Sophisticated recipes");
		this.assets = assets;
	}

	public static void gatherData(GatherDataEvent evt) {
		DataGenerator generator = evt.getGenerator();
		PackOutput output = generator.getPackOutput();
		generator.addProvider(evt.includeServer(), new SophisticatedDataProvider(output, false));
		generator.addProvider(evt.includeClient(), new SophisticatedDataProvider(output, true));
	}

	@Override
	protected void addFiles(Map<ResourceLocation, JsonObject> files) {
		if (assets) {
			for (String name : ITEMS) {
				JsonObject model = new JsonObject();
				model.addProperty("parent", "minecraft:item/generated");
				JsonObject textures = new JsonObject();
				textures.addProperty("layer0", botania("item/" + name));
				model.add("textures", textures);
				files.put(id("models/item/" + name), model);
			}
			BotaniaBackpackData.addAssets(files);
			BotaniaStorageData.addAssets(files);
			return;
		}
		JsonObject bases = new JsonObject();
		bases.addProperty("replace", false);
		JsonArray values = new JsonArray();
		values.add(optionalTagEntry("sophisticatedbackpacks:upgrade_base"));
		values.add(optionalTagEntry("sophisticatedstorage:upgrade_base"));
		bases.add("values", values);
		files.put(id("tags/item/sophisticated_upgrade_bases"), bases);
		BotaniaBackpackData.addData(files);
		BotaniaStorageData.addData(files);
		files.put(ResourceLocation.fromNamespaceAndPath(SophisticatedModule.BACKPACKS, "tags/item/upgrade"), upgradeTag());
		files.put(ResourceLocation.fromNamespaceAndPath(SophisticatedModule.STORAGE, "tags/item/upgrade"), upgradeTag());

		files.put(id("tags/item/" + NETHERITE_STACKS), optionalTag(
				SophisticatedModule.BACKPACKS + ":stack_upgrade_tier_4", SophisticatedModule.STORAGE + ":stack_upgrade_tier_5"));
		files.put(id("tags/item/" + UNOBTAINIUM_STACKS), optionalTag(
				SophisticatedOptional.ATM_TIERS + ":unobtainium_stack_upgrade", SophisticatedOptional.ATM_TIERS + ":unobtainium_storage_stack_upgrade"));
		manasteelStack(files, "", NETHERITE_STACKS, false);
		manasteelStack(files, "atm/", UNOBTAINIUM_STACKS, true);
		stack(files, SophisticatedItems.ELEMENTIUM_STACK, tag(LibMisc.MOD_ID + ":elementium_ingots"), item(botania(SophisticatedItems.MANASTEEL_STACK)), botania(SophisticatedItems.MANASTEEL_STACK));
		stack(files, SophisticatedItems.TERRASTEEL_STACK, tag(LibMisc.MOD_ID + ":terrasteel_ingots"), item(botania(SophisticatedItems.ELEMENTIUM_STACK)), botania(SophisticatedItems.ELEMENTIUM_STACK));
		stack(files, SophisticatedItems.GAIA_STACK, item(botania("gaia_ingot")), item(botania(SophisticatedItems.TERRASTEEL_STACK)), botania(SophisticatedItems.TERRASTEEL_STACK));
		stack(files, SophisticatedItems.ALFSTEEL_STACK, item(SophisticatedOptional.ALFSTEEL_INGOT), item(botania(SophisticatedItems.GAIA_STACK)), botania(SophisticatedItems.GAIA_STACK), SophisticatedOptional.MASTERFUL_BOTANY);

		JsonObject pile = recipe(SophisticatedItems.MANA_PILE, "XFX", "DBD", "XFX");
		JsonObject pileKey = new JsonObject();
		pileKey.add("X", item(botania("bifrost_pane")));
		pileKey.add("F", item(botania("fabulous_pool")));
		pileKey.add("D", tag(LibMisc.MOD_ID + ":mana_diamond_gems"));
		pileKey.add("B", tag(BASES_TAG));
		pile.add("key", pileKey);
		files.put(id("recipe/sophisticated/" + SophisticatedItems.MANA_PILE), pile);
		files.put(id("advancement/recipes/misc/sophisticated/" + SophisticatedItems.MANA_PILE), advancement("sophisticated/" + SophisticatedItems.MANA_PILE, botania("fabulous_pool")));

		JsonObject link = recipe(SophisticatedItems.MANA_LINK, "PRP", "EBE", "PRP");
		JsonObject linkKey = new JsonObject();
		linkKey.add("P", item(botania("pixie_dust")));
		linkKey.add("R", item(botania("rune_mana")));
		linkKey.add("E", tag(LibMisc.MOD_ID + ":elementium_ingots"));
		linkKey.add("B", tag(BASES_TAG));
		link.add("key", linkKey);
		files.put(id("recipe/sophisticated/" + SophisticatedItems.MANA_LINK), link);
		files.put(id("advancement/recipes/misc/sophisticated/" + SophisticatedItems.MANA_LINK), advancement("sophisticated/" + SophisticatedItems.MANA_LINK, botania("rune_mana")));

		JsonObject altar = recipe(SophisticatedItems.RUNIC_ALTAR, "DCD", "LBK", "DRD");
		JsonObject altarKey = new JsonObject();
		altarKey.add("D", tag(LibMisc.MOD_ID + ":mana_diamond_gems"));
		altarKey.add("C", item("minecraft:crafting_table"));
		altarKey.add("L", item(botania(SophisticatedItems.MANA_PILE)));
		altarKey.add("K", item(botania(SophisticatedItems.MANA_LINK)));
		altarKey.add("R", item(botania("runic_altar")));
		altarKey.add("B", tag(BASES_TAG));
		altar.add("key", altarKey);
		files.put(id("recipe/sophisticated/" + SophisticatedItems.RUNIC_ALTAR), altar);
		files.put(id("advancement/recipes/misc/sophisticated/" + SophisticatedItems.RUNIC_ALTAR), advancement("sophisticated/" + SophisticatedItems.RUNIC_ALTAR, botania("runic_altar")));

		JsonObject portal = recipe(SophisticatedItems.ELVEN_PORTAL, "DGD", "EBE", "DAD");
		JsonObject portalKey = new JsonObject();
		portalKey.add("D", tag(LibMisc.MOD_ID + ":dragonstone_gems"));
		portalKey.add("G", item(botania("glimmering_livingwood_log")));
		portalKey.add("A", item(botania("alfheim_portal")));
		portalKey.add("E", tag(LibMisc.MOD_ID + ":elementium_ingots"));
		portalKey.add("B", tag(BASES_TAG));
		portal.add("key", portalKey);
		files.put(id("recipe/sophisticated/" + SophisticatedItems.ELVEN_PORTAL), portal);
		files.put(id("advancement/recipes/misc/sophisticated/" + SophisticatedItems.ELVEN_PORTAL), advancement("sophisticated/" + SophisticatedItems.ELVEN_PORTAL, "#botania:dragonstone_gems"));

		JsonObject brewery = recipe(SophisticatedItems.BREWERY, "DAD", "LBL", "DWD");
		JsonObject breweryKey = new JsonObject();
		breweryKey.add("D", tag(LibMisc.MOD_ID + ":mana_diamond_gems"));
		breweryKey.add("A", item(botania("apothecary_default")));
		breweryKey.add("L", item(botania(SophisticatedItems.MANA_PILE)));
		breweryKey.add("W", item(botania("brewery")));
		breweryKey.add("B", tag(BASES_TAG));
		brewery.add("key", breweryKey);
		files.put(id("recipe/sophisticated/" + SophisticatedItems.BREWERY), brewery);
		files.put(id("advancement/recipes/misc/sophisticated/" + SophisticatedItems.BREWERY), advancement("sophisticated/" + SophisticatedItems.BREWERY, botania("brewery")));

		JsonObject apothecary = recipe(SophisticatedItems.PETAL_APOTHECARY, "VUV", "SBS", "VAV");
		JsonObject apothecaryKey = new JsonObject();
		apothecaryKey.add("V", item(botania("vial")));
		apothecaryKey.add("U", item("minecraft:water_bucket"));
		apothecaryKey.add("S", item(botania("grass_seeds")));
		apothecaryKey.add("A", item(botania("apothecary_default")));
		apothecaryKey.add("B", tag(BASES_TAG));
		apothecary.add("key", apothecaryKey);
		files.put(id("recipe/sophisticated/" + SophisticatedItems.PETAL_APOTHECARY), apothecary);
		files.put(id("advancement/recipes/misc/sophisticated/" + SophisticatedItems.PETAL_APOTHECARY), advancement("sophisticated/" + SophisticatedItems.PETAL_APOTHECARY, botania("apothecary_default")));

		terraPlate(files);
	}

	private static void terraPlate(Map<ResourceLocation, JsonObject> files) {
		String name = SophisticatedItems.TERRA_PLATE;
		String[] pattern = {"WMF", "DTD", "FPA"};
		JsonObject key = new JsonObject();
		key.add("W", item(botania("rune_water")));
		key.add("M", item(botania("rune_mana")));
		key.add("F", item(botania("rune_fire")));
		key.add("A", item(botania("rune_air")));
		key.add("D", tag(LibMisc.MOD_ID + ":mana_diamond_gems"));
		key.add("T", item(botania("terrasteel_block")));
		key.add("P", item(botania("terra_plate")));

		JsonObject plain = recipe(name, pattern);
		plain.add("key", key);
		files.put(id("recipe/sophisticated/" + name), plain);
		files.put(id("advancement/recipes/misc/sophisticated/" + name), advancement("sophisticated/" + name, botania("terra_plate")));
	}

	private static void manasteelStack(Map<ResourceLocation, JsonObject> files, String folder, String centerTag, boolean atm) {
		String name = SophisticatedItems.MANASTEEL_STACK;
		JsonObject json = recipe(name, "III", "ICI", "III");
		JsonArray conditions = json.getAsJsonArray("neoforge:conditions");
		conditions.add(atm ? SophisticatedOptional.modLoaded(SophisticatedOptional.ATM_TIERS) : SophisticatedOptional.notModLoaded(SophisticatedOptional.ATM_TIERS));
		JsonObject key = new JsonObject();
		key.add("I", tag(LibMisc.MOD_ID + ":manasteel_ingots"));
		key.add("C", tag(LibMisc.MOD_ID + ":" + centerTag));
		json.add("key", key);
		files.put(id("recipe/sophisticated/" + folder + name), json);
		JsonObject advancement = advancement("sophisticated/" + folder + name, "#botania:manasteel_ingots");
		advancement.getAsJsonArray("neoforge:conditions").add(atm ? SophisticatedOptional.modLoaded(SophisticatedOptional.ATM_TIERS) : SophisticatedOptional.notModLoaded(SophisticatedOptional.ATM_TIERS));
		files.put(id("advancement/recipes/misc/sophisticated/" + folder + name), advancement);
	}

	private static JsonObject optionalTag(String... ids) {
		JsonObject tag = new JsonObject();
		tag.addProperty("replace", false);
		JsonArray values = new JsonArray();
		for (String id : ids) {
			values.add(optionalTagEntry(id));
		}
		tag.add("values", values);
		return tag;
	}

	private static JsonObject upgradeTag() {
		JsonObject tag = new JsonObject();
		tag.addProperty("replace", false);
		JsonArray values = new JsonArray();
		for (String name : ITEMS) {
			values.add(optionalTagEntry(botania(name)));
		}
		tag.add("values", values);
		return tag;
	}

	private static void stack(Map<ResourceLocation, JsonObject> files, String name, JsonObject ring, JsonObject center, String trigger) {
		stack(files, name, ring, center, trigger, null);
	}

	private static void stack(Map<ResourceLocation, JsonObject> files, String name, JsonObject ring, JsonObject center, String trigger, String requiredMod) {
		JsonObject json = recipe(name, "III", "ICI", "III");
		requireMod(json, requiredMod);
		JsonObject key = new JsonObject();
		key.add("I", ring);
		key.add("C", center);
		json.add("key", key);
		files.put(id("recipe/sophisticated/" + name), json);
		JsonObject advancement = advancement("sophisticated/" + name, trigger);
		requireMod(advancement, requiredMod);
		files.put(id("advancement/recipes/misc/sophisticated/" + name), advancement);
	}

	private static void requireMod(JsonObject json, String requiredMod) {
		SophisticatedOptional.addTierCondition(json.getAsJsonArray("neoforge:conditions"), requiredMod);
	}

	private static JsonObject recipe(String result, String... pattern) {
		JsonObject json = conditioned();
		json.addProperty("type", "minecraft:crafting_shaped");
		json.addProperty("category", "misc");
		JsonArray rows = new JsonArray();
		for (String row : pattern) {
			rows.add(row);
		}
		json.add("pattern", rows);
		JsonObject output = new JsonObject();
		output.addProperty("count", 1);
		output.addProperty("id", botania(result));
		json.add("result", output);
		return json;
	}

	private static JsonObject advancement(String recipe, String trigger) {
		JsonObject json = conditioned();
		json.addProperty("parent", "minecraft:recipes/root");
		JsonObject criteria = new JsonObject();
		JsonObject unlocked = new JsonObject();
		JsonObject unlockedConditions = new JsonObject();
		unlockedConditions.addProperty("recipe", botania(recipe));
		unlocked.add("conditions", unlockedConditions);
		unlocked.addProperty("trigger", "minecraft:recipe_unlocked");
		criteria.add("has_the_recipe", unlocked);
		JsonObject has = new JsonObject();
		JsonObject hasConditions = new JsonObject();
		JsonArray items = new JsonArray();
		JsonObject predicate = new JsonObject();
		predicate.addProperty("items", trigger);
		items.add(predicate);
		hasConditions.add("items", items);
		has.add("conditions", hasConditions);
		has.addProperty("trigger", "minecraft:inventory_changed");
		criteria.add("has_item", has);
		json.add("criteria", criteria);
		JsonArray any = new JsonArray();
		any.add("has_the_recipe");
		any.add("has_item");
		JsonArray requirements = new JsonArray();
		requirements.add(any);
		json.add("requirements", requirements);
		JsonObject rewards = new JsonObject();
		JsonArray recipes = new JsonArray();
		recipes.add(botania(recipe));
		rewards.add("recipes", recipes);
		json.add("rewards", rewards);
		return json;
	}

	private static JsonObject conditioned() {
		JsonObject json = new JsonObject();
		JsonArray conditions = new JsonArray();
		JsonObject and = new JsonObject();
		and.addProperty("type", "neoforge:and");
		JsonArray all = new JsonArray();
		JsonObject module = new JsonObject();
		module.addProperty("type", LibMisc.MOD_ID + ":module_enabled");
		module.addProperty("module", SophisticatedModule.ID);
		all.add(module);
		all.add(modLoaded(SophisticatedModule.CORE));
		JsonObject or = new JsonObject();
		or.addProperty("type", "neoforge:or");
		JsonArray either = new JsonArray();
		either.add(modLoaded(SophisticatedModule.BACKPACKS));
		either.add(modLoaded(SophisticatedModule.STORAGE));
		or.add("values", either);
		all.add(or);
		and.add("values", all);
		conditions.add(and);
		json.add("neoforge:conditions", conditions);
		return json;
	}

	private static JsonObject modLoaded(String modId) {
		JsonObject condition = new JsonObject();
		condition.addProperty("type", "neoforge:mod_loaded");
		condition.addProperty("modid", modId);
		return condition;
	}

	private static JsonObject optionalTagEntry(String id) {
		JsonObject entry = new JsonObject();
		entry.addProperty("id", id);
		entry.addProperty("required", false);
		return entry;
	}

	private static JsonObject item(String id) {
		JsonObject json = new JsonObject();
		json.addProperty("item", id);
		return json;
	}

	private static JsonObject tag(String id) {
		JsonObject json = new JsonObject();
		json.addProperty("tag", id);
		return json;
	}

	private static String botania(String path) {
		return LibMisc.MOD_ID + ":" + path;
	}

	private static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, path);
	}
}
