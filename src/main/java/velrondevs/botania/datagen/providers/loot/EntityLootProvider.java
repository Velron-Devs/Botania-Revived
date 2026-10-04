package velrondevs.botania.datagen.providers.loot;

import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.FillPlayerHead;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaEntities;
import velrondevs.botania.registry.BotaniaItems;

import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class EntityLootProvider implements LootTableSubProvider {
	public static final ResourceKey<LootTable> ELEMENTIUM_AXE_BEHEADING = ResourceKey.create(Registries.LOOT_TABLE, prefix("elementium_axe_beheading"));
	public static final ResourceKey<LootTable> GHAST_ENDER_AIR_CRYING = ResourceKey.create(Registries.LOOT_TABLE, prefix("ghast_ender_air_crying"));

	private final Holder<Enchantment> looting;

	public EntityLootProvider(HolderLookup.Provider registries) {
		this.looting = registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING);
	}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		output.accept(ELEMENTIUM_AXE_BEHEADING, LootTable.lootTable()
				.withPool(headPool(Items.WITHER_SKELETON_SKULL, 0.1154F, 0.1539F, 0.0385F,
						isEntity(EntityType.WITHER_SKELETON)))
				.withPool(headPool(Items.SKELETON_SKULL, 0.1154F, 0.1539F, 0.0385F,
						LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
								EntityPredicate.Builder.entity().of(EntityTypeTags.SKELETONS)),
						InvertedLootItemCondition.invert(isEntity(EntityType.WITHER_SKELETON))))
				.withPool(headPool(Items.ZOMBIE_HEAD, 0.0769F, 0.1538F, 0.0769F,
						AnyOfCondition.anyOf(isEntity(EntityType.ZOMBIE), isEntity(EntityType.ZOMBIE_VILLAGER),
								isEntity(EntityType.HUSK), isEntity(EntityType.DROWNED))))
				.withPool(headPool(Items.PIGLIN_HEAD, 0.0769F, 0.1538F, 0.0769F,
						AnyOfCondition.anyOf(isEntity(EntityType.PIGLIN), isEntity(EntityType.PIGLIN_BRUTE))))
				.withPool(headPool(Items.CREEPER_HEAD, 0.0769F, 0.1538F, 0.0769F,
						isEntity(EntityType.CREEPER)))
				.withPool(headPool(Items.PLAYER_HEAD, 0.0909F, 0.1818F, 0.0909F,
						isEntity(EntityType.PLAYER))
						.apply(FillPlayerHead.fillPlayerHead(LootContext.EntityTarget.THIS)))
				.withPool(headPool(BotaniaBlocks.gaiaHead, 0.0769F, 0.1538F, 0.0769F,
						isEntity(BotaniaEntities.DOPPLEGANGER))));

		output.accept(GHAST_ENDER_AIR_CRYING, LootTable.lootTable()
				.withPool(LootPool.lootPool().name("main").setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(Items.GHAST_TEAR).setWeight(1))
						.add(EmptyLootItem.emptyItem().setWeight(7))));
	}

	private LootPool.Builder headPool(ItemLike head, float unenchantedChance, float enchantedBase, float perLevelAboveFirst, LootItemCondition.Builder... conditions) {
		LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1))
				.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKING_PLAYER,
						EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment()
								.mainhand(ItemPredicate.Builder.item().of(BotaniaItems.elementiumAxe)))));
		for (LootItemCondition.Builder condition : conditions) {
			pool.when(condition);
		}
		return pool.when(() -> new LootItemRandomChanceWithEnchantedBonusCondition(unenchantedChance,
				new LevelBasedValue.Linear(enchantedBase, perLevelAboveFirst), looting))
				.add(LootItem.lootTableItem(head));
	}

	private static LootItemCondition.Builder isEntity(EntityType<?> type) {
		return LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().of(type));
	}
}
