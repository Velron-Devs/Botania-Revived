package velrondevs.botania.compat.sophisticated.client;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
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
import net.p3pp3rf1y.sophisticatedstorage.util.GenericWoodStorageHelper;

import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.compat.sophisticated.storage.BotaniaStorageRegistry;
import velrondevs.botania.compat.sophisticated.storage.BotaniaStorageTier;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class BotaniaStorageClient {
	private static final Map<ChestType, Map<BotaniaStorageTier, SpriteId>> CHEST_MATERIALS = new EnumMap<>(ChestType.class);

	private BotaniaStorageClient() {}

	public static void init(IEventBus modBus) {
		modBus.addListener(BotaniaStorageClient::registerExtensions);
		modBus.addListener(BotaniaStorageClient::registerBlockColors);
	}

	public static SpriteId chestMaterial(Block block, ChestType chestType) {
		BotaniaStorageTier tier = BotaniaStorageRegistry.tierOf(block);
		if (tier == null) {
			return null;
		}
		return CHEST_MATERIALS.computeIfAbsent(chestType, type -> new EnumMap<>(BotaniaStorageTier.class)).computeIfAbsent(tier, t -> {
			String prefix = chestType == ChestType.LEFT ? "left_" : chestType == ChestType.RIGHT ? "right_" : "";
			return new SpriteId(Sheets.CHEST_SHEET, Identifier.fromNamespaceAndPath(LibMisc.MOD_ID, "entity/chest/" + prefix + t.id() + "_tier"));
		});
	}

	private static void registerExtensions(RegisterClientExtensionsEvent event) {
		for (BarrelBlock barrel : BotaniaStorageRegistry.barrels()) {
			event.registerBlock(new BarrelBlockClientExtensions(barrel), barrel);
		}
	}

	private static void registerBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
		event.register(List.of(new StorageTint(true), new StorageTint(false)), BotaniaStorageRegistry.barrels().toArray(Block[]::new));
		event.register(List.of(new StorageTint(true)), BotaniaStorageRegistry.chests().toArray(Block[]::new));
	}

	private static int storageColor(BlockAndTintGetter level, BlockPos pos, boolean main) {
		if (level == null || pos == null || !(level.getBlockEntity(pos) instanceof StorageBlockEntity storage)) {
			return -1;
		}
		StorageWrapper wrapper = storage.getStorageWrapper();
		int color = main ? wrapper.getMainColor() : wrapper.getAccentColor();
		if (color != -1) {
			return ARGB.opaque(color);
		}
		if (storage instanceof WoodStorageBlockEntity wood) {
			return genericWoodColor(wood.getWoodType(), main);
		}
		return -1;
	}

	private static int genericWoodColor(Optional<WoodType> woodType, boolean main) {
		return woodType.filter(GenericWoodStorageHelper::isGenericWood)
				.map(wood -> main ? GenericWoodStorageTintCache.getMainColor(wood) : GenericWoodStorageTintCache.getAccentColor(wood))
				.orElse(-1);
	}

	private record StorageTint(boolean main) implements BlockTintSource {
		@Override
		public int color(BlockState state) {
			return -1;
		}

		@Override
		public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
			return storageColor(level, pos, main);
		}
	}
}
