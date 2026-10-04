package velrondevs.botania.datagen.providers.botaniaextras;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.module.ModuleTagSink;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasItems;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasMagicWoods;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasTags;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;
import velrondevs.botania.registry.BotaniaBlocks;

import java.util.ArrayList;
import java.util.List;

public final class BotaniaExtrasTagEntries {
	private BotaniaExtrasTagEntries() {}

	private static List<DyeColor> colorsAndBifrost() {
		List<DyeColor> list = new ArrayList<>(List.of(DyeColor.values()));
		list.add(null);
		return list;
	}

	private static <T> void optional(TagsProvider.TagAppender<T> appender, ResourceLocation id) {
		appender.addOptional(id);
	}

	private static ResourceLocation blockId(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block);
	}

	private static ResourceLocation itemId(ItemLike item) {
		return BuiltInRegistries.ITEM.getKey(item.asItem());
	}

	public static void addBlockTags(ModuleTagSink<Block> sink) {
		var iridescentDirt = sink.tag(BotaniaExtrasTags.Blocks.IRIDESCENT_DIRT);
		var iridescentGrass = sink.tag(BotaniaExtrasTags.Blocks.IRIDESCENT_GRASS);
		var dirt = sink.tag(BlockTags.DIRT);
		var shovel = sink.tag(BlockTags.MINEABLE_WITH_SHOVEL);
		var replaceableByTrees = sink.tag(BlockTags.REPLACEABLE_BY_TREES);
		var swordEfficient = sink.tag(BlockTags.SWORD_EFFICIENT);
		for (DyeColor color : colorsAndBifrost()) {
			ResourceLocation dirtId = blockId(BotaniaExtrasBlocks.getDirt(color));
			ResourceLocation grassId = blockId(BotaniaExtrasBlocks.getGrass(color));
			ResourceLocation tallId = blockId(BotaniaExtrasBlocks.getTallGrass(color));
			optional(iridescentDirt, dirtId);
			optional(dirt, dirtId);
			optional(shovel, dirtId);
			optional(iridescentGrass, grassId);
			optional(replaceableByTrees, grassId);
			optional(replaceableByTrees, tallId);
			optional(swordEfficient, grassId);
			optional(swordEfficient, tallId);
		}
		optional(sink.tag(BlockTags.SMALL_FLOWERS), blockId(BotaniaExtrasBlocks.bifrostFlower));
		optional(sink.tag(BlockTags.TALL_FLOWERS), blockId(BotaniaExtrasBlocks.tallBifrostFlower));
		optional(swordEfficient, blockId(BotaniaExtrasBlocks.bifrostFlower));
		optional(swordEfficient, blockId(BotaniaExtrasBlocks.tallBifrostFlower));

		var pickaxe = sink.tag(BlockTags.MINEABLE_WITH_PICKAXE);
		for (Block b : List.of(BotaniaExtrasBlocks.shimmerQuartz, BotaniaExtrasBlocks.shimmerQuartzChiseled,
				BotaniaExtrasBlocks.shimmerQuartzPillar, BotaniaExtrasBlocks.shimmerQuartzSlab, BotaniaExtrasBlocks.shimmerQuartzStairs)) {
			optional(pickaxe, blockId(b));
		}
		optional(sink.tag(BlockTags.SLABS), blockId(BotaniaExtrasBlocks.shimmerQuartzSlab));
		optional(sink.tag(BlockTags.STAIRS), blockId(BotaniaExtrasBlocks.shimmerQuartzStairs));

		var logs = sink.tag(BlockTags.LOGS);
		var logsThatBurn = sink.tag(BlockTags.LOGS_THAT_BURN);
		var planks = sink.tag(BlockTags.PLANKS);
		var woodenSlabs = sink.tag(BlockTags.WOODEN_SLABS);
		var woodenStairs = sink.tag(BlockTags.WOODEN_STAIRS);
		var slabs = sink.tag(BlockTags.SLABS);
		var stairs = sink.tag(BlockTags.STAIRS);
		var leaves = sink.tag(BlockTags.LEAVES);
		var axe = sink.tag(BlockTags.MINEABLE_WITH_AXE);
		var hoe = sink.tag(BlockTags.MINEABLE_WITH_HOE);
		for (BotaniaExtrasWoods.WoodSet set : BotaniaExtrasWoods.sets()) {
			optional(logs, blockId(set.log()));
			optional(logsThatBurn, blockId(set.log()));
			optional(planks, blockId(set.planks()));
			optional(woodenSlabs, blockId(set.slab()));
			optional(woodenStairs, blockId(set.stairs()));
			optional(slabs, blockId(set.slab()));
			optional(stairs, blockId(set.stairs()));
			optional(leaves, blockId(set.leaves()));
			optional(axe, blockId(set.log()));
			optional(axe, blockId(set.planks()));
			optional(axe, blockId(set.slab()));
			optional(axe, blockId(set.stairs()));
			optional(hoe, blockId(set.leaves()));
		}
		optional(swordEfficient, blockId(BotaniaExtrasWoods.sapling));

		var suffuserPlanks = sink.tag(BotaniaExtrasTags.Blocks.SUFFUSER_PLANKS);
		for (BotaniaExtrasWoods.WoodSet set : BotaniaExtrasWoods.sets()) {
			if (set.kind() != BotaniaExtrasWoods.Kind.ALT) {
				optional(suffuserPlanks, blockId(set.planks()));
			}
		}

		var sealingWood = sink.tag(BotaniaExtrasTags.Blocks.SEALING_WOOD);
		var infiniburn = sink.tag(BlockTags.INFINIBURN_OVERWORLD);
		for (BotaniaExtrasMagicWoods.MagicSet set : BotaniaExtrasMagicWoods.sets()) {
			optional(logs, blockId(set.log()));
			optional(planks, blockId(set.planks()));
			optional(woodenSlabs, blockId(set.slab()));
			optional(woodenStairs, blockId(set.stairs()));
			optional(slabs, blockId(set.slab()));
			optional(stairs, blockId(set.stairs()));
			optional(leaves, blockId(set.leaves()));
			optional(axe, blockId(set.log()));
			optional(axe, blockId(set.planks()));
			optional(axe, blockId(set.slab()));
			optional(axe, blockId(set.stairs()));
			optional(hoe, blockId(set.leaves()));
			optional(swordEfficient, blockId(set.sapling()));
			if (set.type() == BotaniaExtrasMagicWoods.Type.INFERNAL) {
				optional(infiniburn, blockId(set.log()));
			} else {
				optional(logsThatBurn, blockId(set.log()));
			}
			if (set.type() == BotaniaExtrasMagicWoods.Type.SEALING) {
				for (Block block : List.of(set.log(), set.planks(), set.leaves(), set.slab(), set.stairs())) {
					optional(sealingWood, blockId(block));
				}
			}
		}

		for (Block platform : List.of(BotaniaExtrasBlocks.manasteelItemPlatform, BotaniaExtrasBlocks.terrasteelItemPlatform, BotaniaExtrasBlocks.elementiumItemPlatform,
				BotaniaExtrasBlocks.dendricSuffuser, BotaniaExtrasBlocks.sonicAmplifier)) {
			optional(axe, blockId(platform));
		}
		optional(sink.tag(BotaniaExtrasTags.Blocks.SOUND_AMPLIFIERS), blockId(BotaniaExtrasBlocks.sonicAmplifier));
	}

	public static void addAccessoryTags(ModuleTagSink<Item> sink) {
		var necklace = sink.tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("curios", "necklace")));
		optional(necklace, itemId(BotaniaExtrasItems.priestEmblemThor));
		optional(necklace, itemId(BotaniaExtrasItems.priestEmblemSif));
		optional(necklace, itemId(BotaniaExtrasItems.priestEmblemNjord));
		optional(necklace, itemId(BotaniaExtrasItems.aesirEmblem));
	}

	public static void addItemTags(ModuleTagSink<Item> sink) {
		var iridescentDirt = sink.tag(BotaniaExtrasTags.Items.IRIDESCENT_DIRT);
		var iridescentGrass = sink.tag(BotaniaExtrasTags.Items.IRIDESCENT_GRASS);
		var seeds = sink.tag(BotaniaExtrasTags.Items.IRIDESCENT_SEEDS);
		var dirt = sink.tag(ItemTags.DIRT);
		for (DyeColor color : colorsAndBifrost()) {
			optional(iridescentDirt, itemId(BotaniaExtrasBlocks.getDirt(color)));
			optional(dirt, itemId(BotaniaExtrasBlocks.getDirt(color)));
			optional(iridescentGrass, itemId(BotaniaExtrasBlocks.getGrass(color)));
			optional(seeds, itemId(BotaniaExtrasItems.getSeeds(color)));
		}
		sink.tag(BotaniaExtrasTags.Items.BIFROST_DYES).add(ResourceKey.create(Registries.ITEM, itemId(BotaniaBlocks.bifrostPerm)));
		optional(sink.tag(BotaniaExtrasTags.Items.BIFROST_DYES), itemId(BotaniaExtrasItems.floralBifrostPowder));
		optional(sink.tag(BotaniaTags.Items.PETALS), itemId(BotaniaExtrasItems.mysticalBifrostPetal));
		optional(sink.tag(ItemTags.SMALL_FLOWERS), itemId(BotaniaExtrasBlocks.bifrostFlower));
		optional(sink.tag(ItemTags.TALL_FLOWERS), itemId(BotaniaExtrasBlocks.tallBifrostFlower));

		var rods = sink.tag(BotaniaTags.Items.RODS);
		var manaUsing = sink.tag(BotaniaTags.Items.MANA_USING_ITEMS);
		for (ItemLike rod : List.of(BotaniaExtrasItems.vibrantPlainsRod, BotaniaExtrasItems.thunderingPeaksRod, BotaniaExtrasItems.stormySeaRod, BotaniaExtrasItems.prismaticLakeRod)) {
			optional(rods, itemId(rod));
			optional(manaUsing, itemId(rod));
		}
		optional(sink.tag(BotaniaTags.Items.PICKABLE_BLOCK_PROVIDER), itemId(BotaniaExtrasItems.vibrantPlainsRod));
		optional(sink.tag(BotaniaTags.Items.LENS), itemId(BotaniaExtrasItems.phantomFlashLens));
		optional(manaUsing, itemId(BotaniaExtrasItems.priestEmblemThor));
		optional(manaUsing, itemId(BotaniaExtrasItems.priestEmblemSif));
		optional(manaUsing, itemId(BotaniaExtrasItems.priestEmblemNjord));
		optional(manaUsing, itemId(BotaniaExtrasItems.aesirEmblem));

		var logs = sink.tag(ItemTags.LOGS);
		var logsThatBurn = sink.tag(ItemTags.LOGS_THAT_BURN);
		var planks = sink.tag(ItemTags.PLANKS);
		var woodenSlabs = sink.tag(ItemTags.WOODEN_SLABS);
		var woodenStairs = sink.tag(ItemTags.WOODEN_STAIRS);
		var slabs = sink.tag(ItemTags.SLABS);
		var stairs = sink.tag(ItemTags.STAIRS);
		var leaves = sink.tag(ItemTags.LEAVES);
		for (BotaniaExtrasWoods.WoodSet set : BotaniaExtrasWoods.sets()) {
			optional(logs, itemId(set.log()));
			optional(logsThatBurn, itemId(set.log()));
			optional(planks, itemId(set.planks()));
			optional(woodenSlabs, itemId(set.slab()));
			optional(woodenStairs, itemId(set.stairs()));
			optional(slabs, itemId(set.slab()));
			optional(stairs, itemId(set.stairs()));
			optional(leaves, itemId(set.leaves()));
		}
		for (BotaniaExtrasMagicWoods.MagicSet set : BotaniaExtrasMagicWoods.sets()) {
			optional(logs, itemId(set.log()));
			optional(planks, itemId(set.planks()));
			optional(woodenSlabs, itemId(set.slab()));
			optional(woodenStairs, itemId(set.stairs()));
			optional(slabs, itemId(set.slab()));
			optional(stairs, itemId(set.stairs()));
			optional(leaves, itemId(set.leaves()));
			if (set.type() != BotaniaExtrasMagicWoods.Type.INFERNAL) {
				optional(logsThatBurn, itemId(set.log()));
			}
		}
	}
}
