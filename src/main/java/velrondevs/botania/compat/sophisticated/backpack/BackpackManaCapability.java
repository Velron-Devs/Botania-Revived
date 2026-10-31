package velrondevs.botania.compat.sophisticated.backpack;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlockEntity;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModBlocks;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;

import velrondevs.botania.api.BotaniaCapabilities;
import velrondevs.botania.compat.sophisticated.StorageManaReceiver;

public final class BackpackManaCapability {
	private BackpackManaCapability() {}

	public static void register(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(BotaniaCapabilities.MANA_RECEIVER, ModBlocks.BACKPACK_TILE_TYPE.get(),
				(backpack, side) -> new StorageManaReceiver(backpack, BackpackManaCapability::wrapper));
		event.registerBlockEntity(BotaniaCapabilities.SPARK_ATTACHABLE, ModBlocks.BACKPACK_TILE_TYPE.get(),
				(backpack, side) -> new StorageManaReceiver(backpack, BackpackManaCapability::wrapper));
	}

	private static IStorageWrapper wrapper(BlockEntity entity) {
		return entity instanceof BackpackBlockEntity backpack ? backpack.getStorageWrapper() : null;
	}
}
