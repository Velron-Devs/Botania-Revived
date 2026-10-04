package velrondevs.botania.client.core.handler;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import velrondevs.botania.client.gui.ItemsRemainingRenderHandler;
import velrondevs.botania.client.render.block_entity.RedStringBlockEntityRenderer;
import velrondevs.botania.common.block.flower.functional.VinculotusBlockEntity;
import velrondevs.botania.common.handler.ManaNetworkHandler;
import velrondevs.botania.common.helper.PlayerHelper;
import velrondevs.botania.common.item.LexicaBotaniaItem;
import velrondevs.botania.common.item.WandOfTheForestItem;

public final class ClientTickHandler {

	private ClientTickHandler() {}

	public static int ticksWithLexicaOpen = 0;
	public static int pageFlipTicks = 0;
	public static int ticksInGame = 0;
	public static float partialTicks = 0;

	public static float total() {
		return ticksInGame + partialTicks;
	}

	public static void renderTick(float renderTickTime) {
		partialTicks = renderTickTime;
	}

	public static void clientTickEnd(Minecraft mc) {
		RedStringBlockEntityRenderer.tick();
		ItemsRemainingRenderHandler.tick();

		if (mc.level == null) {
			ManaNetworkHandler.instance.clear();
			VinculotusBlockEntity.existingFlowers.clear();
		}

		if (!mc.isPaused()) {
			ticksInGame++;
			partialTicks = 0;

			Player player = mc.player;
			if (player != null) {
				if (PlayerHelper.hasHeldItemClass(player, WandOfTheForestItem.class)) {
					for (var collector : ImmutableList.copyOf(ManaNetworkHandler.instance.getAllCollectorsInWorld(Minecraft.getInstance().level))) {
						collector.onClientDisplayTick();
					}
				}
			}
		}

		int ticksToOpen = 10;
		if (LexicaBotaniaItem.isOpen()) {
			if (ticksWithLexicaOpen < 0) {
				ticksWithLexicaOpen = 0;
			}
			if (ticksWithLexicaOpen < ticksToOpen) {
				ticksWithLexicaOpen++;
			}
			if (pageFlipTicks > 0) {
				pageFlipTicks--;
			}
		} else {
			pageFlipTicks = 0;
			if (ticksWithLexicaOpen > 0) {
				if (ticksWithLexicaOpen > ticksToOpen) {
					ticksWithLexicaOpen = ticksToOpen;
				}
				ticksWithLexicaOpen--;
			}
		}
	}

	public static void notifyPageChange() {
		if (pageFlipTicks == 0) {
			pageFlipTicks = 5;
		}
	}

}
