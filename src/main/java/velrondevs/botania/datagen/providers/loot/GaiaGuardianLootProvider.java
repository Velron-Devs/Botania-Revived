package velrondevs.botania.datagen.providers.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import velrondevs.botania.common.loot.BindUuid;
import velrondevs.botania.common.loot.EnableRelics;
import velrondevs.botania.common.loot.RealPlayerCondition;
import velrondevs.botania.common.loot.TrueGuardianKiller;
import velrondevs.botania.registry.BotaniaItems;

import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class GaiaGuardianLootProvider implements LootTableSubProvider {
	public static final ResourceKey<LootTable> GAIA_GUARDIAN = key("gaia_guardian");
	public static final ResourceKey<LootTable> GAIA_GUARDIAN_2 = key("gaia_guardian_2");
	public static final ResourceKey<LootTable> LOTUSES = key("gaia_guardian/lotuses");
	public static final ResourceKey<LootTable> MATERIALS = key("gaia_guardian/materials");
	public static final ResourceKey<LootTable> RUNES = key("gaia_guardian/runes");
	public static final ResourceKey<LootTable> FEL_BLAZE = key("fel_blaze");

	private final HolderLookup.Provider registries;

	public GaiaGuardianLootProvider(HolderLookup.Provider registries) {
		this.registries = registries;
	}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		output.accept(GAIA_GUARDIAN, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("life_essence")
						.when(() -> RealPlayerCondition.INSTANCE)
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(BotaniaItems.lifeEssence)
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(6)))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(8))
										.when(() -> TrueGuardianKiller.INSTANCE))))
				.withPool(LootPool.lootPool().name("music_disc")
						.when(() -> RealPlayerCondition.INSTANCE)
						.when(LootItemRandomChanceCondition.randomChance(0.2F))
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(BotaniaItems.recordGaia1))));

		output.accept(GAIA_GUARDIAN_2, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("life_essence")
						.when(() -> RealPlayerCondition.INSTANCE)
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(BotaniaItems.lifeEssence)
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(10)))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(16))
										.when(() -> TrueGuardianKiller.INSTANCE))))
				.withPool(LootPool.lootPool().name("black_lotuses")
						.when(() -> RealPlayerCondition.INSTANCE)
						.when(LootItemRandomChanceCondition.randomChance(0.5F))
						.setRolls(ConstantValue.exactly(1))
						.add(NestedLootTable.lootTableReference(LOTUSES)))
				.withPool(LootPool.lootPool().name("ancient_wills")
						.when(() -> RealPlayerCondition.INSTANCE)
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(BotaniaItems.ancientWillAhrim))
						.add(LootItem.lootTableItem(BotaniaItems.ancientWillDharok))
						.add(LootItem.lootTableItem(BotaniaItems.ancientWillGuthan))
						.add(LootItem.lootTableItem(BotaniaItems.ancientWillTorag))
						.add(LootItem.lootTableItem(BotaniaItems.ancientWillVerac))
						.add(LootItem.lootTableItem(BotaniaItems.ancientWillKaril)))
				.withPool(LootPool.lootPool().name("overgrowth_seeds")
						.when(() -> RealPlayerCondition.INSTANCE)
						.when(LootItemRandomChanceCondition.randomChance(0.25F))
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(BotaniaItems.overgrowthSeed)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))))
				.withPool(LootPool.lootPool().name("materials")
						.when(() -> RealPlayerCondition.INSTANCE)
						.setRolls(ConstantValue.exactly(1))
						.add(NestedLootTable.lootTableReference(MATERIALS)))
				.withPool(LootPool.lootPool().name("runes")
						.when(() -> RealPlayerCondition.INSTANCE)
						.setRolls(UniformGenerator.between(1, 6))
						.add(NestedLootTable.lootTableReference(RUNES)))
				.withPool(LootPool.lootPool().name("pinkinator")
						.when(() -> RealPlayerCondition.INSTANCE)
						.when(LootItemRandomChanceCondition.randomChance(0.2F))
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(BotaniaItems.pinkinator)))
				.withPool(LootPool.lootPool().name("music_discs")
						.when(() -> RealPlayerCondition.INSTANCE)
						.when(LootItemRandomChanceCondition.randomChance(0.44F))
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(BotaniaItems.recordGaia2).setWeight(14))
						.add(LootItem.lootTableItem(Items.MUSIC_DISC_13).setWeight(15))
						.add(LootItem.lootTableItem(Items.MUSIC_DISC_WAIT).setWeight(15)))
				.withPool(LootPool.lootPool().name("relics")
						.when(() -> RealPlayerCondition.INSTANCE)
						.when(() -> EnableRelics.INSTANCE)
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(BotaniaItems.dice)
								.apply(BindUuid.builder()))));

		output.accept(LOTUSES, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("black_lotuses")
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(BotaniaItems.blackerLotus).setWeight(3).setQuality(1))
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(7)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))));

		output.accept(MATERIALS, LootTable.lootTable()
				.withPool(materialPool("manasteel", 0.9F, BotaniaItems.manaSteel, 16, 27))
				.withPool(materialPool("mana_pearl", 0.7F, BotaniaItems.manaPearl, 8, 13))
				.withPool(materialPool("mana_diamond", 0.5F, BotaniaItems.manaDiamond, 4, 6)));

		output.accept(RUNES, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("runes")
						.setRolls(ConstantValue.exactly(1))
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
						.add(LootItem.lootTableItem(BotaniaItems.runeWater))
						.add(LootItem.lootTableItem(BotaniaItems.runeFire))
						.add(LootItem.lootTableItem(BotaniaItems.runeEarth))
						.add(LootItem.lootTableItem(BotaniaItems.runeAir))
						.add(LootItem.lootTableItem(BotaniaItems.runeSpring))
						.add(LootItem.lootTableItem(BotaniaItems.runeSummer))
						.add(LootItem.lootTableItem(BotaniaItems.runeAutumn))
						.add(LootItem.lootTableItem(BotaniaItems.runeWinter))
						.add(LootItem.lootTableItem(BotaniaItems.runeMana))
						.add(LootItem.lootTableItem(BotaniaItems.runeLust))
						.add(LootItem.lootTableItem(BotaniaItems.runeGluttony))
						.add(LootItem.lootTableItem(BotaniaItems.runeGreed))
						.add(LootItem.lootTableItem(BotaniaItems.runeSloth))
						.add(LootItem.lootTableItem(BotaniaItems.runeWrath))
						.add(LootItem.lootTableItem(BotaniaItems.runeEnvy))
						.add(LootItem.lootTableItem(BotaniaItems.runePride))));

		output.accept(FEL_BLAZE, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("main")
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.BLAZE_POWDER)
								.when(InvertedLootItemCondition.invert(LootItemKilledByPlayerCondition.killedByPlayer()))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(6))))
						.add(LootItem.lootTableItem(Items.BLAZE_POWDER)
								.when(LootItemKilledByPlayerCondition.killedByPlayer())
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(10)))
								.apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0, 6))))));
	}

	private static LootPool.Builder materialPool(String name, float chance, ItemLike item, int min, int max) {
		return LootPool.lootPool().name(name)
				.when(LootItemRandomChanceCondition.randomChance(chance))
				.setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(item)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))));
	}

	private static ResourceKey<LootTable> key(String path) {
		return ResourceKey.create(Registries.LOOT_TABLE, prefix(path));
	}
}
