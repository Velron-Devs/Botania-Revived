package velrondevs.botania.module.botaniaextras;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;

import org.jetbrains.annotations.Nullable;

public final class IridescentColors {
	public static final String BIFROST = "bifrost";
	public static final int UNPOWERED_LANTERN = 0x191616;

	private IridescentColors() {}

	public static int rgb(@Nullable DyeColor color) {
		if (color == null) {
			return 0xFFFFFF;
		}
		return color.getTextureDiffuseColor() & 0xFFFFFF;
	}

	public static int rainbow(float ticks) {
		return Mth.hsvToRgb((ticks * 2 % 360) / 360F, 1F, 1F) & 0xFFFFFF;
	}

	public static int lanternColor(int power) {
		if (power <= 0) {
			return UNPOWERED_LANTERN;
		}
		if (power >= 15) {
			return 0xFFFFFF;
		}
		return Mth.hsvToRgb((power - 1) / 16F, 1F, 1F) & 0xFFFFFF;
	}

	public static String name(@Nullable DyeColor color) {
		return color == null ? BIFROST : color.getSerializedName();
	}

	public static Component displayName(@Nullable DyeColor color) {
		return color == null
				? Component.translatable("botaniamisc.botania_extras.bifrost")
				: Component.translatable("color.minecraft." + color.getSerializedName());
	}
}
