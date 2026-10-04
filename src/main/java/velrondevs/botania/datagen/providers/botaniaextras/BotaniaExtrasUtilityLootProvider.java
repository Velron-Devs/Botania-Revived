package velrondevs.botania.datagen.providers.botaniaextras;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.common.conditions.ICondition;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.module.botaniaextras.BotaniaExtrasModule;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasUtilities;
import velrondevs.botania.registry.BotaniaItems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BotaniaExtrasUtilityLootProvider implements DataProvider {
	private final PackOutput.PathProvider pathProvider;
	private final CompletableFuture<HolderLookup.Provider> registries;

	public BotaniaExtrasUtilityLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "loot_table");
		this.registries = registries;
	}

	@NotNull
	@Override
	public String getName() {
		return "Botania Extras utility loot tables";
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		return registries.thenCompose(lookup -> {
			ICondition condition = BotaniaExtrasModule.INSTANCE.enabledCondition();
			var ops = lookup.createSerializationContext(JsonOps.INSTANCE);
			List<CompletableFuture<?>> futures = new ArrayList<>();

			ResourceLocation funnelId = BuiltInRegistries.BLOCK.getKey(BotaniaExtrasUtilities.livingwoodFunnel);
			LootTable.Builder funnel = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(BotaniaExtrasUtilities.livingwoodFunnel))
					.when(ExplosionCondition.survivesExplosion()));
			futures.add(save(cache, ops, condition, funnel, LootContextParamSets.BLOCK, funnelId.withPrefix("blocks/")));

			LootTable.Builder creeper = LootTable.lootTable()
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
							.add(LootItem.lootTableItem(Items.GUNPOWDER)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))))
					.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
							.add(LootItem.lootTableItem(BotaniaItems.blackLotus))
							.when(LootItemKilledByPlayerCondition.killedByPlayer())
							.when(LootItemRandomChanceCondition.randomChance(0.05F)));
			ResourceLocation creeperTable = BotaniaExtrasUtilities.MANASEAL_CREEPER_ID.withPrefix("entities/");
			futures.add(save(cache, ops, condition, creeper, LootContextParamSets.ENTITY, creeperTable));
			return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
		});
	}

	private CompletableFuture<?> save(CachedOutput cache, DynamicOps<JsonElement> ops, ICondition condition,
			LootTable.Builder builder, LootContextParamSet params, ResourceLocation id) {
		LootTable table = builder.setParamSet(params).build();
		JsonElement json = LootTable.DIRECT_CODEC.encodeStart(ops, table).getOrThrow();
		ICondition.writeConditions(ops, json.getAsJsonObject(), List.of(condition));
		return DataProvider.saveStable(cache, json, pathProvider.json(id));
	}
}
