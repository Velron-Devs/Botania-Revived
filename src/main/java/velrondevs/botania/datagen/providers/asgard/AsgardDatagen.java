package velrondevs.botania.datagen.providers.asgard;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public final class AsgardDatagen {
	private AsgardDatagen() {}

	public static void gatherData(GatherDataEvent evt, CompletableFuture<HolderLookup.Provider> lookup) {
		PackOutput output = evt.getGenerator().getPackOutput();
		if (evt instanceof GatherDataEvent.Server) {
			evt.addProvider(new AsgardRecipeProvider(output, lookup));
			evt.addProvider(new AsgardExtremeRecipeProvider(output));
			evt.addProvider(new AsgardLootProvider(output, lookup));
		}
		if (evt instanceof GatherDataEvent.Client) {
			evt.addProvider(new AsgardModelProvider(output));
		}
	}
}
