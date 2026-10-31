package velrondevs.botania.compat.sophisticated;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import velrondevs.botania.compat.sophisticated.backpack.BotaniaBackpackData;
import velrondevs.botania.compat.sophisticated.storage.BotaniaStorageData;
import velrondevs.botania.module.BotaniaModule;
import velrondevs.botania.module.ModuleContext;
import velrondevs.botania.module.ModuleTagSink;

import java.util.concurrent.CompletableFuture;

public final class SophisticatedModule extends BotaniaModule {
	public static final String ID = "sophisticated";
	public static final String CORE = "sophisticatedcore";
	public static final String BACKPACKS = "sophisticatedbackpacks";
	public static final String STORAGE = "sophisticatedstorage";
	public static final SophisticatedModule INSTANCE = new SophisticatedModule();

	private SophisticatedModule() {
		super(ID, "Sophisticated");
	}

	@Override
	public boolean isAvailable() {
		return isModLoaded(CORE) && (isModLoaded(BACKPACKS) || isModLoaded(STORAGE));
	}

	public boolean hasBackpacks() {
		return isModLoaded(BACKPACKS);
	}

	public boolean hasStorage() {
		return isModLoaded(STORAGE);
	}

	@Override
	public void register(ModuleContext ctx) {
		SophisticatedCompat.register(ctx);
	}

	@Override
	public void registerClient(ModuleContext ctx) {
		SophisticatedCompat.registerClient(ctx);
	}

	@Override
	public void addBlockTags(ModuleTagSink<Block> sink) {
		BotaniaStorageData.addBlockTags(sink);
	}

	@Override
	public void addItemTags(ModuleTagSink<Item> sink) {
		BotaniaStorageData.addItemTags(sink);
	}

	@Override
	public void addAccessoryTags(ModuleTagSink<Item> sink) {
		BotaniaBackpackData.addBackTags(sink);
	}

	@Override
	public void gatherData(GatherDataEvent evt, CompletableFuture<HolderLookup.Provider> lookup) {
		SophisticatedDataProvider.gatherData(evt);
	}
}
