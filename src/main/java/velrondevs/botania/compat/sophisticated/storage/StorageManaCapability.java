package velrondevs.botania.compat.sophisticated.storage;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedstorage.block.StorageBlockEntity;
import net.p3pp3rf1y.sophisticatedstorage.init.ModBlocks;

import velrondevs.botania.api.BotaniaCapabilities;
import velrondevs.botania.compat.sophisticated.StorageManaReceiver;

public final class StorageManaCapability {
	private StorageManaCapability() {}

	public static void register(RegisterCapabilitiesEvent event) {
		add(event, ModBlocks.BARREL_BLOCK_ENTITY_TYPE.get());
		add(event, ModBlocks.LIMITED_BARREL_BLOCK_ENTITY_TYPE.get());
		add(event, ModBlocks.CHEST_BLOCK_ENTITY_TYPE.get());
		add(event, ModBlocks.SHULKER_BOX_BLOCK_ENTITY_TYPE.get());
	}

	private static <T extends StorageBlockEntity> void add(RegisterCapabilitiesEvent event, BlockEntityType<T> type) {
		event.registerBlockEntity(BotaniaCapabilities.MANA_RECEIVER, type, (storage, side) -> new StorageManaReceiver(storage, StorageManaCapability::wrapper));
		event.registerBlockEntity(BotaniaCapabilities.SPARK_ATTACHABLE, type, (storage, side) -> new StorageManaReceiver(storage, StorageManaCapability::wrapper));
	}

	private static IStorageWrapper wrapper(BlockEntity entity) {
		return entity instanceof StorageBlockEntity storage ? storage.getStorageWrapper() : null;
	}
}
