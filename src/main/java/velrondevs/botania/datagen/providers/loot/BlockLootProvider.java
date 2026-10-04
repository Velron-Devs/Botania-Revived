package velrondevs.botania.datagen.providers.loot;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemEnchantmentsPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicates;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.block.BotaniaDoubleFlowerBlock;
import velrondevs.botania.common.block.BotaniaGrassBlock;
import velrondevs.botania.common.lib.LibBlockNames;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.module.BotaniaModules;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BlockLootProvider implements LootTableSubProvider {
	private static final Function<Block, LootTable.Builder> SKIP = b -> {
		throw new RuntimeException("shouldn't be executed");
	};

	private final LootItemCondition.Builder silkTouch;
	private final Map<Block, Function<Block, LootTable.Builder>> functionTable = new HashMap<>();

	public BlockLootProvider(HolderLookup.Provider registries) {
		HolderLookup.RegistryLookup<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
		this.silkTouch = MatchTool.toolMatches(ItemPredicate.Builder.item()
				.withSubPredicate(ItemSubPredicates.ENCHANTMENTS, ItemEnchantmentsPredicate.enchantments(
						List.of(new EnchantmentPredicate(enchantments.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))));

		for (Block b : BuiltInRegistries.BLOCK) {
			ResourceLocation id = BuiltInRegistries.BLOCK.getKey(b);
			if (!LibMisc.MOD_ID.equals(id.getNamespace()) || BotaniaModules.isModuleBlock(b)) {
				continue;
			}
			if (b instanceof SlabBlock) {
				functionTable.put(b, BlockLootProvider::genSlab);
			} else if (b instanceof BotaniaDoubleFlowerBlock) {
				functionTable.put(b, BlockLootProvider::genDoubleFlower);
			} else if (b instanceof BotaniaGrassBlock) {
				functionTable.put(b, this::genAltGrass);
			} else if (b instanceof FlowerPotBlock flowerPot) {
				functionTable.put(b, block -> createPotAndPlantItemTable(flowerPot.getPotted()));
			} else if (id.getPath().matches(LibBlockNames.METAMORPHIC_PREFIX + "\\w+" + "_stone")) {
				functionTable.put(b, this::genMetamorphicStone);
			}
		}

		functionTable.put(BotaniaBlocks.bifrost, BlockLootProvider::empty);
		functionTable.put(BotaniaBlocks.cocoon, BlockLootProvider::empty);
		functionTable.put(BotaniaBlocks.fakeAir, BlockLootProvider::empty);
		functionTable.put(BotaniaBlocks.manaFlame, BlockLootProvider::empty);

		functionTable.put(BotaniaBlocks.cacophonium, b -> genRegular(Blocks.NOTE_BLOCK));
		functionTable.put(BotaniaBlocks.enchantedSoil, b -> genRegular(Blocks.DIRT));
		functionTable.put(BotaniaBlocks.enchanter, b -> genRegular(Blocks.LAPIS_BLOCK));

		functionTable.put(BotaniaBlocks.cellBlock, this::genCellBlock);
		functionTable.put(BotaniaBlocks.root, BlockLootProvider::genRoot);
		functionTable.put(BotaniaBlocks.solidVines, BlockLootProvider::genSolidVine);
		functionTable.put(BotaniaBlocks.tinyPotato, BlockLootProvider::genTinyPotato);

		functionTable.put(BotaniaFlowerBlocks.gourmaryllis, BlockLootProvider::genCopyBlockEntityData);
		functionTable.put(BotaniaFlowerBlocks.gourmaryllisFloating, BlockLootProvider::genCopyBlockEntityData);
		functionTable.put(BotaniaFlowerBlocks.hydroangeas, BlockLootProvider::genCopyBlockEntityData);
		functionTable.put(BotaniaFlowerBlocks.hydroangeasFloating, BlockLootProvider::genCopyBlockEntityData);
		functionTable.put(BotaniaFlowerBlocks.munchdew, BlockLootProvider::genCopyBlockEntityData);
		functionTable.put(BotaniaFlowerBlocks.munchdewFloating, BlockLootProvider::genCopyBlockEntityData);
		functionTable.put(BotaniaFlowerBlocks.rafflowsia, BlockLootProvider::genCopyBlockEntityData);
		functionTable.put(BotaniaFlowerBlocks.rafflowsiaFloating, BlockLootProvider::genCopyBlockEntityData);
		functionTable.put(BotaniaFlowerBlocks.spectrolus, BlockLootProvider::genCopyBlockEntityData);
		functionTable.put(BotaniaFlowerBlocks.spectrolusFloating, BlockLootProvider::genCopyBlockEntityData);
		functionTable.put(BotaniaFlowerBlocks.thermalily, BlockLootProvider::genCopyBlockEntityData);
		functionTable.put(BotaniaFlowerBlocks.thermalilyFloating, BlockLootProvider::genCopyBlockEntityData);
		functionTable.put(BotaniaFlowerBlocks.witherManaRose, BlockLootProvider::genCopyBlockEntityData);
		functionTable.put(BotaniaFlowerBlocks.witherManaRoseFloating, BlockLootProvider::genCopyBlockEntityData);
	}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		for (Block b : BuiltInRegistries.BLOCK) {
			ResourceLocation id = BuiltInRegistries.BLOCK.getKey(b);
			if (!LibMisc.MOD_ID.equals(id.getNamespace()) || BotaniaModules.isModuleBlock(b)) {
				continue;
			}
			Function<Block, LootTable.Builder> func = functionTable.getOrDefault(b, BlockLootProvider::genRegular);
			if (func != SKIP) {
				output.accept(ResourceKey.create(Registries.LOOT_TABLE, id.withPrefix("blocks/")), func.apply(b));
			}
		}
	}

	protected static LootTable.Builder empty(Block b) {
		return LootTable.lootTable();
	}

	@Nullable
	protected static LootTable.Builder skip(Block b) {
		return null;
	}

	protected static LootTable.Builder genCopyBlockEntityData(Block b) {
		LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(b);
		CopyComponentsFunction.Builder func = CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
				.include(DataComponents.BLOCK_ENTITY_DATA);
		LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry)
				.when(ExplosionCondition.survivesExplosion())
				.apply(func);
		return LootTable.lootTable().withPool(pool);
	}

	protected LootTable.Builder genCellBlock(Block b) {
		LootPoolEntryContainer.Builder<?> silk = LootItem.lootTableItem(b)
				.when(silkTouch);
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(silk));
	}

	protected static LootTable.Builder genTinyPotato(Block b) {
		LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(b)
				.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY));
		LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry)
				.when(ExplosionCondition.survivesExplosion());
		return LootTable.lootTable().withPool(pool);
	}

	protected LootTable.Builder genMetamorphicStone(Block b) {
		String cobbleName = BuiltInRegistries.BLOCK.getKey(b).getPath().replaceAll("_stone", "_cobblestone");
		Block cobble = BuiltInRegistries.BLOCK.getOptional(prefix(cobbleName)).get();
		return genSilkDrop(b, cobble);
	}

	protected LootTable.Builder genSilkDrop(ItemLike silkDrop, ItemLike normalDrop) {
		LootPoolEntryContainer.Builder<?> cobbleDrop = LootItem.lootTableItem(normalDrop).when(ExplosionCondition.survivesExplosion());
		LootPoolEntryContainer.Builder<?> stoneDrop = LootItem.lootTableItem(silkDrop).when(silkTouch);

		return LootTable.lootTable().withPool(
				LootPool.lootPool().setRolls(ConstantValue.exactly(1))
						.add(stoneDrop.otherwise(cobbleDrop)));
	}

	protected static LootTable.Builder genSolidVine(Block b) {
		LootPoolEntryContainer.Builder<?> entry = NestedLootTable.lootTableReference(Blocks.VINE.getLootTable());
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry));
	}

	protected static LootTable.Builder genRoot(Block b) {
		LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(BotaniaItems.livingroot)
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(4)))
				.apply(ApplyExplosionDecay.explosionDecay());
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry));
	}

	protected static LootTable.Builder genSlab(Block b) {
		LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(b)
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE))))
				.apply(ApplyExplosionDecay.explosionDecay());
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry));
	}

	protected static LootTable.Builder genDoubleFlower(Block b) {
		var entry = LootItem.lootTableItem(b)
				.when(ExplosionCondition.survivesExplosion())
				.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b)
						.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TallFlowerBlock.HALF, DoubleBlockHalf.LOWER)));
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(entry));
	}

	protected LootTable.Builder genAltGrass(Block b) {
		LootPoolEntryContainer.Builder<?> silk = LootItem.lootTableItem(b)
				.when(silkTouch);
		LootPoolEntryContainer.Builder<?> dirt = LootItem.lootTableItem(Blocks.DIRT)
				.when(ExplosionCondition.survivesExplosion());
		LootPoolEntryContainer.Builder<?> entry = AlternativesEntry.alternatives(silk, dirt);
		LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry);
		return LootTable.lootTable().withPool(pool);
	}

	protected static LootTable.Builder genRegular(Block b) {
		LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(b);
		LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry)
				.when(ExplosionCondition.survivesExplosion());
		return LootTable.lootTable().withPool(pool);
	}

	protected static LootTable.Builder createPotAndPlantItemTable(ItemLike plant) {

		final var potPool = LootPool.lootPool().add(LootItem.lootTableItem(Blocks.FLOWER_POT))
				.setRolls(ConstantValue.exactly(1.0f))
				.when(ExplosionCondition.survivesExplosion());
		final var plantPool = LootPool.lootPool().add(LootItem.lootTableItem(plant))
				.setRolls(ConstantValue.exactly(1.0f))
				.when(ExplosionCondition.survivesExplosion());
		return LootTable.lootTable().withPool(potPool).withPool(plantPool);
	}
}
