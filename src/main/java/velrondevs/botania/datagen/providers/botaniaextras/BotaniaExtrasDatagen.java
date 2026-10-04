package velrondevs.botania.datagen.providers.botaniaextras;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public final class BotaniaExtrasDatagen {
	private BotaniaExtrasDatagen() {}

	public static void gatherData(GatherDataEvent evt, CompletableFuture<HolderLookup.Provider> lookup) {
		DataGenerator generator = evt.getGenerator();
		PackOutput output = generator.getPackOutput();
		generator.addProvider(evt.includeServer(), new BotaniaExtrasRecipeProvider(output, lookup));
		generator.addProvider(evt.includeServer(), new BotaniaExtrasLootProvider(output, lookup));
		generator.addProvider(evt.includeClient(), new BotaniaExtrasModelProvider(output));
		generator.addProvider(evt.includeServer(), new BotaniaExtrasUtilityRecipeProvider(output, lookup));
		generator.addProvider(evt.includeServer(), new BotaniaExtrasUtilityLootProvider(output, lookup));
		generator.addProvider(evt.includeServer(), new BotaniaExtrasSpawnProvider(output));
		generator.addProvider(evt.includeClient(), new BotaniaExtrasUtilityModelProvider(output));
	}
}
