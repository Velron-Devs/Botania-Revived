package velrondevs.botania.module.botaniaextras;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import velrondevs.botania.common.block.BotaniaBlock;
import velrondevs.botania.common.block.decor.stairs.BotaniaStairBlock;
import velrondevs.botania.module.botaniaextras.block.MagicSaplingBlock;
import velrondevs.botania.module.botaniaextras.block.ThunderousLogBlock;
import velrondevs.botania.registry.BotaniaItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaExtrasMagicWoods {
	public enum Type {
		THUNDEROUS("thunderous", MapColor.COLOR_PURPLE),
		INFERNAL("infernal", MapColor.NETHER),
		SEALING("sealing", MapColor.SNOW);

		private final String name;
		private final MapColor mapColor;

		Type(String name, MapColor mapColor) {
			this.name = name;
			this.mapColor = mapColor;
		}

		public String woodName() {
			return name;
		}
	}

	public static final class MagicSet {
		private final Type type;
		private final Block sapling;
		private final Block log;
		private final Block planks;
		private final Block leaves;
		private final Block slab;
		private final Block stairs;

		private MagicSet(Type type) {
			this.type = type;
			MapColor mapColor = type.mapColor;
			this.sapling = new MagicSaplingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING), type);
			BlockBehaviour.Properties logProps = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).mapColor(mapColor);
			this.log = type == Type.THUNDEROUS ? new ThunderousLogBlock(logProps) : new RotatedPillarBlock(logProps);
			this.planks = new BotaniaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).mapColor(mapColor));
			this.leaves = new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).mapColor(mapColor));
			this.slab = new SlabBlock(BlockBehaviour.Properties.ofFullCopy(planks));
			this.stairs = new BotaniaStairBlock(planks.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(planks));
		}

		public Type type() {
			return type;
		}

		public String name() {
			return type.name;
		}

		public Block sapling() {
			return sapling;
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
			return List.of(sapling, log, planks, leaves, slab, stairs);
		}
	}

	private static final Map<Type, MagicSet> SETS = new EnumMap<>(Type.class);
	private static final List<Block> ALL = new ArrayList<>();

	static {
		for (Type type : Type.values()) {
			SETS.put(type, new MagicSet(type));
		}
	}

	private BotaniaExtrasMagicWoods() {}

	public static MagicSet get(Type type) {
		return SETS.get(type);
	}

	public static List<MagicSet> sets() {
		return List.copyOf(SETS.values());
	}

	public static List<Block> all() {
		return Collections.unmodifiableList(ALL);
	}

	public static ResourceLocation saplingId(Type type) {
		return prefix(type.name + "_oak_sapling");
	}

	public static void registerBlocks(BiConsumer<Block, ResourceLocation> r) {
		for (MagicSet set : SETS.values()) {
			register(r, set.sapling, saplingId(set.type));
			register(r, set.log, prefix(set.name() + "_log"));
			register(r, set.planks, prefix(set.name() + "_planks"));
			register(r, set.leaves, prefix(set.name() + "_leaves"));
			register(r, set.slab, prefix(set.name() + "_slab"));
			register(r, set.stairs, prefix(set.name() + "_stairs"));
		}
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
