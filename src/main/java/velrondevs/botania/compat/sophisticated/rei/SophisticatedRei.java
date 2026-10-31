package velrondevs.botania.compat.sophisticated.rei;

import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;

public final class SophisticatedRei {
	private SophisticatedRei() {}

	public static void registerTransfers(TransferHandlerRegistry registry) {
		registry.register(new StationReiTransferHandler());
	}
}
