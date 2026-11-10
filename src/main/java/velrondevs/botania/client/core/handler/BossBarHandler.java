package velrondevs.botania.client.core.handler;

import net.minecraft.client.renderer.RenderPipelines;

import org.joml.Matrix3x2fStack;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.BossEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import velrondevs.botania.client.core.helper.BotaniaShaders;
import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.client.lib.ResourcesLib;
import velrondevs.botania.common.entity.GaiaGuardianEntity;

import java.util.*;

public final class BossBarHandler {

	private BossBarHandler() {}

	public static final Set<GaiaGuardianEntity> bosses = Collections.newSetFromMap(new WeakHashMap<>());
	private static final Identifier BAR_TEXTURE = Identifier.parse(ResourcesLib.GUI_BOSS_BAR);

	public static OptionalInt onBarRender(GuiGraphicsExtractor gui, int x, int y, BossEvent bossEvent, boolean drawName) {
		for (GaiaGuardianEntity currentBoss : bosses) {
			if (currentBoss.getBossInfoUuid().equals(bossEvent.getId())) {
				Minecraft mc = Minecraft.getInstance();

				int frameU = 0, frameV = 0;
				int frameWidth = 185, frameHeight = 15;
				int healthU = 0, healthV = frameV + frameHeight;
				int healthWidth = 181, healthHeight = 7;
				int healthX = x + (frameWidth - healthWidth) / 2;
				int healthY = y + (frameHeight - healthHeight) / 2;

				int playerCountHeight = drawPlayerCount(currentBoss.getPlayerCount(), gui, x, y);
				RenderHelper.drawTexturedModalRect(gui, BAR_TEXTURE, x, y, frameU, frameV,
						frameWidth, frameHeight);
				drawHealthBar(gui, currentBoss, healthX, healthY, healthU, healthV,
						(int) (healthWidth * bossEvent.getProgress()), healthHeight, false);

				if (drawName) {
					Component name = bossEvent.getName();
					int centerX = mc.getWindow().getGuiScaledWidth() / 2;
					RenderHelper.drawCenteredString(gui, mc.font, name, centerX, y - 10, 0xA2018C);
				}

				return OptionalInt.of(frameHeight + playerCountHeight + (drawName ? mc.font.lineHeight : 0));
			}
		}

		return OptionalInt.empty();
	}

	private static int drawPlayerCount(int playerCount, GuiGraphicsExtractor gui, int x, int y) {
		Matrix3x2fStack ps = gui.pose();
		ps.pushMatrix();
		int px = x + 160;
		int py = y + 12;

		Minecraft mc = Minecraft.getInstance();
		ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
		gui.item(stack, px, py);

		RenderHelper.drawString(gui, mc.font, Integer.toString(playerCount), px + 15, py + 4, 0xFFFFFF);
		ps.popMatrix();

		return 5;
	}

	private static void drawHealthBar(GuiGraphicsExtractor gui, GaiaGuardianEntity currentBoss, int x, int y, int u, int v, int w, int h, boolean bg) {
		if (BotaniaShaders.usable(BotaniaShaders.DOPPLEGANGER_BAR)) {
			float time = currentBoss.getInvulTime();
			float grainIntensity = time > 20 ? 1F : Math.max(currentBoss.isHardMode() ? 0.5F : 0F, time / 20F);
			float hpFract = currentBoss.getHealth() / currentBoss.getMaxHealth();
			gui.blit(BotaniaShaders.DOPPLEGANGER_BAR, BAR_TEXTURE, x, y, u, v, w, h, 256, 256, BotaniaShaders.dopplegangerBarTint(grainIntensity, hpFract));
		} else {
			gui.blit(RenderPipelines.GUI_TEXTURED, BAR_TEXTURE, x, y, u, v, w, h, 256, 256);
		}
	}

}
