package velrondevs.botania.client.core.handler;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import org.jspecify.annotations.Nullable;

import velrondevs.botania.common.handler.ManaNetworkHandler;
import velrondevs.botania.xplat.BotaniaConfig;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class DebugHandler implements DebugScreenEntry {
	public static final Identifier ID = prefix("debug_info");
	public static final DebugHandler INSTANCE = new DebugHandler();

	private DebugHandler() {}

	private static final String PREFIX = ChatFormatting.GREEN + "[Botania] " + ChatFormatting.RESET;

	@Override
	public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
		List<String> lines = new ArrayList<>();
		onDrawDebugText(lines);
		if (!lines.isEmpty()) {
			displayer.addToGroup(ID, lines);
		}
	}

	public static void onDrawDebugText(List<String> left) {
		Minecraft mc = Minecraft.getInstance();
		Level world = mc.level;
		if (world == null || !BotaniaConfig.client().debugInfo()) {
			return;
		}
		String version = XplatAbstractions.INSTANCE.getBotaniaVersion();

		left.add(PREFIX + "(CLIENT) netColl: " + ManaNetworkHandler.instance.getAllCollectorsInWorld(world).size() + ", netPool: " + ManaNetworkHandler.instance.getAllPoolsInWorld(world).size() + ", rv: " + version);

		if (mc.hasSingleplayerServer() && mc.getSingleplayerServer() != null) {
			ResourceKey<Level> dim = world.dimension();
			Identifier dimName = dim.identifier();
			Level serverWorld = mc.getSingleplayerServer().getLevel(dim);
			if (serverWorld != null) {
				left.add(PREFIX + String.format("(INTEGRATED SERVER %s) netColl : %d, netPool: %d", dimName, ManaNetworkHandler.instance.getAllCollectorsInWorld(serverWorld).size(), ManaNetworkHandler.instance.getAllPoolsInWorld(serverWorld).size()));
			}
		}
	}

}
