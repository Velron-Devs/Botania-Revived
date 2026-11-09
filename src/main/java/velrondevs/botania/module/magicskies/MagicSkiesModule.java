package velrondevs.botania.module.magicskies;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.datagen.providers.magicskies.MagicSkiesDatagen;
import velrondevs.botania.module.BotaniaModule;
import velrondevs.botania.module.ModuleContext;

import java.util.concurrent.CompletableFuture;

public final class MagicSkiesModule extends BotaniaModule {
	public static final String ID = "skybox";
	public static final Identifier DIMENSION_ID = Identifier.fromNamespaceAndPath(LibMisc.MOD_ID, ID);
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
	public void gatherData(GatherDataEvent evt, CompletableFuture<HolderLookup.Provider> lookup) {
		MagicSkiesDatagen.gatherData(evt);
	}
}
