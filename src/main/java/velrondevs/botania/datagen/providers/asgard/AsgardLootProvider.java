package velrondevs.botania.datagen.providers.asgard;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.conditions.ICondition;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.module.asgard.AsgardFlowers;
import velrondevs.botania.module.asgard.AsgardModule;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AsgardLootProvider implements DataProvider {
	private final PackOutput.PathProvider pathProvider;
	private final CompletableFuture<HolderLookup.Provider> registries;

	public AsgardLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "loot_table");
		this.registries = registries;
	}

	@NotNull
	@Override
	public String getName() {
		return "Botania Asgard loot tables";
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		return registries.thenCompose(lookup -> {
			Map<ResourceKey<LootTable>, LootTable.Builder> tables = new LinkedHashMap<>();
			new BlockLoot(lookup).generate(tables::put);
			ICondition condition = AsgardModule.INSTANCE.enabledCondition();
			var ops = lookup.createSerializationContext(JsonOps.INSTANCE);
			List<CompletableFuture<?>> futures = new ArrayList<>();
			tables.forEach((key, builder) -> {
				LootTable table = builder.setParamSet(LootContextParamSets.BLOCK).build();
				JsonElement json = LootTable.DIRECT_CODEC.encodeStart(ops, table).getOrThrow();
				ICondition.writeConditions(ops, json.getAsJsonObject(), List.of(condition));
				futures.add(DataProvider.saveStable(cache, json, pathProvider.json(key.location())));
			});
			return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
		});
	}

	private static class BlockLoot extends BlockLootSubProvider {
		BlockLoot(HolderLookup.Provider registries) {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return AsgardFlowers.all();
		}

		@Override
		protected void generate() {
			dropSelf(AsgardFlowers.asgardandelion);
			dropSelf(AsgardFlowers.asgardandelionFloating);
			add(AsgardFlowers.asgardandelionPotted, createPotFlowerItemTable(AsgardFlowers.asgardandelion));
		}
	}
}
