package velrondevs.botania.module.asgard;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import velrondevs.botania.datagen.providers.asgard.AsgardDatagen;
import velrondevs.botania.datagen.providers.asgard.AsgardTagEntries;
import velrondevs.botania.module.BotaniaModule;
import velrondevs.botania.module.ModuleContext;
import velrondevs.botania.module.ModuleTagSink;
import velrondevs.botania.module.asgard.client.AsgardClient;

import java.util.concurrent.CompletableFuture;

public final class AsgardModule extends BotaniaModule {
	public static final String ID = "asg";
	public static final AsgardModule INSTANCE = new AsgardModule();

	private AsgardModule() {
		super(ID, "Asgard");
	}

	@Override
	public void register(ModuleContext ctx) {
		ctx.bindBlocks(AsgardFlowers::registerBlocks);
		ctx.bindItems(AsgardFlowers::registerItemBlocks);
		ctx.bind(Registries.BLOCK_ENTITY_TYPE, AsgardFlowers::registerBlockEntities);
		ctx.creativeTab(() -> new ItemStack(AsgardFlowers.asgardandelion), false, output -> {});
		ctx.modBus().addListener((FMLCommonSetupEvent e) -> e.enqueueWork(AsgardFlowers::registerPottedPlants));
	}

	@Override
	public void registerClient(ModuleContext ctx) {
		AsgardClient.init(ctx.modBus());
	}

	@Override
	public void gatherData(GatherDataEvent evt, CompletableFuture<HolderLookup.Provider> lookup) {
		AsgardDatagen.gatherData(evt, lookup);
	}

	@Override
	public void addBlockTags(ModuleTagSink<Block> sink) {
		AsgardTagEntries.addBlockTags(sink);
	}
}
