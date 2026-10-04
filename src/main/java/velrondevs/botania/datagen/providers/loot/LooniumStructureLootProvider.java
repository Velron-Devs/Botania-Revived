package velrondevs.botania.datagen.providers.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.loot.BotaniaLootTables;

import java.util.*;
import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class LooniumStructureLootProvider implements LootTableSubProvider {

	public static final EnumSet<VillageLoot> PLAINS_VILLAGE_LOOT = EnumSet
			.of(VillageLoot.CARTOGRAPHER, VillageLoot.FISHER, VillageLoot.TANNERY, VillageLoot.WEAPONSMITH);
	public static final EnumSet<VillageLoot> DESERT_VILLAGE_LOOT = EnumSet
			.of(VillageLoot.TEMPLE, VillageLoot.TOOLSMITH, VillageLoot.WEAPONSMITH);
	public static final EnumSet<VillageLoot> SAVANNA_VILLAGE_LOOT = EnumSet
			.of(VillageLoot.BUTCHER, VillageLoot.CARTOGRAPHER, VillageLoot.MASON, VillageLoot.TANNERY, VillageLoot.WEAPONSMITH);
	public static final EnumSet<VillageLoot> SNOWY_VILLAGE_LOOT = EnumSet
			.of(VillageLoot.ARMORER, VillageLoot.CARTOGRAPHER, VillageLoot.SHEPHERD, VillageLoot.TANNERY, VillageLoot.WEAPONSMITH);
	public static final EnumSet<VillageLoot> TAIGA_VILLAGE_LOOT = EnumSet
			.of(VillageLoot.CARTOGRAPHER, VillageLoot.FLETCHER, VillageLoot.TANNERY, VillageLoot.TOOLSMITH, VillageLoot.WEAPONSMITH);

	private final HolderLookup.Provider registries;

	public LooniumStructureLootProvider(HolderLookup.Provider registries) {
		this.registries = registries;
	}

	public static ResourceLocation getStructureId(ResourceKey<Structure> structureKey) {
		return getStructureId(structureKey.location());
	}

	public static ResourceLocation getStructureId(ResourceLocation structureId) {
		return prefix("%s/%s".formatted(structureId.getNamespace(), structureId.getPath()));
	}

	public static ResourceKey<LootTable> getStructureLootTable(ResourceKey<Structure> structureKey) {
		return ResourceKey.create(Registries.LOOT_TABLE, getStructureId(structureKey).withPrefix("loonium/"));
	}

	@Override
	public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		Map<ResourceKey<LootTable>, LootTable.Builder> tables = new HashMap<>();
		addLootTables(tables);
		tables.forEach(output);
	}

	private void addLootTables(Map<ResourceKey<LootTable>, LootTable.Builder> tables) {

		tables.put(BotaniaLootTables.LOONIUM_DEFAULT_LOOT, buildDelegateLootTable(BuiltInLootTables.SIMPLE_DUNGEON));

		tables.put(getStructureLootTable(BuiltinStructures.ANCIENT_CITY),
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.ANCIENT_CITY).setWeight(9))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.ANCIENT_CITY_ICE_BOX).setWeight(1))
				)
		);
		tables.put(getStructureLootTable(BuiltinStructures.BASTION_REMNANT),
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.BASTION_BRIDGE).setWeight(1))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.BASTION_HOGLIN_STABLE).setWeight(1))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.BASTION_TREASURE).setWeight(1))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.BASTION_OTHER).setWeight(7))
				)
		);
		tables.put(getStructureLootTable(BuiltinStructures.BURIED_TREASURE), buildDelegateLootTable(BuiltInLootTables.BURIED_TREASURE));
		tables.put(getStructureLootTable(BuiltinStructures.DESERT_PYRAMID),
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.DESERT_PYRAMID).setWeight(37))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY).setWeight(2))

						.add(NestedLootTable.lootTableReference(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY))
				)
		);
		tables.put(getStructureLootTable(BuiltinStructures.END_CITY),
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.END_CITY_TREASURE).setWeight(49))
						.add(LootItem.lootTableItem(Items.ELYTRA))
				)
		);
		tables.put(getStructureLootTable(BuiltinStructures.FORTRESS), buildDelegateLootTable(BuiltInLootTables.NETHER_BRIDGE));

		tables.put(getStructureLootTable(BuiltinStructures.JUNGLE_TEMPLE),
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.JUNGLE_TEMPLE).setWeight(9))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER))
				)
		);
		tables.put(getStructureLootTable(BuiltinStructures.MINESHAFT), buildDelegateLootTable(BuiltInLootTables.ABANDONED_MINESHAFT));
		tables.put(getStructureLootTable(BuiltinStructures.MINESHAFT_MESA), buildDelegateLootTable(BuiltInLootTables.ABANDONED_MINESHAFT));
		tables.put(getStructureLootTable(BuiltinStructures.OCEAN_MONUMENT),
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(EntityType.ELDER_GUARDIAN.getDefaultLootTable()).setWeight(5))

						.add(LootItem.lootTableItem(Items.WET_SPONGE))

				)
		);
		tables.put(getStructureLootTable(BuiltinStructures.OCEAN_RUIN_COLD),
				buildOceanRuinLootTable(BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY)
		);
		tables.put(getStructureLootTable(BuiltinStructures.OCEAN_RUIN_WARM),
				buildOceanRuinLootTable(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY)
		);
		tables.put(getStructureLootTable(BuiltinStructures.PILLAGER_OUTPOST), buildDelegateLootTable(BuiltInLootTables.PILLAGER_OUTPOST));
		tables.put(getStructureLootTable(BuiltinStructures.RUINED_PORTAL_DESERT), buildDelegateLootTable(BuiltInLootTables.RUINED_PORTAL));
		tables.put(getStructureLootTable(BuiltinStructures.RUINED_PORTAL_JUNGLE), buildDelegateLootTable(BuiltInLootTables.RUINED_PORTAL));
		tables.put(getStructureLootTable(BuiltinStructures.RUINED_PORTAL_MOUNTAIN), buildDelegateLootTable(BuiltInLootTables.RUINED_PORTAL));
		tables.put(getStructureLootTable(BuiltinStructures.RUINED_PORTAL_NETHER), buildDelegateLootTable(BuiltInLootTables.RUINED_PORTAL));
		tables.put(getStructureLootTable(BuiltinStructures.RUINED_PORTAL_OCEAN), buildDelegateLootTable(BuiltInLootTables.RUINED_PORTAL));
		tables.put(getStructureLootTable(BuiltinStructures.RUINED_PORTAL_STANDARD), buildDelegateLootTable(BuiltInLootTables.RUINED_PORTAL));
		tables.put(getStructureLootTable(BuiltinStructures.RUINED_PORTAL_SWAMP), buildDelegateLootTable(BuiltInLootTables.RUINED_PORTAL));
		tables.put(getStructureLootTable(BuiltinStructures.SHIPWRECK), buildShipwreckLootTable());
		tables.put(getStructureLootTable(BuiltinStructures.SHIPWRECK_BEACHED), buildShipwreckLootTable());
		tables.put(getStructureLootTable(BuiltinStructures.STRONGHOLD),

				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.STRONGHOLD_CORRIDOR).setWeight(4))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.STRONGHOLD_CROSSING).setWeight(6))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.STRONGHOLD_LIBRARY).setWeight(3))
				)
		);

		tables.put(getStructureLootTable(BuiltinStructures.TRAIL_RUINS),

				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON).setWeight(9))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE))
				)
		);
		tables.put(getStructureLootTable(BuiltinStructures.VILLAGE_PLAINS),
				buildVillageLootTable(BuiltInLootTables.VILLAGE_PLAINS_HOUSE, PLAINS_VILLAGE_LOOT)
		);
		tables.put(getStructureLootTable(BuiltinStructures.VILLAGE_DESERT),
				buildVillageLootTable(BuiltInLootTables.VILLAGE_DESERT_HOUSE, DESERT_VILLAGE_LOOT)
		);
		tables.put(getStructureLootTable(BuiltinStructures.VILLAGE_SAVANNA),
				buildVillageLootTable(BuiltInLootTables.VILLAGE_SAVANNA_HOUSE, SAVANNA_VILLAGE_LOOT)
		);
		tables.put(getStructureLootTable(BuiltinStructures.VILLAGE_SNOWY),
				buildVillageLootTable(BuiltInLootTables.VILLAGE_SNOWY_HOUSE, SNOWY_VILLAGE_LOOT)
		);
		tables.put(getStructureLootTable(BuiltinStructures.VILLAGE_TAIGA),
				buildVillageLootTable(BuiltInLootTables.VILLAGE_TAIGA_HOUSE, TAIGA_VILLAGE_LOOT)
		);
		tables.put(getStructureLootTable(BuiltinStructures.WOODLAND_MANSION),
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.WOODLAND_MANSION).setWeight(99))
						.add(LootItem.lootTableItem(Items.TOTEM_OF_UNDYING).setWeight(1))
				)
		);
	}

	public static LootTable.Builder buildVillageLootTable(ResourceKey<LootTable> house, Set<VillageLoot> villageLootSet) {
		LootPool.Builder lootPool = LootPool.lootPool().add(NestedLootTable.lootTableReference(house).setWeight(3));
		for (VillageLoot loot : villageLootSet) {
			lootPool.add(NestedLootTable.lootTableReference(loot.lootTable));
		}
		return LootTable.lootTable().withPool(lootPool);
	}

	@NotNull
	public static LootTable.Builder buildShipwreckLootTable() {
		return LootTable.lootTable().withPool(LootPool.lootPool()
				.add(NestedLootTable.lootTableReference(BuiltInLootTables.SHIPWRECK_MAP))
				.add(NestedLootTable.lootTableReference(BuiltInLootTables.SHIPWRECK_SUPPLY))
				.add(NestedLootTable.lootTableReference(BuiltInLootTables.SHIPWRECK_TREASURE))
		);
	}

	@NotNull
	public static LootTable.Builder buildDelegateLootTable(ResourceKey<LootTable> reference) {
		return LootTable.lootTable().withPool(LootPool.lootPool()
				.add(NestedLootTable.lootTableReference(reference))
		);
	}

	@NotNull
	public static LootTable.Builder buildOceanRuinLootTable(ResourceKey<LootTable> archaeology) {

		return LootTable.lootTable().withPool(LootPool.lootPool()

				.add(NestedLootTable.lootTableReference(BuiltInLootTables.UNDERWATER_RUIN_BIG))
				.add(NestedLootTable.lootTableReference(BuiltInLootTables.UNDERWATER_RUIN_SMALL).setWeight(8))
				.add(NestedLootTable.lootTableReference(archaeology))
		);
	}

	public enum VillageLoot {
		WEAPONSMITH(BuiltInLootTables.VILLAGE_WEAPONSMITH),
		TOOLSMITH(BuiltInLootTables.VILLAGE_TOOLSMITH),
		ARMORER(BuiltInLootTables.VILLAGE_ARMORER),
		CARTOGRAPHER(BuiltInLootTables.VILLAGE_CARTOGRAPHER),
		MASON(BuiltInLootTables.VILLAGE_MASON),
		SHEPHERD(BuiltInLootTables.VILLAGE_SHEPHERD),
		BUTCHER(BuiltInLootTables.VILLAGE_BUTCHER),
		FLETCHER(BuiltInLootTables.VILLAGE_FLETCHER),
		FISHER(BuiltInLootTables.VILLAGE_FISHER),
		TANNERY(BuiltInLootTables.VILLAGE_TANNERY),
		TEMPLE(BuiltInLootTables.VILLAGE_TEMPLE);

		public final ResourceKey<LootTable> lootTable;

		VillageLoot(ResourceKey<LootTable> lootTable) {
			this.lootTable = lootTable;
		}
	}
}
