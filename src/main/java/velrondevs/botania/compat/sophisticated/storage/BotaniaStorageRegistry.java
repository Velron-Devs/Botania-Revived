package velrondevs.botania.compat.sophisticated.storage;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.p3pp3rf1y.sophisticatedstorage.block.BarrelBlock;
import net.p3pp3rf1y.sophisticatedstorage.block.ChestBlock;
import net.p3pp3rf1y.sophisticatedstorage.init.ModBlocks;

import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.compat.sophisticated.SophisticatedConfig;
import velrondevs.botania.module.ModuleContext;

import java.util.Collection;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.Map;

public final class BotaniaStorageRegistry {
	private static final Map<BotaniaStorageTier, BarrelBlock> BARRELS = new EnumMap<>(BotaniaStorageTier.class);
	private static final Map<BotaniaStorageTier, ChestBlock> CHESTS = new EnumMap<>(BotaniaStorageTier.class);
	private static final Map<BotaniaStorageTier, BlockItem> BARREL_ITEMS = new EnumMap<>(BotaniaStorageTier.class);
	private static final Map<BotaniaStorageTier, BlockItem> CHEST_ITEMS = new EnumMap<>(BotaniaStorageTier.class);
	private static final Map<BotaniaStorageTier, Item> UPGRADES = new EnumMap<>(BotaniaStorageTier.class);
	private static final Map<Block, BotaniaStorageTier> TIERS = new IdentityHashMap<>();

	private BotaniaStorageRegistry() {}

	public static void register(ModuleContext ctx) {
		ctx.bindBlocks(consumer -> {
			create();
			for (BotaniaStorageTier tier : BotaniaStorageTier.active()) {
				consumer.accept(BARRELS.get(tier), id(tier.barrelName()));
				consumer.accept(CHESTS.get(tier), id(tier.chestName()));
			}
		});
		ctx.bind(Registries.ITEM, consumer -> {
			create();
			for (BotaniaStorageTier tier : BotaniaStorageTier.active()) {
				consumer.accept(BARREL_ITEMS.get(tier), id(tier.barrelName()));
				consumer.accept(CHEST_ITEMS.get(tier), id(tier.chestName()));
			}
		});
		ctx.bindItems(consumer -> {
			create();
			for (BotaniaStorageTier tier : BotaniaStorageTier.active()) {
				consumer.accept(UPGRADES.get(tier), id(tier.upgradeName()));
			}
		});
		ctx.modBus().addListener((BlockEntityTypeAddBlocksEvent event) -> {
			event.modify(ModBlocks.BARREL_BLOCK_ENTITY_TYPE.get(), BARRELS.values().toArray(Block[]::new));
			event.modify(ModBlocks.CHEST_BLOCK_ENTITY_TYPE.get(), CHESTS.values().toArray(Block[]::new));
		});
	}

	public static void addCreativeTabItems(CreativeModeTab.Output output) {
		for (BotaniaStorageTier tier : BotaniaStorageTier.active()) {
			BotaniaStorageStacks.addCreativeVariants(output, BARRELS.get(tier));
			BotaniaStorageStacks.addCreativeVariants(output, CHESTS.get(tier));
		}
	}

	private static void create() {
		if (!BARRELS.isEmpty()) {
			return;
		}
		for (BotaniaStorageTier tier : BotaniaStorageTier.active()) {
			BarrelBlock barrel = new BarrelBlock(() -> SophisticatedConfig.storageSlots(tier),
					() -> SophisticatedConfig.storageUpgradeSlots(tier), tier.explosionResistance());
			ChestBlock chest = new ChestBlock(() -> SophisticatedConfig.storageSlots(tier),
					() -> SophisticatedConfig.storageUpgradeSlots(tier), tier.explosionResistance());
			BARRELS.put(tier, barrel);
			CHESTS.put(tier, chest);
			BARREL_ITEMS.put(tier, new BotaniaBarrelBlockItem(barrel, properties(tier)));
			CHEST_ITEMS.put(tier, new BotaniaChestBlockItem(chest, properties(tier)));
			UPGRADES.put(tier, new BotaniaStorageTierUpgradeItem(tier));
			TIERS.put(barrel, tier);
			TIERS.put(chest, tier);
		}
	}

	private static Item.Properties properties(BotaniaStorageTier tier) {
		Item.Properties properties = new Item.Properties();
		return tier.fireResistant() ? properties.fireResistant() : properties;
	}

	public static BotaniaStorageTier tierOf(Block block) {
		return TIERS.get(block);
	}

	public static BarrelBlock barrel(BotaniaStorageTier tier) {
		return BARRELS.get(tier);
	}

	public static ChestBlock chest(BotaniaStorageTier tier) {
		return CHESTS.get(tier);
	}

	public static Collection<BarrelBlock> barrels() {
		return BARRELS.values();
	}

	public static Collection<ChestBlock> chests() {
		return CHESTS.values();
	}

	public static Collection<BlockItem> barrelItems() {
		return BARREL_ITEMS.values();
	}

	public static Collection<BlockItem> chestItems() {
		return CHEST_ITEMS.values();
	}

	private static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, path);
	}
}
