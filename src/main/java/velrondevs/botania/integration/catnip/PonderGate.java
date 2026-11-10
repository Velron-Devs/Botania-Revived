package velrondevs.botania.integration.catnip;

import net.neoforged.fml.ModList;

public final class PonderGate {
	private PonderGate() {}

	public static boolean enabled() {
		ModList mods = ModList.get();
		return mods.isLoaded("ponder") && (mods.isLoaded("create") || Boolean.getBoolean("botania.ponder"));
	}
}
