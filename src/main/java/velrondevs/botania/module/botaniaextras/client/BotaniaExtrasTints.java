package velrondevs.botania.module.botaniaextras.client;

import com.mojang.serialization.MapCodec;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import org.jspecify.annotations.Nullable;

import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.common.item.lens.LensItem;
import velrondevs.botania.module.botaniaextras.IridescentColors;
import velrondevs.botania.module.botaniaextras.item.FrozenStarItem;
import velrondevs.botania.module.botaniaextras.item.VibrantPlainsRodItem;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaExtrasTints {
	public static final Identifier RAINBOW_ID = prefix("extras_rainbow");
	public static final Identifier VIBRANT_ROD_ID = prefix("extras_vibrant_rod");
	public static final Identifier LENS_ID = prefix("extras_lens_color");
	public static final Identifier FROZEN_STAR_ID = prefix("extras_frozen_star");

	private BotaniaExtrasTints() {}

	public static int rainbow() {
		return IridescentColors.rainbow(ClientTickHandler.total());
	}

	public static void register(RegisterColorHandlersEvent.ItemTintSources e) {
		e.register(RAINBOW_ID, Rainbow.MAP_CODEC);
		e.register(VIBRANT_ROD_ID, VibrantRod.MAP_CODEC);
		e.register(LENS_ID, Lens.MAP_CODEC);
		e.register(FROZEN_STAR_ID, FrozenStar.MAP_CODEC);
	}

	public record Rainbow() implements ItemTintSource {
		public static final Rainbow INSTANCE = new Rainbow();
		public static final MapCodec<Rainbow> MAP_CODEC = MapCodec.unit(INSTANCE);

		@Override
		public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
			return ARGB.opaque(rainbow());
		}

		@Override
		public MapCodec<Rainbow> type() {
			return MAP_CODEC;
		}
	}

	public record VibrantRod() implements ItemTintSource {
		public static final VibrantRod INSTANCE = new VibrantRod();
		public static final MapCodec<VibrantRod> MAP_CODEC = MapCodec.unit(INSTANCE);

		@Override
		public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
			int index = VibrantPlainsRodItem.getColorIndex(stack);
			return ARGB.opaque(index >= VibrantPlainsRodItem.BIFROST ? rainbow() : IridescentColors.rgb(VibrantPlainsRodItem.dyeOf(index)));
		}

		@Override
		public MapCodec<VibrantRod> type() {
			return MAP_CODEC;
		}
	}

	public record Lens() implements ItemTintSource {
		public static final Lens INSTANCE = new Lens();
		public static final MapCodec<Lens> MAP_CODEC = MapCodec.unit(INSTANCE);

		@Override
		public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
			if (stack.getItem() instanceof LensItem lens) {
				return ARGB.opaque(lens.getLensColor(stack, level));
			}
			return -1;
		}

		@Override
		public MapCodec<Lens> type() {
			return MAP_CODEC;
		}
	}

	public record FrozenStar() implements ItemTintSource {
		public static final FrozenStar INSTANCE = new FrozenStar();
		public static final MapCodec<FrozenStar> MAP_CODEC = MapCodec.unit(INSTANCE);

		@Override
		public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
			int color = FrozenStarItem.getColor(stack);
			return ARGB.opaque(color == FrozenStarItem.RAINBOW ? rainbow() : color);
		}

		@Override
		public MapCodec<FrozenStar> type() {
			return MAP_CODEC;
		}
	}
}
