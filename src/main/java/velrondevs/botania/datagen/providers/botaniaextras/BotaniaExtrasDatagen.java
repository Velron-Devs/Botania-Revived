package velrondevs.botania.datagen.providers.botaniaextras;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public final class BotaniaExtrasDatagen {
	private BotaniaExtrasDatagen() {}

	public static void gatherData(GatherDataEvent evt, CompletableFuture<HolderLookup.Provider> lookup) {
		PackOutput output = evt.getGenerator().getPackOutput();
		if (evt instanceof GatherDataEvent.Server) {
			evt.addProvider(new BotaniaExtrasRecipeProvider(output, lookup));
			evt.addProvider(new BotaniaExtrasLootProvider(output, lookup));
			evt.addProvider(new BotaniaExtrasUtilityRecipeProvider(output, lookup));
			evt.addProvider(new BotaniaExtrasUtilityLootProvider(output, lookup));
			evt.addProvider(new BotaniaExtrasSpawnProvider(output));
		}
		if (evt instanceof GatherDataEvent.Client) {
			evt.addProvider(new BotaniaExtrasModelProvider(output));
			evt.addProvider(new BotaniaExtrasUtilityModelProvider(output));
		}
	}
}
