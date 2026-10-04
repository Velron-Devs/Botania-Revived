package velrondevs.botania.datagen.providers.magicskies;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import velrondevs.botania.datagen.providers.integration.JsonFileProvider;
import velrondevs.botania.module.magicskies.MagicSkiesModule;

import java.util.Map;

public class MagicSkiesDimensionProvider extends JsonFileProvider {
	public enum Kind {
		DIMENSION("dimension", "Skybox dimension"),
		DIMENSION_TYPE("dimension_type", "Skybox dimension type");

		private final String folder;
		private final String name;

		Kind(String folder, String name) {
			this.folder = folder;
			this.name = name;
		}
	}

	private final Kind kind;

	public MagicSkiesDimensionProvider(PackOutput output, Kind kind) {
		super(output, PackOutput.Target.DATA_PACK, kind.folder, kind.name);
		this.kind = kind;
	}

	@Override
	protected void addFiles(Map<ResourceLocation, JsonObject> files) {
		JsonObject json = kind == Kind.DIMENSION ? dimension() : dimensionType();
		files.put(MagicSkiesModule.DIMENSION_ID, json);
	}

	private static JsonObject withCondition() {
		JsonObject condition = new JsonObject();
		condition.addProperty("type", "botania:module_enabled");
		condition.addProperty("module", MagicSkiesModule.ID);
		JsonArray conditions = new JsonArray();
		conditions.add(condition);
		JsonObject json = new JsonObject();
		json.add("neoforge:conditions", conditions);
		return json;
	}

	private static JsonObject dimension() {
		JsonObject settings = new JsonObject();
		settings.add("layers", new JsonArray());
		settings.addProperty("biome", "minecraft:the_void");

		JsonObject generator = new JsonObject();
		generator.addProperty("type", "minecraft:flat");
		generator.add("settings", settings);

		JsonObject json = withCondition();
		json.addProperty("type", MagicSkiesModule.DIMENSION_ID.toString());
		json.add("generator", generator);
		return json;
	}

	private static JsonObject dimensionType() {
		JsonObject json = withCondition();
		json.addProperty("ultrawarm", false);
		json.addProperty("natural", false);
		json.addProperty("piglin_safe", false);
		json.addProperty("respawn_anchor_works", false);
		json.addProperty("bed_works", false);
		json.addProperty("has_raids", false);
		json.addProperty("has_skylight", true);
		json.addProperty("has_ceiling", false);
		json.addProperty("coordinate_scale", 1.0);
		json.addProperty("ambient_light", 0.5);
		json.addProperty("effects", "minecraft:overworld");
		json.addProperty("min_y", 0);
		json.addProperty("height", 384);
		json.addProperty("logical_height", 384);
		json.addProperty("infiniburn", "#minecraft:infiniburn_overworld");
		json.addProperty("monster_spawn_light_level", 0);
		json.addProperty("monster_spawn_block_light_limit", 0);
		return json;
	}
}
