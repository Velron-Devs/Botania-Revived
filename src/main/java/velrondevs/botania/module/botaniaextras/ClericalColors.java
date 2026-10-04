package velrondevs.botania.module.botaniaextras;

import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.item.CosmeticAttachable;
import velrondevs.botania.common.handler.EquipmentHandler;

public final class ClericalColors {
	public static final int NONE = -1;

	private ClericalColors() {}

	private static int colorOf(ItemStack stack) {
		if (stack.getItem() instanceof ClericalColor override) {
			return override.clericalColor(stack);
		}
		if (stack.getItem() instanceof CosmeticAttachable attachable) {
			ItemStack cosmetic = attachable.getCosmeticItem(stack);
			if (!cosmetic.isEmpty() && cosmetic.getItem() instanceof ClericalColor override) {
				return override.clericalColor(cosmetic);
			}
		}
		return NONE;
	}

	public static int get(LivingEntity living) {
		if (living == null || EquipmentHandler.instance == null) {
			return NONE;
		}
		Container worn = EquipmentHandler.getAllWorn(living);
		int size = worn.getContainerSize();
		Container source = worn;
		if (size == 0 && living instanceof Player player) {
			source = player.getInventory();
			size = 9;
		}
		int red = 0;
		int green = 0;
		int blue = 0;
		int count = 0;
		for (int i = 0; i < size; i++) {
			int color = colorOf(source.getItem(i));
			if (color != NONE) {
				red += (color >> 16) & 0xFF;
				green += (color >> 8) & 0xFF;
				blue += color & 0xFF;
				count++;
			}
		}
		return count == 0 ? NONE : ((red / count) << 16) | ((green / count) << 8) | (blue / count);
	}

	public static int get(LivingEntity living, int fallback) {
		int color = get(living);
		return color == NONE ? fallback : color;
	}

	public static int brighter(int rgb) {
		int red = (rgb >> 16) & 0xFF;
		int green = (rgb >> 8) & 0xFF;
		int blue = rgb & 0xFF;
		if (red == 0 && green == 0 && blue == 0) {
			return 0x030303;
		}
		int floor = 3;
		if (red > 0 && red < floor) {
			red = floor;
		}
		if (green > 0 && green < floor) {
			green = floor;
		}
		if (blue > 0 && blue < floor) {
			blue = floor;
		}
		return (Math.min((int) (red / 0.7F), 255) << 16) | (Math.min((int) (green / 0.7F), 255) << 8) | Math.min((int) (blue / 0.7F), 255);
	}

	public static int brighter2(int rgb) {
		return brighter(brighter(rgb));
	}

	public static float red(int rgb) {
		return ((rgb >> 16) & 0xFF) / 255F;
	}

	public static float green(int rgb) {
		return ((rgb >> 8) & 0xFF) / 255F;
	}

	public static float blue(int rgb) {
		return (rgb & 0xFF) / 255F;
	}
}
