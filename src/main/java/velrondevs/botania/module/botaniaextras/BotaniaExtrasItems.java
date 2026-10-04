package velrondevs.botania.module.botaniaextras;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.item.lens.LensItem;
import velrondevs.botania.module.botaniaextras.item.AesirEmblemItem;
import velrondevs.botania.module.botaniaextras.item.FloralBifrostPowderItem;
import velrondevs.botania.module.botaniaextras.item.FrozenStarItem;
import velrondevs.botania.module.botaniaextras.item.IridescentSeedsItem;
import velrondevs.botania.module.botaniaextras.item.PhantomFlashLens;
import velrondevs.botania.module.botaniaextras.item.PrismaticLakeRodItem;
import velrondevs.botania.module.botaniaextras.item.PriestEmblemItem;
import velrondevs.botania.module.botaniaextras.item.StormySeaRodItem;
import velrondevs.botania.module.botaniaextras.item.ThunderingPeaksRodItem;
import velrondevs.botania.module.botaniaextras.item.VibrantPlainsRodItem;
import velrondevs.botania.registry.BotaniaItems;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaExtrasItems {
	private static final Map<DyeColor, Item> SEEDS = new EnumMap<>(DyeColor.class);

	static {
		for (DyeColor color : DyeColor.values()) {
			SEEDS.put(color, new IridescentSeedsItem(color, BotaniaItems.defaultBuilder()));
		}
	}

	public static final Item bifrostSeeds = new IridescentSeedsItem(null, BotaniaItems.defaultBuilder());
	public static final Item floralBifrostPowder = new FloralBifrostPowderItem(BotaniaItems.defaultBuilder());
	public static final Item mysticalBifrostPetal = new Item(BotaniaItems.defaultBuilder());
	public static final Item shimmerQuartz = new Item(BotaniaItems.defaultBuilder());
	public static final Item frozenStar = new FrozenStarItem(BotaniaExtrasBlocks.frozenStar, BotaniaItems.defaultBuilder());

	public static final Item vibrantPlainsRod = new VibrantPlainsRodItem(BotaniaItems.defaultBuilder().stacksTo(1));
	public static final Item thunderingPeaksRod = new ThunderingPeaksRodItem(BotaniaItems.defaultBuilder().stacksTo(1));
	public static final Item stormySeaRod = new StormySeaRodItem(BotaniaItems.defaultBuilder().stacksTo(1));
	public static final Item prismaticLakeRod = new PrismaticLakeRodItem(BotaniaItems.defaultBuilder().stacksTo(1));
	public static final Item phantomFlashLens = new LensItem(BotaniaItems.defaultBuilder().stacksTo(16), new PhantomFlashLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION);
	public static final Item holySymbol = new Item(BotaniaItems.defaultBuilder());
	public static final Item priestEmblemThor = new PriestEmblemItem(PriestEmblemItem.Type.THOR, BotaniaItems.defaultBuilder().stacksTo(1));
	public static final Item priestEmblemSif = new PriestEmblemItem(PriestEmblemItem.Type.SIF, BotaniaItems.defaultBuilder().stacksTo(1));
	public static final Item priestEmblemNjord = new PriestEmblemItem(PriestEmblemItem.Type.NJORD, BotaniaItems.defaultBuilder().stacksTo(1));
	public static final Item aesirEmblem = new AesirEmblemItem(BotaniaItems.defaultBuilder().stacksTo(1).rarity(Rarity.EPIC));

	public static final Item thunderousTwig = new Item(BotaniaItems.defaultBuilder());
	public static final Item thunderousSplinters = new Item(BotaniaItems.defaultBuilder());
	public static final Item infernalTwig = new Item(BotaniaItems.defaultBuilder());
	public static final Item infernalSplinters = new Item(BotaniaItems.defaultBuilder());
	public static final Item flameLacedCharcoal = new Item(BotaniaItems.defaultBuilder());

	private BotaniaExtrasItems() {}

	public static Item getSeeds(@Nullable DyeColor color) {
		return color == null ? bifrostSeeds : SEEDS.get(color);
	}

	public static ResourceLocation seedsId(@Nullable DyeColor color) {
		return color == null ? prefix("bifrost_seeds") : prefix(color.getSerializedName() + "_iridescent_seeds");
	}

	public static void registerItems(BiConsumer<Item, ResourceLocation> r) {
		for (DyeColor color : DyeColor.values()) {
			r.accept(getSeeds(color), seedsId(color));
		}
		r.accept(bifrostSeeds, seedsId(null));
		r.accept(floralBifrostPowder, prefix("floral_bifrost_powder"));
		r.accept(mysticalBifrostPetal, prefix("mystical_bifrost_petal"));
		r.accept(shimmerQuartz, prefix("quartz_shimmer"));
		r.accept(frozenStar, prefix("frozen_star"));
		r.accept(vibrantPlainsRod, prefix("vibrant_plains_rod"));
		r.accept(thunderingPeaksRod, prefix("thundering_peaks_rod"));
		r.accept(stormySeaRod, prefix("stormy_sea_rod"));
		r.accept(prismaticLakeRod, prefix("prismatic_lake_rod"));
		r.accept(phantomFlashLens, prefix("phantom_flash_lens"));
		r.accept(holySymbol, prefix("holy_symbol"));
		r.accept(priestEmblemThor, prefix("priest_emblem_thor"));
		r.accept(priestEmblemSif, prefix("priest_emblem_sif"));
		r.accept(priestEmblemNjord, prefix("priest_emblem_njord"));
		r.accept(aesirEmblem, prefix("aesir_emblem"));
		r.accept(thunderousTwig, prefix("thunderous_twig"));
		r.accept(thunderousSplinters, prefix("thunderous_splinters"));
		r.accept(infernalTwig, prefix("infernal_twig"));
		r.accept(infernalSplinters, prefix("infernal_splinters"));
		r.accept(flameLacedCharcoal, prefix("flame_laced_charcoal"));
	}
}
