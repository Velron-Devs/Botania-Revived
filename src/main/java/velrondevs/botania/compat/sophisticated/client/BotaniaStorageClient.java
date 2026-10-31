package velrondevs.botania.compat.sophisticated.client;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.p3pp3rf1y.sophisticatedstorage.block.BarrelBlock;
import net.p3pp3rf1y.sophisticatedstorage.block.BarrelBlockClientExtensions;
import net.p3pp3rf1y.sophisticatedstorage.block.StorageBlockEntity;
import net.p3pp3rf1y.sophisticatedstorage.block.StorageWrapper;
import net.p3pp3rf1y.sophisticatedstorage.block.WoodStorageBlockEntity;
import net.p3pp3rf1y.sophisticatedstorage.client.GenericWoodStorageTintCache;
import net.p3pp3rf1y.sophisticatedstorage.client.render.ChestItemRenderer;
import net.p3pp3rf1y.sophisticatedstorage.item.StorageBlockItem;
import net.p3pp3rf1y.sophisticatedstorage.item.WoodStorageBlockItem;
import net.p3pp3rf1y.sophisticatedstorage.util.GenericWoodStorageHelper;

import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.compat.sophisticated.storage.BotaniaStorageRegistry;
import velrondevs.botania.compat.sophisticated.storage.BotaniaStorageTier;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public final class BotaniaStorageClient {
	private static final int MAIN_TINT = 1000;
	private static final int ACCENT_TINT = 1001;
	private static final Map<ChestType, Map<BotaniaStorageTier, Material>> CHEST_MATERIALS = new EnumMap<>(ChestType.class);

	private BotaniaStorageClient() {}

	public static void init(IEventBus modBus) {
		modBus.addListener(BotaniaStorageClient::registerExtensions);
		modBus.addListener(BotaniaStorageClient::registerBlockColors);
		modBus.addListener(BotaniaStorageClient::registerItemColors);
	}

	public static Material chestMaterial(Block block, ChestType chestType) {
		BotaniaStorageTier tier = BotaniaStorageRegistry.tierOf(block);
		if (tier == null) {
			return null;
		}
		return CHEST_MATERIALS.computeIfAbsent(chestType, type -> new EnumMap<>(BotaniaStorageTier.class)).computeIfAbsent(tier, t -> {
			String prefix = chestType == ChestType.LEFT ? "left_" : chestType == ChestType.RIGHT ? "right_" : "";
			return new Material(Sheets.CHEST_SHEET, ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "entity/chest/" + prefix + t.id() + "_tier"));
		});
	}

	private static void registerExtensions(RegisterClientExtensionsEvent event) {
		for (BarrelBlock barrel : BotaniaStorageRegistry.barrels()) {
			event.registerBlock(new BarrelBlockClientExtensions(barrel), barrel);
		}
		event.registerItem(ChestItemRenderer.getItemRenderProperties(), BotaniaStorageRegistry.chestItems().toArray(Item[]::new));
	}

	private static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		event.register(BotaniaStorageClient::barrelColor, BotaniaStorageRegistry.barrels().toArray(Block[]::new));
		event.register(BotaniaStorageClient::chestColor, BotaniaStorageRegistry.chests().toArray(Block[]::new));
	}

	private static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		event.register(BotaniaStorageClient::barrelItemColor, BotaniaStorageRegistry.barrelItems().toArray(ItemLike[]::new));
	}

	private static int barrelColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int index) {
		if (index != MAIN_TINT && index != ACCENT_TINT) {
			return -1;
		}
		return storageColor(level, pos, index == MAIN_TINT);
	}

	private static int chestColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int index) {
		return index == 0 ? storageColor(level, pos, true) : -1;
	}

	private static int storageColor(BlockAndTintGetter level, BlockPos pos, boolean main) {
		if (level == null || pos == null || !(level.getBlockEntity(pos) instanceof StorageBlockEntity storage)) {
			return -1;
		}
		StorageWrapper wrapper = storage.getStorageWrapper();
		int color = main ? wrapper.getMainColor() : wrapper.getAccentColor();
		if (color != -1) {
			return color;
		}
		if (storage instanceof WoodStorageBlockEntity wood) {
			return genericWoodColor(wood.getWoodType(), main);
		}
		return -1;
	}

	private static int barrelItemColor(ItemStack stack, int index) {
		if (index != MAIN_TINT && index != ACCENT_TINT) {
			return -1;
		}
		boolean main = index == MAIN_TINT;
		Optional<Integer> color = main ? StorageBlockItem.getMainColorFromComponentHolder(stack) : StorageBlockItem.getAccentColorFromComponentHolder(stack);
		return color.orElseGet(() -> genericWoodColor(WoodStorageBlockItem.getWoodType(stack), main));
	}

	private static int genericWoodColor(Optional<WoodType> woodType, boolean main) {
		return woodType.filter(GenericWoodStorageHelper::isGenericWood)
				.map(wood -> main ? GenericWoodStorageTintCache.getMainColor(wood) : GenericWoodStorageTintCache.getAccentColor(wood))
				.orElse(-1);
	}
}
