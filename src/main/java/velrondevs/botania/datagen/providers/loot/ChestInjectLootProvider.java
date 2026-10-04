package velrondevs.botania.datagen.providers.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import velrondevs.botania.registry.BotaniaItems;

import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class ChestInjectLootProvider implements LootTableSubProvider {
	public ChestInjectLootProvider() {}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		output.accept(inject("abandoned_mineshaft"), LootTable.lootTable()
				.withPool(main()
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(35))
						.add(LootItem.lootTableItem(BotaniaItems.overgrowthSeed).setWeight(5))
						.add(EmptyLootItem.emptyItem().setWeight(60))));

		output.accept(inject("desert_pyramid"), LootTable.lootTable()
				.withPool(main()
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(35))
						.add(LootItem.lootTableItem(BotaniaItems.overgrowthSeed).setWeight(5))
						.add(EmptyLootItem.emptyItem().setWeight(60))));

		output.accept(inject("jungle_temple"), LootTable.lootTable()
				.withPool(main()
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(25))
						.add(LootItem.lootTableItem(BotaniaItems.overgrowthSeed).setWeight(5))
						.add(EmptyLootItem.emptyItem().setWeight(70))));

		output.accept(inject("simple_dungeon"), LootTable.lootTable()
				.withPool(main()
						.add(LootItem.lootTableItem(BotaniaItems.manaSteel).setWeight(25)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
						.add(LootItem.lootTableItem(BotaniaItems.lexicon).setWeight(20))
						.add(LootItem.lootTableItem(BotaniaItems.manaBottle).setWeight(10))
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(5))
						.add(LootItem.lootTableItem(BotaniaItems.overgrowthSeed).setWeight(2))
						.add(EmptyLootItem.emptyItem().setWeight(38))));

		output.accept(inject("spawn_bonus_chest"), LootTable.lootTable()
				.withPool(main()
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(10))
						.add(EmptyLootItem.emptyItem().setWeight(90)))
				.withPool(LootPool.lootPool().name("lexicon")
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(BotaniaItems.lexicon))));

		output.accept(inject("stronghold_corridor"), LootTable.lootTable()
				.withPool(main()
						.add(LootItem.lootTableItem(BotaniaItems.manaSteel).setWeight(45)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(10))
						.add(LootItem.lootTableItem(BotaniaItems.overgrowthSeed).setWeight(5))
						.add(EmptyLootItem.emptyItem().setWeight(40))));

		output.accept(inject("village_chest"), LootTable.lootTable()
				.withPool(main()
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(10))
						.add(EmptyLootItem.emptyItem().setWeight(90))));
	}

	private static LootPool.Builder main() {
		return LootPool.lootPool().name("main").setRolls(ConstantValue.exactly(1));
	}

	private static ResourceKey<LootTable> inject(String name) {
		return ResourceKey.create(Registries.LOOT_TABLE, prefix("inject/" + name));
	}
}
