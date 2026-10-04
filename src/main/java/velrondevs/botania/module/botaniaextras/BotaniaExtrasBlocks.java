package velrondevs.botania.module.botaniaextras;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.block.BotaniaBlock;
import velrondevs.botania.common.block.decor.stairs.BotaniaStairBlock;
import velrondevs.botania.module.botaniaextras.block.BifrostFlowerBlock;
import velrondevs.botania.module.botaniaextras.block.BlazeKindlingBlock;
import velrondevs.botania.module.botaniaextras.block.DendricSuffuserBlock;
import velrondevs.botania.module.botaniaextras.block.ItemPlatformBlock;
import velrondevs.botania.module.botaniaextras.block.FrozenStarBlock;
import velrondevs.botania.module.botaniaextras.block.IridescentDirtBlock;
import velrondevs.botania.module.botaniaextras.block.IridescentGrassBlock;
import velrondevs.botania.module.botaniaextras.block.IridescentLanternBlock;
import velrondevs.botania.module.botaniaextras.block.IridescentTallGrassBlock;
import velrondevs.botania.module.botaniaextras.block.ManaFlashBlock;
import velrondevs.botania.registry.BotaniaItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaExtrasBlocks {
	private static final Map<DyeColor, Block> DIRT = new EnumMap<>(DyeColor.class);
	private static final Map<DyeColor, Block> GRASS = new EnumMap<>(DyeColor.class);
	private static final Map<DyeColor, Block> TALL_GRASS = new EnumMap<>(DyeColor.class);
	private static final List<Block> ALL = new ArrayList<>();

	static {
		for (DyeColor color : DyeColor.values()) {
			DIRT.put(color, new IridescentDirtBlock(color, dirtProps(color.getMapColor())));
			GRASS.put(color, new IridescentGrassBlock(color, grassProps()));
			TALL_GRASS.put(color, new IridescentTallGrassBlock(color, grassProps()));
		}
	}

	public static final Block bifrostDirt = new IridescentDirtBlock(null, dirtProps(MapColor.DIAMOND));
	public static final Block bifrostGrass = new IridescentGrassBlock(null, grassProps());
	public static final Block bifrostTallGrass = new IridescentTallGrassBlock(null, grassProps());
	public static final Block bifrostFlower = new BifrostFlowerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY));
	public static final Block tallBifrostFlower = new DoublePlantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ROSE_BUSH));

	public static final Block iridescentLantern = new IridescentLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_LAMP)
			.strength(0.3F).sound(SoundType.GLASS)
			.lightLevel(s -> s.getValue(IridescentLanternBlock.POWER) > 0 ? 15 : 0));
	public static final Block frozenStar = new FrozenStarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NONE)
			.noCollission().instabreak().sound(SoundType.WOOL).lightLevel(s -> 15).noLootTable()
			.pushReaction(PushReaction.DESTROY));
	public static final Block blazeKindling = new BlazeKindlingBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)
			.instabreak().sound(SoundType.WOOL));

	public static final Block rainbowManaFlash = new ManaFlashBlock(true, flashProps());
	public static final Block phantomManaFlash = new ManaFlashBlock(false, flashProps());

	public static final Block shimmerQuartz = new BotaniaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK).mapColor(MapColor.DIAMOND));
	public static final Block shimmerQuartzChiseled = new BotaniaBlock(BlockBehaviour.Properties.ofFullCopy(shimmerQuartz));
	public static final Block shimmerQuartzPillar = new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(shimmerQuartz));
	public static final Block shimmerQuartzSlab = new SlabBlock(BlockBehaviour.Properties.ofFullCopy(shimmerQuartz));
	public static final Block shimmerQuartzStairs = new BotaniaStairBlock(shimmerQuartz.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(shimmerQuartz));

	public static final Block manasteelItemPlatform = new ItemPlatformBlock(platformProps(MapColor.COLOR_LIGHT_BLUE));
	public static final Block terrasteelItemPlatform = new ItemPlatformBlock(platformProps(MapColor.COLOR_GREEN));
	public static final Block elementiumItemPlatform = new ItemPlatformBlock(platformProps(MapColor.COLOR_PINK));
	public static final Block dendricSuffuser = new DendricSuffuserBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).mapColor(MapColor.DIAMOND).lightLevel(s -> 7));
	public static final Block sonicAmplifier = new BotaniaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NOTE_BLOCK).sound(SoundType.WOOL));

	private BotaniaExtrasBlocks() {}

	private static BlockBehaviour.Properties platformProps(MapColor mapColor) {
		return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB).mapColor(mapColor).strength(2.0F, 3.0F);
	}

	private static BlockBehaviour.Properties dirtProps(MapColor mapColor) {
		return BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).mapColor(mapColor).strength(0.5F).sound(SoundType.GRAVEL);
	}

	private static BlockBehaviour.Properties flashProps() {
		return BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL).lightLevel(s -> 15).noCollission().noLootTable();
	}

	private static BlockBehaviour.Properties grassProps() {
		return BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS);
	}

	public static Block getDirt(@Nullable DyeColor color) {
		return color == null ? bifrostDirt : DIRT.get(color);
	}

	public static Block getGrass(@Nullable DyeColor color) {
		return color == null ? bifrostGrass : GRASS.get(color);
	}

	public static Block getTallGrass(@Nullable DyeColor color) {
		return color == null ? bifrostTallGrass : TALL_GRASS.get(color);
	}

	public static List<Block> all() {
		return Collections.unmodifiableList(ALL);
	}

	public static ResourceLocation dirtId(@Nullable DyeColor color) {
		return color == null ? prefix("bifrost_dirt") : prefix(color.getSerializedName() + "_iridescent_dirt");
	}

	public static ResourceLocation grassId(@Nullable DyeColor color) {
		return color == null ? prefix("bifrost_grass") : prefix(color.getSerializedName() + "_iridescent_grass");
	}

	public static ResourceLocation tallGrassId(@Nullable DyeColor color) {
		return color == null ? prefix("bifrost_tall_grass") : prefix(color.getSerializedName() + "_iridescent_tall_grass");
	}

	public static void registerBlocks(BiConsumer<Block, ResourceLocation> r) {
		BiConsumer<Block, ResourceLocation> tracking = (b, id) -> {
			ALL.add(b);
			r.accept(b, id);
		};
		for (DyeColor color : DyeColor.values()) {
			tracking.accept(getDirt(color), dirtId(color));
		}
		tracking.accept(bifrostDirt, dirtId(null));
		for (DyeColor color : DyeColor.values()) {
			tracking.accept(getGrass(color), grassId(color));
		}
		tracking.accept(bifrostGrass, grassId(null));
		for (DyeColor color : DyeColor.values()) {
			tracking.accept(getTallGrass(color), tallGrassId(color));
		}
		tracking.accept(bifrostTallGrass, tallGrassId(null));
		tracking.accept(bifrostFlower, prefix("mystical_bifrost_flower"));
		tracking.accept(tallBifrostFlower, prefix("tall_mystical_bifrost_flower"));
		tracking.accept(iridescentLantern, prefix("iridescent_lantern"));
		tracking.accept(frozenStar, prefix("frozen_star"));
		tracking.accept(blazeKindling, prefix("blaze_kindling"));
		tracking.accept(rainbowManaFlash, prefix("rainbow_mana_flash"));
		tracking.accept(phantomManaFlash, prefix("phantom_mana_flash"));
		tracking.accept(shimmerQuartz, prefix("shimmer_quartz"));
		tracking.accept(shimmerQuartzChiseled, prefix("chiseled_shimmer_quartz"));
		tracking.accept(shimmerQuartzPillar, prefix("shimmer_quartz_pillar"));
		tracking.accept(shimmerQuartzSlab, prefix("shimmer_quartz_slab"));
		tracking.accept(shimmerQuartzStairs, prefix("shimmer_quartz_stairs"));
		tracking.accept(manasteelItemPlatform, prefix("manasteel_item_platform"));
		tracking.accept(terrasteelItemPlatform, prefix("terrasteel_item_platform"));
		tracking.accept(elementiumItemPlatform, prefix("elementium_item_platform"));
		tracking.accept(dendricSuffuser, prefix("dendric_suffuser"));
		tracking.accept(sonicAmplifier, prefix("sonic_amplifier"));
	}

	public static void registerItemBlocks(BiConsumer<Item, ResourceLocation> r) {
		Item.Properties props = BotaniaItems.defaultBuilder();
		for (Block b : ALL) {
			if (b == frozenStar || b == rainbowManaFlash || b == phantomManaFlash || b == dendricSuffuser) {
				continue;
			}
			r.accept(new BlockItem(b, props), BuiltInRegistries.BLOCK.getKey(b));
		}
	}
}
