package velrondevs.botania.datagen.providers.asgard;

import com.google.gson.JsonObject;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import velrondevs.botania.datagen.providers.integration.JsonFileProvider;
import velrondevs.botania.module.asgard.AsgardFlowers;
import velrondevs.botania.module.asgard.AsgardModule;

import java.util.LinkedHashMap;
import java.util.Map;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;
import static velrondevs.botania.datagen.providers.creativecrafting.CreativeCraftingExtremeRecipeProvider.botania;
import static velrondevs.botania.datagen.providers.creativecrafting.CreativeCraftingExtremeRecipeProvider.result;
import static velrondevs.botania.datagen.providers.creativecrafting.CreativeCraftingExtremeRecipeProvider.shapedTable;

public class AsgardExtremeRecipeProvider extends JsonFileProvider {
	public AsgardExtremeRecipeProvider(PackOutput output) {
		super(output, PackOutput.Target.DATA_PACK, "recipe", "Botania Asgard recipes for the Re-Avaritia Extreme Crafting Table");
	}

	@Override
	protected void addFiles(Map<Identifier, JsonObject> files) {
		Map<Character, String> key = new LinkedHashMap<>();
		key.put('T', botania("terrasteel_block"));
		key.put('D', botania("dragonstone_block"));
		key.put('L', botania("life_essence"));
		key.put('R', botania("wither_mana_rose"));
		key.put('F', "minecraft:dandelion");
		files.put(prefix(AsgardModule.ID + "/extreme/asgardandelion"), shapedTable(AsgardModule.ID, key, new String[] {
				"TTTTDTTTT",
				"TL  D  LT",
				"T  DRD  T",
				"T D L D T",
				"DRDLFLDRD",
				"T D L D T",
				"T  DRD  T",
				"TL  D  LT",
				"TTTTDTTTT"
		}, result(AsgardFlowers.ASGARDANDELION_ID.toString(), 1)));
	}
}
