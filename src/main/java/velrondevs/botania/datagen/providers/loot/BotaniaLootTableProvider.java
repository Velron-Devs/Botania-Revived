package velrondevs.botania.datagen.providers.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BotaniaLootTableProvider extends LootTableProvider {
	private static final String UNKNOWN_VANILLA_TABLE = "Unknown loot table called minecraft:";

	public BotaniaLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, Set.of(), List.of(
				new SubProviderEntry(BlockLootProvider::new, LootContextParamSets.BLOCK),
				new SubProviderEntry(EntityLootProvider::new, LootContextParamSets.ENTITY),
				new SubProviderEntry(GaiaGuardianLootProvider::new, LootContextParamSets.ALL_PARAMS),
				new SubProviderEntry(lookup -> new DiceOfFateLootProvider(), LootContextParamSets.GIFT),
				new SubProviderEntry(lookup -> new ChestInjectLootProvider(), LootContextParamSets.ALL_PARAMS),
				new SubProviderEntry(LooniumStructureLootProvider::new, LootContextParamSets.ALL_PARAMS),
				new SubProviderEntry(LooniumEquipmentLootProvider::new, LootContextParamSets.SELECTOR)
		), registries);
	}

	@Override
	protected void validate(WritableRegistry<LootTable> registry, ValidationContext context, ProblemReporter.Collector collector) {
		ProblemReporter.Collector problems = new ProblemReporter.Collector();
		super.validate(registry, new ValidationContext(problems, LootContextParamSets.ALL_PARAMS, context.resolver()), problems);
		problems.get().forEach((path, problem) -> {
			if (!problem.startsWith(UNKNOWN_VANILLA_TABLE)) {
				collector.forChild(path).report(problem);
			}
		});
	}
}
