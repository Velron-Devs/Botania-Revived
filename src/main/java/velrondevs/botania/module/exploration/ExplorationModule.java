package velrondevs.botania.module.exploration;

import velrondevs.botania.module.BotaniaModule;
import velrondevs.botania.module.ModuleContext;

public final class ExplorationModule extends BotaniaModule {
	public static final String ID = "exploration";
	public static final ExplorationModule INSTANCE = new ExplorationModule();

	private ExplorationModule() {
		super(ID, "Exploration");
	}

	@Override
	public void register(ModuleContext ctx) {}
}
