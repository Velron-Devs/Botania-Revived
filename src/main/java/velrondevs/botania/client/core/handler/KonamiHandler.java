package velrondevs.botania.client.core.handler;

import org.joml.Matrix3x2fStack;

import velrondevs.botania.client.core.helper.RenderHelper;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;

import org.lwjgl.glfw.GLFW;

import velrondevs.botania.common.item.LexicaBotaniaItem;
import velrondevs.botania.registry.BotaniaSounds;

public class KonamiHandler {
	private static final int[] KONAMI_CODE = {
			GLFW.GLFW_KEY_UP, GLFW.GLFW_KEY_UP,
			GLFW.GLFW_KEY_DOWN, GLFW.GLFW_KEY_DOWN,
			GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_RIGHT,
			GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_RIGHT,
			GLFW.GLFW_KEY_B, GLFW.GLFW_KEY_A,
	};
	private static int nextLetter = 0;
	private static int konamiTime = 0;

	public static void clientTick(Minecraft client) {
		if (konamiTime > 0) {
			konamiTime--;
		}

		if (!LexicaBotaniaItem.isOpen()) {
			nextLetter = 0;
		}
	}

	public static void handleInput(int key, int action, int modifiers) {
		Minecraft mc = Minecraft.getInstance();
		if (modifiers == 0 && action == GLFW.GLFW_PRESS && LexicaBotaniaItem.isOpen()) {
			if (konamiTime == 0 && key == KONAMI_CODE[nextLetter]) {
				nextLetter++;
				if (nextLetter >= KONAMI_CODE.length) {
					mc.getSoundManager().play(SimpleSoundInstance.forUI(BotaniaSounds.way, 1.0F));
					nextLetter = 0;
					konamiTime = 240;
				}
			} else {
				nextLetter = 0;
			}
		}
	}

	public static void renderBook(Identifier book, Screen gui, int mouseX, int mouseY, float partialTicks, GuiGraphicsExtractor guiGraphics) {
		Matrix3x2fStack ms = guiGraphics.pose();
		if (konamiTime > 0) {
			String meme = I18n.get("botania.subtitle.way");
			ms.pushMatrix();
			int fullWidth = Minecraft.getInstance().font.width(meme);
			int left = gui.width;
			double widthPerTick = (fullWidth + gui.width) / 240;
			double currWidth = left - widthPerTick * (240 - (konamiTime - partialTicks)) * 3.2;

			ms.translate((float) (currWidth), (float) (gui.height / 2 - 10));
			ms.scale((float) (4), (float) (4));
			RenderHelper.drawString(guiGraphics, Minecraft.getInstance().font, meme, 0, 0, 0xFFFFFF);
			ms.popMatrix();
		}
	}
}
