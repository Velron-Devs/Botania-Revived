package velrondevs.botania.datagen.providers.integration;

import com.google.gson.JsonObject;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class QuarkAttributeTooltipProvider extends JsonFileProvider {
	private static final String[] SLOTS = { "mainhand", "offhand", "feet", "legs", "chest", "head", "potion" };

	public QuarkAttributeTooltipProvider(PackOutput output) {
		super(output, PackOutput.Target.RESOURCE_PACK, "", "Botania Quark attribute tooltips");
	}

	@Override
	protected void addFiles(Map<ResourceLocation, JsonObject> files) {
		JsonObject display = new JsonObject();
		for (String slot : SLOTS) {
			display.addProperty(slot, "percentage");
		}
		JsonObject pixieSpawnChance = new JsonObject();
		pixieSpawnChance.add("display", display);
		pixieSpawnChance.addProperty("texture", "botania:attribute/pixie_spawn_chance");
		pixieSpawnChance.addProperty("base_value", 0);
		JsonObject tooltips = new JsonObject();
		tooltips.add("botania:pixie_spawn_chance", pixieSpawnChance);
		files.put(ResourceLocation.fromNamespaceAndPath("quark", "attribute_tooltips"), tooltips);
	}
}
