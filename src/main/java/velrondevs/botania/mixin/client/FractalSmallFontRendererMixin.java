package velrondevs.botania.mixin.client;

import java.text.Normalizer;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;

@Pseudo
@Mixin(targets = "de.dafuqs.fractal.impl.client.SmallFontRenderer")
public class FractalSmallFontRendererMixin {

	@Unique
	private static final String BOTANIA$BASES = "AEIOUYAEIOUAEIOUAEIOUYANOC";

	@Unique
	private static final String BOTANIA$MARKS = "\u0301\u0301\u0301\u0301\u0301\u0301\u0300\u0300\u0300\u0300\u0300\u0302\u0302\u0302\u0302\u0302\u0308\u0308\u0308\u0308\u0308\u0308\u0303\u0303\u0303\u0327";

	@Unique
	private static final float BOTANIA$SOLID_U = 20.0F;

	@Unique
	private static final float BOTANIA$SOLID_V = 24.0F;

	@ModifyVariable(method = "draw", at = @At("HEAD"), argsOnly = true, require = 0)
	private static String botania$stripLatinAccents(String text) {
		if (text == null) {
			return null;
		}
		int length = text.length();
		int i = 0;
		while (i < length && text.charAt(i) >= 32 && text.charAt(i) <= 127) {
			i++;
		}
		if (i == length) {
			return text;
		}
		StringBuilder stripped = new StringBuilder(length);
		StringBuilder marked = new StringBuilder(length);
		stripped.append(text, 0, i);
		marked.append(text, 0, i);
		boolean markersAllowed = true;
		for (; i < length; i++) {
			char c = text.charAt(i);
			if (c <= 127) {
				if (c < 32) {
					markersAllowed = false;
				}
				stripped.append(c);
				marked.append(c);
				continue;
			}
			String decomposed = Normalizer.normalize(String.valueOf(c), Normalizer.Form.NFD);
			if (decomposed.length() > 1 && decomposed.charAt(0) <= 127 && botania$onlyMarks(decomposed)) {
				char base = decomposed.charAt(0);
				stripped.append(base);
				int marker = decomposed.length() == 2 ? botania$marker(base, decomposed.charAt(1)) : 0;
				marked.append(marker > 0 ? (char) marker : base);
			} else {
				stripped.append(c);
				marked.append(c);
				markersAllowed = false;
			}
		}
		if (markersAllowed && !Minecraft.getInstance().isEnforceUnicode()) {
			return marked.toString();
		}
		return stripped.toString();
	}

	@WrapOperation(method = "draw", at = @At(value = "INVOKE", target = "Lde/dafuqs/fractal/impl/client/SmallFontRenderer;quad(Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/vertex/VertexConsumer;FFFFFFFF)V"), require = 0)
	private static void botania$drawAccentedGlyph(Matrix4f matrix, VertexConsumer consumer, float x, float y, float u, float v, float w, float h, float texW, float texH, Operation<Void> original) {
		if (texW == 64.0F && texH == 48.0F && w == 4.0F && h == 6.0F && v < 12.0F) {
			int code = (int) (v / 6.0F) * 16 + (int) (u / 4.0F);
			if (code >= 1 && code <= BOTANIA$BASES.length()) {
				char base = BOTANIA$BASES.charAt(code - 1);
				original.call(matrix, consumer, x, y, (float) (base % 16 * 4), (float) (base / 16 * 6), w, h, texW, texH);
				int[] pixels = botania$accentPixels(BOTANIA$MARKS.charAt(code - 1));
				for (int p = 0; p < pixels.length; p += 2) {
					original.call(matrix, consumer, x + pixels[p], y + pixels[p + 1], BOTANIA$SOLID_U, BOTANIA$SOLID_V, 1.0F, 1.0F, texW, texH);
				}
				return;
			}
		}
		original.call(matrix, consumer, x, y, u, v, w, h, texW, texH);
	}

	@Unique
	private static int botania$marker(char base, char mark) {
		char upper = Character.toUpperCase(base);
		for (int k = 0; k < BOTANIA$BASES.length(); k++) {
			if (BOTANIA$BASES.charAt(k) == upper && BOTANIA$MARKS.charAt(k) == mark) {
				return k + 1;
			}
		}
		return 0;
	}

	@Unique
	private static int[] botania$accentPixels(char mark) {
		return switch (mark) {
			case '\u0301' -> new int[] {3, -2, 2, -1};
			case '\u0300' -> new int[] {0, -2, 1, -1};
			case '\u0302' -> new int[] {0, -1, 1, -2, 2, -1};
			case '\u0308' -> new int[] {0, -2, 2, -2};
			case '\u0303' -> new int[] {0, -1, 1, -2, 2, -1, 3, -2};
			case '\u0327' -> new int[] {1, 5};
			default -> new int[0];
		};
	}

	@Unique
	private static boolean botania$onlyMarks(String decomposed) {
		for (int j = 1; j < decomposed.length(); j++) {
			if (Character.getType(decomposed.charAt(j)) != Character.NON_SPACING_MARK) {
				return false;
			}
		}
		return true;
	}
}
