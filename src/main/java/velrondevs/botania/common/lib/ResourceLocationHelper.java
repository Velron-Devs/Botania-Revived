package velrondevs.botania.common.lib;

import net.minecraft.resources.Identifier;

public class ResourceLocationHelper {
	public static Identifier prefix(String path) {
		return Identifier.fromNamespaceAndPath(LibMisc.MOD_ID, path);
	}
}
