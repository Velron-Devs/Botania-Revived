package velrondevs.botania.client;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

import velrondevs.botania.client.core.proxy.ClientProxy;
import velrondevs.botania.common.block.mana.ManaPoolBlock;
import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.item.*;
import velrondevs.botania.common.item.brew.BaseBrewItem;
import velrondevs.botania.common.item.equipment.bauble.RingOfMagnetizationItem;
import velrondevs.botania.common.item.equipment.tool.bow.LivingwoodBowItem;
import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraTruncatorItem;
import velrondevs.botania.common.item.relic.FruitOfGrisaiaItem;
import velrondevs.botania.common.item.rod.SkiesRodItem;

import java.util.Locale;
import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaItemProperties {
	public static final Identifier FLAG_ID = prefix("flag");
	public static final Identifier SWIGS_TAKEN_ID = prefix("swigs_taken");
	public static final Identifier BOW_PULL_ID = prefix("bow_pull");

	public static void initConditional(BiConsumer<Identifier, MapCodec<? extends ConditionalItemModelProperty>> consumer) {
		consumer.accept(FLAG_ID, Flag.MAP_CODEC);
	}

	public static void initRange(BiConsumer<Identifier, MapCodec<? extends RangeSelectItemModelProperty>> consumer) {
		consumer.accept(SWIGS_TAKEN_ID, SwigsTaken.MAP_CODEC);
		consumer.accept(BOW_PULL_ID, BowPull.MAP_CODEC);
	}

	public enum FlagKind implements StringRepresentable {
		BAUBLE_BOX_OPEN("bauble_box_open"),
		BLACK_HOLE_ACTIVE("black_hole_active"),
		VUVUZELA("vuvuzela"),
		ELVEN("elven"),
		TOTALBISCUIT("totalbiscuit"),
		SLIME_ACTIVE("slime_active"),
		SPAWNER_FULL("spawner_full"),
		TEMPERANCE_ACTIVE("temperance_active"),
		BINDMODE("bindmode"),
		AUTOCRAFTING_ACTIVE("autocrafting_active"),
		POOL_FULL("pool_full"),
		HOLIDAY("holiday"),
		MAGNET_ACTIVE("magnet_active"),
		REDDIT("reddit"),
		ELUCIDATOR("elucidator"),
		TERRA_AXE_ACTIVE("terra_axe_active"),
		TERRA_PICK_TIPPED("terra_pick_tipped"),
		TERRA_PICK_ACTIVE("terra_pick_active"),
		BOOT("boot"),
		TORNADO_ACTIVE("tornado_active");

		public static final StringRepresentable.EnumCodec<FlagKind> CODEC = StringRepresentable.fromEnum(FlagKind::values);
		private final String name;

		FlagKind(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}

	public record Flag(FlagKind kind) implements ConditionalItemModelProperty {
		public static final MapCodec<Flag> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				FlagKind.CODEC.fieldOf("kind").forGetter(Flag::kind)
		).apply(i, Flag::new));

		@Override
		public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
			return switch (kind) {
				case BAUBLE_BOX_OPEN -> ItemNBTHelper.getBoolean(stack, BaubleBoxItem.TAG_OPEN, false);
				case BLACK_HOLE_ACTIVE -> ItemNBTHelper.getBoolean(stack, BlackHoleTalismanItem.TAG_ACTIVE, false);
				case VUVUZELA -> stack.getHoverName().getString().toLowerCase(Locale.ROOT).contains("vuvuzela");
				case ELVEN -> LexicaBotaniaItem.isElven(stack);
				case TOTALBISCUIT -> stack.getHoverName().getString().toLowerCase(Locale.ROOT).contains("totalbiscuit");
				case SLIME_ACTIVE -> ItemNBTHelper.getBoolean(stack, SlimeInABottleItem.TAG_ACTIVE, false);
				case SPAWNER_FULL -> LifeAggregatorItem.hasData(stack);
				case TEMPERANCE_ACTIVE -> ItemNBTHelper.getBoolean(stack, StoneOfTemperanceItem.TAG_ACTIVE, false);
				case BINDMODE -> WandOfTheForestItem.getBindMode(stack);
				case AUTOCRAFTING_ACTIVE -> ItemNBTHelper.getBoolean(stack, ManufactoryHaloItem.TAG_ACTIVE, true);
				case POOL_FULL -> stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ManaPoolBlock pool
						&& (pool.variant == ManaPoolBlock.Variant.CREATIVE || ItemNBTHelper.getBoolean(stack, "RenderFull", false));
				case HOLIDAY -> ClientProxy.jingleTheBells;
				case MAGNET_ACTIVE -> RingOfMagnetizationItem.getCooldown(stack) <= 0;
				case REDDIT -> stack.getHoverName().getString().equalsIgnoreCase("dammit reddit");
				case ELUCIDATOR -> "the elucidator".equals(stack.getHoverName().getString().toLowerCase(Locale.ROOT).trim());
				case TERRA_AXE_ACTIVE -> !(entity instanceof Player player) || TerraTruncatorItem.shouldBreak(player);
				case TERRA_PICK_TIPPED -> TerraShattererItem.isTipped(stack);
				case TERRA_PICK_ACTIVE -> TerraShattererItem.isEnabled(stack);
				case BOOT -> FruitOfGrisaiaItem.isBoot(stack);
				case TORNADO_ACTIVE -> SkiesRodItem.isFlying(stack);
			};
		}

		@Override
		public MapCodec<Flag> type() {
			return MAP_CODEC;
		}
	}

	public record SwigsTaken() implements RangeSelectItemModelProperty {
		public static final MapCodec<SwigsTaken> MAP_CODEC = MapCodec.unit(new SwigsTaken());

		@Override
		public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
			int swigsLeft;
			int totalSwigs;
			if (stack.getItem() instanceof BaseBrewItem item) {
				swigsLeft = item.getSwigsLeft(stack) - 1;
				totalSwigs = item.getSwigs() - 1;
			} else {
				swigsLeft = BottledManaItem.getSwigsLeft(stack) - 1;
				totalSwigs = BottledManaItem.SWIGS - 1;
			}
			return swigsLeft == totalSwigs ? 0.0F : Math.nextUp((totalSwigs - swigsLeft) / (float) totalSwigs);
		}

		@Override
		public MapCodec<SwigsTaken> type() {
			return MAP_CODEC;
		}
	}

	public record BowPull() implements RangeSelectItemModelProperty {
		public static final MapCodec<BowPull> MAP_CODEC = MapCodec.unit(new BowPull());

		@Override
		public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
			LivingEntity entity = owner == null ? null : owner.asLivingEntity();
			if (entity == null || entity.getUseItem() != stack || !(stack.getItem() instanceof LivingwoodBowItem item)) {
				return 0.0F;
			}
			return (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) * item.chargeVelocityMultiplier() / 20.0F;
		}

		@Override
		public MapCodec<BowPull> type() {
			return MAP_CODEC;
		}
	}

	private BotaniaItemProperties() {}
}
