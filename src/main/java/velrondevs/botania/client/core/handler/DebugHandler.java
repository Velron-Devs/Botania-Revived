package velrondevs.botania.client.core.handler;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import velrondevs.botania.common.handler.ManaNetworkHandler;
import velrondevs.botania.xplat.BotaniaConfig;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.List;

public final class DebugHandler {

	private DebugHandler() {}

	private static final String PREFIX = ChatFormatting.GREEN + "[Botania] " + ChatFormatting.RESET;

	public static void onDrawDebugText(List<String> left) {
		Minecraft mc = Minecraft.getInstance();
		Level world = mc.level;
		if (mc.getDebugOverlay().showDebugScreen() && BotaniaConfig.client().debugInfo()) {
			left.add("");
			String version = XplatAbstractions.INSTANCE.getBotaniaVersion();

			left.add(PREFIX + "(CLIENT) netColl: " + ManaNetworkHandler.instance.getAllCollectorsInWorld(world).size() + ", netPool: " + ManaNetworkHandler.instance.getAllPoolsInWorld(world).size() + ", rv: " + version);

			if (Minecraft.getInstance().hasSingleplayerServer()) {
				ResourceKey<Level> dim = world.dimension();
				ResourceLocation dimName = dim.location();
				if (mc.getSingleplayerServer() != null) {
					Level serverWorld = mc.getSingleplayerServer().getLevel(dim);
					left.add(PREFIX + String.format("(INTEGRATED SERVER %s) netColl : %d, netPool: %d", dimName, ManaNetworkHandler.instance.getAllCollectorsInWorld(serverWorld).size(), ManaNetworkHandler.instance.getAllPoolsInWorld(serverWorld).size()));
				}
			}
		}
	}

}
