package velrondevs.botania.module.botaniaextras;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.block.BotaniaBlock;
import velrondevs.botania.common.block.decor.stairs.BotaniaStairBlock;
import velrondevs.botania.module.botaniaextras.block.IridescentSaplingBlock;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaExtrasWoods {
	public enum Kind {
		IRIDESCENT,
		BIFROST,
		ALT
	}

	public static final class WoodSet {
		private final String name;
		private final Kind kind;
		@Nullable
		private final DyeColor color;
		private final Block soil;
		private final Block log;
		private final Block planks;
		private final Block leaves;
		private final Block slab;
		private final Block stairs;

		private WoodSet(String name, Kind kind, @Nullable DyeColor color, Block soil, MapColor mapColor) {
			this.name = name;
			this.kind = kind;
			this.color = color;
			this.soil = soil;
			this.log = new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).mapColor(mapColor));
			this.planks = new BotaniaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).mapColor(mapColor));
			this.leaves = new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).mapColor(mapColor));
			this.slab = new SlabBlock(BlockBehaviour.Properties.ofFullCopy(planks));
			this.stairs = new BotaniaStairBlock(planks.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(planks));
		}

		public String name() {
			return name;
		}

		public Kind kind() {
			return kind;
		}

		@Nullable
		public DyeColor color() {
			return color;
		}

		public Block soil() {
			return soil;
		}

		public Block log() {
			return log;
		}

		public Block planks() {
			return planks;
		}

		public Block leaves() {
			return leaves;
		}

		public Block slab() {
			return slab;
		}

		public Block stairs() {
			return stairs;
		}

		public List<Block> blocks() {
			return List.of(log, planks, leaves, slab, stairs);
		}

		public ResourceLocation id(String suffix) {
			return prefix(name + "_" + suffix);
		}
	}

	private static final List<WoodSet> SETS = new ArrayList<>();
	private static final Map<Block, WoodSet> BY_SOIL = new IdentityHashMap<>();
	private static final List<Block> ALL = new ArrayList<>();

	public static final Block sapling = new IridescentSaplingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING));

	static {
		for (DyeColor color : DyeColor.values()) {
			add(new WoodSet(color.getSerializedName() + "_iridescent", Kind.IRIDESCENT, color, BotaniaExtrasBlocks.getDirt(color), color.getMapColor()));
		}
		add(new WoodSet("bifrost", Kind.BIFROST, null, BotaniaExtrasBlocks.bifrostDirt, MapColor.DIAMOND));
		add(new WoodSet("dry", Kind.ALT, null, BotaniaBlocks.dryGrass, MapColor.TERRACOTTA_LIGHT_GREEN));
		add(new WoodSet("golden", Kind.ALT, null, BotaniaBlocks.goldenGrass, MapColor.GOLD));
		add(new WoodSet("vivid", Kind.ALT, null, BotaniaBlocks.vividGrass, MapColor.PLANT));
		add(new WoodSet("scorched", Kind.ALT, null, BotaniaBlocks.scorchedGrass, MapColor.NETHER));
		add(new WoodSet("infused", Kind.ALT, null, BotaniaBlocks.infusedGrass, MapColor.COLOR_CYAN));
		add(new WoodSet("mutated", Kind.ALT, null, BotaniaBlocks.mutatedGrass, MapColor.WARPED_HYPHAE));
	}

	private BotaniaExtrasWoods() {}

	private static void add(WoodSet set) {
		SETS.add(set);
		BY_SOIL.put(set.soil, set);
	}

	public static List<WoodSet> sets() {
		return Collections.unmodifiableList(SETS);
	}

	public static List<Block> all() {
		return Collections.unmodifiableList(ALL);
	}

	@Nullable
	public static WoodSet forSoil(Block block) {
		return BY_SOIL.get(block);
	}

	public static boolean isSoil(Block block) {
		return BY_SOIL.containsKey(block);
	}

	public static void registerBlocks(BiConsumer<Block, ResourceLocation> r) {
		for (WoodSet set : SETS) {
			register(r, set.log, set.id("log"));
			register(r, set.planks, set.id("planks"));
			register(r, set.leaves, set.id("leaves"));
			register(r, set.slab, set.id("slab"));
			register(r, set.stairs, set.id("stairs"));
		}
		register(r, sapling, prefix("iridescent_sapling"));
	}

	private static void register(BiConsumer<Block, ResourceLocation> r, Block block, ResourceLocation id) {
		ALL.add(block);
		r.accept(block, id);
	}

	public static void registerItemBlocks(BiConsumer<Item, ResourceLocation> r) {
		Item.Properties props = BotaniaItems.defaultBuilder();
		for (Block b : ALL) {
			r.accept(new BlockItem(b, props), BuiltInRegistries.BLOCK.getKey(b));
		}
	}
}
