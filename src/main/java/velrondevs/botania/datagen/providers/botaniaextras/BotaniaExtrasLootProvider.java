package velrondevs.botania.datagen.providers.botaniaextras;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.conditions.ICondition;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasFlowers;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasMagicWoods;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasModule;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;
import velrondevs.botania.module.botaniaextras.block.DendricSuffuserBlock;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BotaniaExtrasLootProvider implements DataProvider {
	private final PackOutput.PathProvider pathProvider;
	private final CompletableFuture<HolderLookup.Provider> registries;

	public BotaniaExtrasLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "loot_table");
		this.registries = registries;
	}

	@NotNull
	@Override
	public String getName() {
		return "Botania Extras loot tables";
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		return registries.thenCompose(lookup -> {
			Map<ResourceKey<LootTable>, LootTable.Builder> tables = new LinkedHashMap<>();
			new BlockLoot(lookup).generate(tables::put);
			ICondition condition = BotaniaExtrasModule.INSTANCE.enabledCondition();
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
		private static final float[] SAPLING_CHANCES = {0.05F, 0.0625F, 0.083333336F, 0.1F};
		private static final float[] ALT_SAPLING_CHANCES = {0.0167F, 0.02F, 0.025F, 0.033F};

		BlockLoot(HolderLookup.Provider registries) {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			List<Block> blocks = new ArrayList<>(BotaniaExtrasBlocks.all());
			blocks.addAll(BotaniaExtrasWoods.all());
			blocks.addAll(BotaniaExtrasMagicWoods.all());
			blocks.addAll(BotaniaExtrasFlowers.all());
			return blocks;
		}

		@Override
		protected void generate() {
			List<DyeColor> colors = new ArrayList<>(List.of(DyeColor.values()));
			colors.add(null);
			for (DyeColor color : colors) {
				dropSelf(BotaniaExtrasBlocks.getDirt(color));
				Block grass = BotaniaExtrasBlocks.getGrass(color);
				add(grass, createGrassDrops(grass));
				Block tall = BotaniaExtrasBlocks.getTallGrass(color);
				add(tall, createDoublePlantWithSeedDrops(tall, grass));
			}
			dropSelf(BotaniaExtrasBlocks.bifrostFlower);
			add(BotaniaExtrasBlocks.tallBifrostFlower, createSinglePropConditionTable(BotaniaExtrasBlocks.tallBifrostFlower, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
			dropSelf(BotaniaExtrasBlocks.iridescentLantern);
			dropSelf(BotaniaExtrasBlocks.blazeKindling);
			dropSelf(BotaniaExtrasBlocks.shimmerQuartz);
			dropSelf(BotaniaExtrasBlocks.shimmerQuartzChiseled);
			dropSelf(BotaniaExtrasBlocks.shimmerQuartzPillar);
			dropSelf(BotaniaExtrasBlocks.shimmerQuartzStairs);
			add(BotaniaExtrasBlocks.shimmerQuartzSlab, createSlabItemTable(BotaniaExtrasBlocks.shimmerQuartzSlab));

			dropSelf(BotaniaExtrasBlocks.manasteelItemPlatform);
			dropSelf(BotaniaExtrasBlocks.terrasteelItemPlatform);
			dropSelf(BotaniaExtrasBlocks.elementiumItemPlatform);
			dropSelf(BotaniaExtrasBlocks.sonicAmplifier);
			dropSelf(BotaniaExtrasFlowers.crysanthermum);
			dropSelf(BotaniaExtrasFlowers.crysanthermumFloating);
			add(BotaniaExtrasFlowers.crysanthermumPotted, createPotFlowerItemTable(BotaniaExtrasFlowers.crysanthermum));
			LootPool.Builder suffuserPool = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
			for (int i = 0; i <= DendricSuffuserBlock.BIFROST; i++) {
				suffuserPool.add(LootItem.lootTableItem(BotaniaExtrasWoods.sets().get(i).planks())
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(BotaniaExtrasBlocks.dendricSuffuser)
								.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DendricSuffuserBlock.COLOR, i))));
			}
			add(BotaniaExtrasBlocks.dendricSuffuser, LootTable.lootTable().withPool(applyExplosionCondition(BotaniaExtrasBlocks.dendricSuffuser, suffuserPool)));
			for (BotaniaExtrasMagicWoods.MagicSet set : BotaniaExtrasMagicWoods.sets()) {
				dropSelf(set.sapling());
				dropSelf(set.log());
				dropSelf(set.planks());
				dropSelf(set.stairs());
				add(set.slab(), createSlabItemTable(set.slab()));
				add(set.leaves(), createLeavesDrops(set.leaves(), set.sapling(), ALT_SAPLING_CHANCES));
			}

			dropSelf(BotaniaExtrasWoods.sapling);
			for (BotaniaExtrasWoods.WoodSet set : BotaniaExtrasWoods.sets()) {
				dropSelf(set.log());
				dropSelf(set.planks());
				dropSelf(set.stairs());
				add(set.slab(), createSlabItemTable(set.slab()));
				if (set.kind() == BotaniaExtrasWoods.Kind.ALT) {
					add(set.leaves(), createLeavesDrops(set.leaves(), BotaniaExtrasWoods.sapling, ALT_SAPLING_CHANCES));
				} else {
					add(set.leaves(), createLeavesDrops(set.leaves(), BotaniaExtrasWoods.sapling, SAPLING_CHANCES));
				}
			}
		}
	}
}
