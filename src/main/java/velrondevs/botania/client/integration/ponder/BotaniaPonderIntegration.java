package velrondevs.botania.client.integration.ponder;

import net.createmod.ponder.foundation.PonderIndex;

public final class BotaniaPonderIntegration {
	private BotaniaPonderIntegration() {}

	public static void init() {
		PonderIndex.addPlugin(new BotaniaPonderPlugin());
	}
}
