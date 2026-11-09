package velrondevs.botania.client.render;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

import velrondevs.botania.api.brew.Brew;
import velrondevs.botania.api.brew.BrewItem;
import velrondevs.botania.api.mana.BurstProperties;
import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.common.block.PlatformBlock;
import velrondevs.botania.common.block.block_entity.PlatformBlockEntity;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.common.block.decor.PetalBlock;
import velrondevs.botania.common.block.mana.ManaPoolBlock;
import velrondevs.botania.common.item.ManaBlasterItem;
import velrondevs.botania.common.item.WandOfTheForestItem;
import velrondevs.botania.common.item.equipment.bauble.TaintedBloodPendantItem;
import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import velrondevs.botania.common.item.lens.LensItem;
import velrondevs.botania.common.item.material.MysticalPetalItem;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaBrews;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class ColorHandler {
	public static final Identifier TINT_ID = prefix("tint");
	private static final int PLATFORM_TINT_LAYERS = 4;

	public interface BlockHandlerConsumer {
		void register(List<BlockTintSource> handler, Block... blocks);
	}

	public static void submitBlocks(BlockHandlerConsumer blocks) {
		blocks.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return FoliageColor.FOLIAGE_DEFAULT;
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return BiomeColors.getAverageFoliageColor(level, pos);
			}
		}), BotaniaBlocks.solidVines);

		blocks.register(List.of(new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return opaque(poolColor(state, Optional.empty()));
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				Optional<Integer> color = Optional.empty();
				BlockEntity te = level.getBlockEntity(pos);
				if (te instanceof ManaPoolBlockEntity pool) {
					color = pool.getColor().map(MysticalPetalItem::getPetalLikeColor);
				}
				return opaque(poolColor(state, color));
			}
		}), BotaniaBlocks.manaPool, BotaniaBlocks.creativePool, BotaniaBlocks.dilutedPool, BotaniaBlocks.fabulousPool);

		blocks.register(List.of(state -> {
			float time = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks;
			return ARGB.opaque(Mth.hsvToRgb(time * 5 % 360 / 360F, 0.4F, 0.9F));
		}), BotaniaBlocks.gaiaSpreader);

		blocks.register(List.of(state -> ARGB.opaque(MysticalPetalItem.getPetalLikeColor(((PetalBlock) state.getBlock()).color))),
				BotaniaBlocks.petalBlockWhite, BotaniaBlocks.petalBlockOrange, BotaniaBlocks.petalBlockMagenta, BotaniaBlocks.petalBlockLightBlue,
				BotaniaBlocks.petalBlockYellow, BotaniaBlocks.petalBlockLime, BotaniaBlocks.petalBlockPink, BotaniaBlocks.petalBlockGray,
				BotaniaBlocks.petalBlockSilver, BotaniaBlocks.petalBlockCyan, BotaniaBlocks.petalBlockPurple, BotaniaBlocks.petalBlockBlue,
				BotaniaBlocks.petalBlockBrown, BotaniaBlocks.petalBlockGreen, BotaniaBlocks.petalBlockRed, BotaniaBlocks.petalBlockBlack
		);

		List<BlockTintSource> platformSources = new ArrayList<>();
		for (int i = 0; i < PLATFORM_TINT_LAYERS; i++) {
			int layer = i;
			platformSources.add(new BlockTintSource() {
				@Override
				public int color(BlockState state) {
					return -1;
				}

				@Override
				public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
					if (level.getBlockEntity(pos) instanceof PlatformBlockEntity camo) {
						BlockState camoState = camo.getCamoState();
						if (camoState != null && !(camoState.getBlock() instanceof PlatformBlock)) {
							BlockTintSource source = Minecraft.getInstance().getBlockColors().getTintSource(camoState, layer);
							if (source != null) {
								return source.colorInWorld(camoState, level, pos);
							}
						}
					}
					return -1;
				}
			});
		}
		blocks.register(platformSources, BotaniaBlocks.abstrusePlatform, BotaniaBlocks.spectralPlatform, BotaniaBlocks.infrangiblePlatform);
	}

	private static int opaque(int color) {
		return color == -1 ? -1 : ARGB.opaque(color);
	}

	private static int poolColor(BlockState state, Optional<Integer> color) {
		if (((ManaPoolBlock) state.getBlock()).variant == ManaPoolBlock.Variant.FABULOUS) {
			float time = (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.005F;
			int fabulousColor = Mth.hsvToRgb(time - (int) time, 0.6F, 1F);
			return color.isEmpty() ? fabulousColor : velrondevs.botania.common.helper.MathHelper.multiplyColor(fabulousColor, color.get());
		}
		return color.orElse(-1);
	}

	public enum Kind implements StringRepresentable {
		HUE_CYCLE("hue_cycle"),
		WAND_FIRST("wand_first"),
		WAND_SECOND("wand_second"),
		BLOCK("block"),
		MANA_FILL("mana_fill"),
		SPELL_CLOTH("spell_cloth"),
		BREW("brew"),
		BLASTER_LENS("blaster_lens"),
		BLASTER_BURST("blaster_burst"),
		ENDER_DAGGER("ender_dagger"),
		TERRA_PICK("terra_pick"),
		LENS("lens");

		public static final StringRepresentable.EnumCodec<Kind> CODEC = StringRepresentable.fromEnum(Kind::values);
		private final String name;

		Kind(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}

	public record Tint(Kind kind) implements ItemTintSource {
		public static final MapCodec<Tint> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Kind.CODEC.fieldOf("kind").forGetter(Tint::kind)
		).apply(i, Tint::new));

		@Override
		public int calculate(ItemStack s, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
			int color = compute(kind, s, level);
			return color == -1 ? -1 : ARGB.opaque(color);
		}

		@Override
		public MapCodec<Tint> type() {
			return MAP_CODEC;
		}
	}

	private static int compute(Kind kind, ItemStack s, @Nullable ClientLevel level) {
		switch (kind) {
			case HUE_CYCLE:
				return Mth.hsvToRgb(ClientTickHandler.ticksInGame * 2 % 360 / 360F, 0.25F, 1F);
			case WAND_FIRST:
				return MysticalPetalItem.getPetalLikeColor(DyeColor.byId(WandOfTheForestItem.getColor1(s)));
			case WAND_SECOND:
				return MysticalPetalItem.getPetalLikeColor(DyeColor.byId(WandOfTheForestItem.getColor2(s)));
			case BLOCK: {
				if (s.getItem() instanceof BlockItem blockItem) {
					BlockState state = blockItem.getBlock().defaultBlockState();
					BlockTintSource source = Minecraft.getInstance().getBlockColors().getTintSource(state, 0);
					return source != null ? source.color(state) : -1;
				}
				return -1;
			}
			case MANA_FILL: {
				var manaItem = XplatAbstractions.INSTANCE.findManaItem(s);
				if (manaItem == null) {
					return -1;
				}
				return Mth.hsvToRgb(0.528F, (float) manaItem.getMana() / (float) manaItem.getMaxMana(), 1F);
			}
			case SPELL_CLOTH:
				return Mth.hsvToRgb(0.55F, ((float) s.getMaxDamage() - (float) s.getDamageValue()) / (float) s.getMaxDamage() * 0.5F, 1F);
			case BREW: {
				if (!(s.getItem() instanceof BrewItem brewItem)) {
					return -1;
				}
				Brew brew = brewItem.getBrew(s);
				if (brew == BotaniaBrews.fallbackBrew) {
					return s.getItem() instanceof TaintedBloodPendantItem ? 0xC6000E : 0x989898;
				}

				int color = brew.getColor(s);
				double speed = s.is(BotaniaItems.brewFlask) || s.is(BotaniaItems.brewVial) ? 0.1 : 0.2;
				int add = (int) (Math.sin(ClientTickHandler.ticksInGame * speed) * 24);

				int r = Math.max(0, Math.min(255, (color >> 16 & 0xFF) + add));
				int g = Math.max(0, Math.min(255, (color >> 8 & 0xFF) + add));
				int b = Math.max(0, Math.min(255, (color & 0xFF) + add));

				return r << 16 | g << 8 | b;
			}
			case BLASTER_LENS: {
				ItemStack lens = ManaBlasterItem.getLens(s);
				if (!lens.isEmpty() && lens.getItem() instanceof LensItem lensItem) {
					return lensItem.getLensColor(lens, level);
				}
				return -1;
			}
			case BLASTER_BURST: {
				if (!(s.getItem() instanceof ManaBlasterItem blaster) || Minecraft.getInstance().player == null) {
					return -1;
				}
				BurstProperties props = blaster.getBurstProps(Minecraft.getInstance().player, s, false, InteractionHand.MAIN_HAND);

				float mul = (float) (Math.sin((double) ClientTickHandler.ticksInGame / 5) * 0.15F);
				int c = (int) (255 * mul);

				int cr = Mth.clamp((props.color >> 16 & 0xFF) + c, 0, 255);
				int cg = Mth.clamp((props.color >> 8 & 0xFF) + c, 0, 255);
				int cb = Mth.clamp((props.color & 0xFF) + c, 0, 255);

				return cr << 16 | cg << 8 | cb;
			}
			case ENDER_DAGGER:
				return Mth.hsvToRgb(0.75F, 1F, 1.5F - (float) Math.min(1F, Math.sin(Util.getMillis() / 100D) * 0.5 + 1.2F));
			case TERRA_PICK:
				return TerraShattererItem.isEnabled(s) ? Mth.hsvToRgb(0.375F, (float) Math.min(1F, Math.sin(Util.getMillis() / 200D) * 0.5 + 1F), 1F) : -1;
			case LENS:
				return s.getItem() instanceof LensItem lensItem ? lensItem.getLensColor(s, level) : -1;
			default:
				return -1;
		}
	}

	private ColorHandler() {}
}
