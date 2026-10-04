package velrondevs.botania.datagen.providers.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import velrondevs.botania.registry.BotaniaItems;

import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class DiceOfFateLootProvider implements LootTableSubProvider {
	public DiceOfFateLootProvider() {}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		output.accept(roll(1), LootTable.lootTable()
				.withPool(lifeEssence(2, 16)));

		output.accept(roll(2), LootTable.lootTable()
				.withPool(lifeEssence(2, 4))
				.withPool(LootPool.lootPool().name("overgrowth_seeds")
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(BotaniaItems.overgrowthSeed)
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));

		output.accept(roll(3), LootTable.lootTable()
				.withPool(lifeEssence(4, 6))
				.withPool(runes(UniformGenerator.between(2, 4))));

		output.accept(roll(4), LootTable.lootTable()
				.withPool(lifeEssence(4, 8))
				.withPool(materials(2, 10)));

		output.accept(roll(5), LootTable.lootTable()
				.withPool(lifeEssence(6, 10))
				.withPool(materials(4, 8))
				.withPool(runes(UniformGenerator.between(0, 3))));

		output.accept(roll(6), LootTable.lootTable()
				.withPool(lifeEssence(8, 12))
				.withPool(materials(5, 10))
				.withPool(runes(UniformGenerator.between(1, 3)))
				.withPool(LootPool.lootPool().name("black_lotuses")
						.setRolls(ConstantValue.exactly(1))
						.add(NestedLootTable.lootTableReference(GaiaGuardianLootProvider.LOTUSES))));
	}

	private static LootPool.Builder lifeEssence(int min, int max) {
		return LootPool.lootPool().name("life_essence")
				.setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(BotaniaItems.lifeEssence)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))));
	}

	private static LootPool.Builder materials(int min, int max) {
		return LootPool.lootPool().name("materials")
				.setRolls(ConstantValue.exactly(1))
				.add(NestedLootTable.lootTableReference(GaiaGuardianLootProvider.MATERIALS))
				.apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
	}

	private static LootPool.Builder runes(NumberProvider rolls) {
		return LootPool.lootPool().name("runes")
				.setRolls(rolls)
				.add(NestedLootTable.lootTableReference(GaiaGuardianLootProvider.RUNES))
				.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)));
	}

	private static ResourceKey<LootTable> roll(int roll) {
		return ResourceKey.create(Registries.LOOT_TABLE, prefix("dice/roll_" + roll));
	}
}
