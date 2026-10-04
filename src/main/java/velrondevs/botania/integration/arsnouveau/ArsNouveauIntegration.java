package velrondevs.botania.integration.arsnouveau;

import com.hollingsworth.arsnouveau.api.source.AbstractSourceMachine;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;

import velrondevs.botania.api.BotaniaCapabilities;
import velrondevs.botania.common.impl.mana.PlayerManaSources;
import velrondevs.botania.registry.BotaniaBlockEntities;

public final class ArsNouveauIntegration {
	private ArsNouveauIntegration() {}

	public static void init(IEventBus modBus, ModContainer container) {
		ArsNouveauConfig.register(container);
		ArsNouveauAttachments.REGISTER.register(modBus);
		modBus.addListener(ArsNouveauIntegration::registerCapabilities);
		PlayerManaSources.register(new ArsManaBridge());
		NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, ArsManaBridge::onSpellCost);
		NeoForge.EVENT_BUS.addListener(ArsManaBridge::onPlayerTick);
		NeoForge.EVENT_BUS.addListener(ArsManaBridge::onFinishUsingItem);
		NeoForge.EVENT_BUS.addListener(PoolSourceProvider::onManaNetworkEvent);
	}

	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		registerSourceMachine(event, BlockRegistry.SOURCE_JAR_TILE.get());
		registerSourceMachine(event, BlockRegistry.CREATIVE_SOURCE_JAR_TILE.get());
		registerSourceMachine(event, BlockRegistry.ARCANE_RELAY_TILE.get());
		registerSourceMachine(event, BlockRegistry.RELAY_COLLECTOR_TILE.get());
		registerSourceMachine(event, BlockRegistry.RELAY_DEPOSIT_TILE.get());
		registerSourceMachine(event, BlockRegistry.RELAY_WARP_TILE.get());
		registerSourceMachine(event, BlockRegistry.IMBUEMENT_TILE.get());
		event.registerBlockEntity(CapabilityRegistry.SOURCE_CAPABILITY, BotaniaBlockEntities.POOL, (pool, side) -> new PoolSourceCap(pool));
	}

	private static <T extends AbstractSourceMachine> void registerSourceMachine(RegisterCapabilitiesEvent event, BlockEntityType<T> type) {
		event.registerBlockEntity(BotaniaCapabilities.MANA_RECEIVER, type, (machine, side) -> new SourceManaReceiver(machine));
	}
}
