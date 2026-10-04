package velrondevs.botania.common.handler;

import net.minecraft.server.level.ServerLevel;

import velrondevs.botania.api.corporea.CorporeaHelper;
import velrondevs.botania.common.impl.corporea.CorporeaHelperImpl;

public final class CommonTickHandler {

	private CommonTickHandler() {}

	public static void onTick(ServerLevel world) {
		((CorporeaHelperImpl) CorporeaHelper.instance()).clearCache();
	}

}
