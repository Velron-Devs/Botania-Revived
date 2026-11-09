package velrondevs.botania.datagen.providers.creativecrafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public final class CreativeCraftingDatagen {
	private CreativeCraftingDatagen() {}

	public static void gatherData(GatherDataEvent evt, CompletableFuture<HolderLookup.Provider> lookup) {
		PackOutput output = evt.getGenerator().getPackOutput();
		if (evt instanceof GatherDataEvent.Server) {
			evt.addProvider(new CreativeCraftingRecipeProvider(output, lookup));
			evt.addProvider(new CreativeCraftingExtremeRecipeProvider(output));
		}
	}
}
