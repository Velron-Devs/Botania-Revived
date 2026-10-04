package velrondevs.botania.module.magicskies;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.datagen.providers.magicskies.MagicSkiesDatagen;
import velrondevs.botania.module.BotaniaModule;
import velrondevs.botania.module.ModuleContext;
import velrondevs.botania.module.magicskies.client.MagicSkiesClient;

import java.util.concurrent.CompletableFuture;

public final class MagicSkiesModule extends BotaniaModule {
	public static final String ID = "skybox";
	public static final ResourceLocation DIMENSION_ID = ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, ID);
	public static final MagicSkiesModule INSTANCE = new MagicSkiesModule();

	private MagicSkiesModule() {
		super(ID, "Skybox");
	}

	@Override
	public boolean enabledByDefault() {
		return false;
	}

	@Override
	public void register(ModuleContext ctx) {}

	@Override
	public void registerClient(ModuleContext ctx) {
		MagicSkiesClient.init();
	}

	@Override
	public void gatherData(GatherDataEvent evt, CompletableFuture<HolderLookup.Provider> lookup) {
		MagicSkiesDatagen.gatherData(evt);
	}
}
