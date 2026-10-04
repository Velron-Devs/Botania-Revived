package velrondevs.botania.datagen.providers.botaniaextras;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import velrondevs.botania.datagen.providers.integration.JsonFileProvider;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasModule;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasUtilities;

import java.util.Map;

public class BotaniaExtrasSpawnProvider extends JsonFileProvider {
	public BotaniaExtrasSpawnProvider(PackOutput output) {
		super(output, PackOutput.Target.DATA_PACK, "neoforge/biome_modifier", "Botania Extras spawns");
	}

	@Override
	protected void addFiles(Map<ResourceLocation, JsonObject> files) {
		JsonObject condition = new JsonObject();
		condition.addProperty("type", "botania:module_enabled");
		condition.addProperty("module", BotaniaExtrasModule.ID);
		JsonArray conditions = new JsonArray();
		conditions.add(condition);

		JsonObject spawner = new JsonObject();
		spawner.addProperty("type", BotaniaExtrasUtilities.MANASEAL_CREEPER_ID.toString());
		spawner.addProperty("weight", 10);
		spawner.addProperty("minCount", 1);
		spawner.addProperty("maxCount", 3);

		JsonObject modifier = new JsonObject();
		modifier.add("neoforge:conditions", conditions);
		modifier.addProperty("type", "neoforge:add_spawns");
		modifier.addProperty("biomes", "#minecraft:is_overworld");
		modifier.add("spawners", spawner);
		files.put(BotaniaExtrasUtilities.MANASEAL_CREEPER_ID, modifier);
	}
}
