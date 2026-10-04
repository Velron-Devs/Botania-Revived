package velrondevs.botania.integration.jade;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;

import org.jetbrains.annotations.Nullable;

import snownee.jade.api.Accessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IDisplayHelper;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.ProgressStyle;

import java.util.List;
import java.util.Locale;

public final class JadeManaHelper {
	private JadeManaHelper() {}

	public static final String MANA = "mana";
	public static final String MAX = "maxMana";
	public static final String TARGET = "target";
	public static final String BOUND = "bound";
	public static final String OUTPUT = "output";

	public static final String DEFAULT_MANA_COLOR = "#0095FF";
	public static final String DEFAULT_PROGRESS_COLOR = "#2ECC71";
	private static final int FALLBACK_MANA_COLOR = 0xFF0095FF;
	private static final int FALLBACK_PROGRESS_COLOR = 0xFF2ECC71;

	public static boolean enabled(IPluginConfig config) {
		return config.get(BotaniaJadeIds.ENABLED);
	}

	public static boolean isValidColor(String value) {
		return parseColor(value) != null;
	}

	@Nullable
	public static Integer parseColor(@Nullable String value) {
		if (value == null) {
			return null;
		}
		String v = value.trim();
		if (v.startsWith("#")) {
			v = v.substring(1);
		} else if (v.startsWith("0x") || v.startsWith("0X")) {
			v = v.substring(2);
		}
		if (v.length() != 6 && v.length() != 8) {
			return null;
		}
		try {
			long parsed = Long.parseLong(v, 16);
			return v.length() == 6 ? (int) (0xFF000000L | parsed) : (int) parsed;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private static int configColor(IPluginConfig config, ResourceLocation key, int fallback) {
		Integer color = parseColor(config.getString(key));
		return color == null ? fallback : color;
	}

	public static int manaColor(IPluginConfig config, int nativeColor) {
		if (nativeColor >= 0 && config.get(BotaniaJadeIds.NATIVE_COLORS)) {
			return 0xFF000000 | nativeColor;
		}
		return configColor(config, BotaniaJadeIds.MANA_BAR_COLOR, FALLBACK_MANA_COLOR);
	}

	public static int progressColor(IPluginConfig config) {
		return configColor(config, BotaniaJadeIds.PROGRESS_BAR_COLOR, FALLBACK_PROGRESS_COLOR);
	}

	public static String number(IPluginConfig config, long value) {
		if (config.get(BotaniaJadeIds.COMPACT_NUMBERS)) {
			return IDisplayHelper.get().humanReadableNumber(value, "", false);
		}
		return Long.toString(value);
	}

	public static Component time(int ticks) {
		int seconds = Math.max(0, ticks) / 20;
		String text;
		if (ticks < 1200) {
			text = String.format(Locale.ROOT, "%.1fs", Math.max(0, ticks) / 20F);
		} else if (seconds < 3600) {
			text = String.format(Locale.ROOT, "%dm %02ds", seconds / 60, seconds % 60);
		} else {
			text = String.format(Locale.ROOT, "%dh %02dm", seconds / 3600, seconds / 60 % 60);
		}
		return Component.literal(text);
	}

	public static void addManaBar(ITooltip tooltip, IPluginConfig config, int mana, int max, int color) {
		Component text = config.get(BotaniaJadeIds.SHOW_NUMBERS)
				? Component.translatable("botania.jade.mana_amount", number(config, mana), number(config, max))
				: Component.empty();
		addBar(tooltip, max <= 0 ? 0F : (float) mana / max, text, color);
	}

	public static void addProgressBar(ITooltip tooltip, IPluginConfig config, float ratio, Component text) {
		addBar(tooltip, ratio, text, progressColor(config));
	}

	public static void addBar(ITooltip tooltip, float ratio, Component text, int color) {
		IElementHelper elements = IElementHelper.get();
		ProgressStyle style = elements.progressStyle().color(color, darker(color));
		tooltip.add(elements.progress(Mth.clamp(ratio, 0F, 1F), text, style, BoxStyle.getNestedBox(), true));
	}

	private static int darker(int color) {
		return (color & 0xFF000000) | ((color & 0xFEFEFE) >> 1);
	}

	public static void addRecipeRow(ITooltip tooltip, List<ItemStack> inputs, float progress, ItemStack output) {
		IElementHelper elements = IElementHelper.get();
		boolean first = true;
		for (ItemStack input : inputs) {
			if (input.isEmpty()) {
				continue;
			}
			IElement element = elements.item(input);
			if (first) {
				tooltip.add(element);
			} else {
				tooltip.append(element);
			}
			first = false;
		}
		IElement arrow = elements.progress(Mth.clamp(progress, 0F, 1F));
		if (first) {
			tooltip.add(arrow);
		} else {
			tooltip.append(elements.spacer(4, 0));
			tooltip.append(arrow.translate(new Vec2(-2, 0)));
		}
		if (!output.isEmpty()) {
			tooltip.append(elements.item(output));
		}
	}

	public static void addItemLine(ITooltip tooltip, ItemStack stack, Component text) {
		IElementHelper elements = IElementHelper.get();
		if (stack.isEmpty()) {
			tooltip.add(text);
		} else {
			tooltip.add(elements.smallItem(stack));
			tooltip.append(elements.text(text));
		}
	}

	public static void addBinding(ITooltip tooltip, IPluginConfig config, CompoundTag data, Accessor<?> accessor, String boundKey, String unboundKey) {
		if (!config.get(BotaniaJadeIds.SHOW_BINDING) || !data.contains(BOUND)) {
			return;
		}
		if (data.getBoolean(BOUND)) {
			ItemStack target = getStack(data, TARGET, accessor);
			addItemLine(tooltip, target, Component.translatable(boundKey, target.isEmpty() ? Component.literal("?") : target.getHoverName()));
		} else {
			tooltip.add(IThemeHelper.get().danger(Component.translatable(unboundKey)));
		}
	}

	public static void putBinding(CompoundTag data, Accessor<?> accessor, Level level, @Nullable BlockPos pos) {
		data.putBoolean(BOUND, pos != null);
		if (pos != null) {
			putStack(data, TARGET, accessor, new ItemStack(level.getBlockState(pos).getBlock()));
		}
	}

	public static void putStack(CompoundTag data, String key, Accessor<?> accessor, ItemStack stack) {
		if (!stack.isEmpty()) {
			data.put(key, accessor.encodeAsNbt(ItemStack.OPTIONAL_STREAM_CODEC, stack));
		}
	}

	public static ItemStack getStack(CompoundTag data, String key, Accessor<?> accessor) {
		if (!data.contains(key)) {
			return ItemStack.EMPTY;
		}
		return accessor.decodeFromNbt(ItemStack.OPTIONAL_STREAM_CODEC, data.get(key)).orElse(ItemStack.EMPTY);
	}

	public static int getInt(CompoundTag data, String key, int fallback) {
		return data.contains(key) ? data.getInt(key) : fallback;
	}

	public static MutableComponent gray(Component component) {
		return component.copy().withStyle(ChatFormatting.GRAY);
	}
}
