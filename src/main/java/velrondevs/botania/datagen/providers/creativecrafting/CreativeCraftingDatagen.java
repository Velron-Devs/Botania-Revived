package velrondevs.botania.datagen.providers.creativecrafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public final class CreativeCraftingDatagen {
	private CreativeCraftingDatagen() {}

	public static void gatherData(GatherDataEvent evt, CompletableFuture<HolderLookup.Provider> lookup) {
		DataGenerator generator = evt.getGenerator();
		PackOutput output = generator.getPackOutput();
		generator.addProvider(evt.includeServer(), new CreativeCraftingRecipeProvider(output, lookup));
		generator.addProvider(evt.includeServer(), new CreativeCraftingExtremeRecipeProvider(output));
	}
}
