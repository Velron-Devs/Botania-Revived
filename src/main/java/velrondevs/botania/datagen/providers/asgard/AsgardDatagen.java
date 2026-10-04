package velrondevs.botania.datagen.providers.asgard;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public final class AsgardDatagen {
	private AsgardDatagen() {}

	public static void gatherData(GatherDataEvent evt, CompletableFuture<HolderLookup.Provider> lookup) {
		DataGenerator generator = evt.getGenerator();
		PackOutput output = generator.getPackOutput();
		generator.addProvider(evt.includeServer(), new AsgardRecipeProvider(output, lookup));
		generator.addProvider(evt.includeServer(), new AsgardLootProvider(output, lookup));
		generator.addProvider(evt.includeClient(), new AsgardModelProvider(output));
	}
}
