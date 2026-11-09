package velrondevs.botania.datagen.providers.asgard;

import net.minecraft.tags.TagEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.module.ModuleTagSink;
import velrondevs.botania.module.asgard.AsgardFlowers;

public final class AsgardTagEntries {
	private AsgardTagEntries() {}

	private static Identifier blockId(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block);
	}

	public static void addBlockTags(ModuleTagSink<Block> sink) {
		sink.tag(BotaniaTags.Blocks.GENERATING_SPECIAL_FLOWERS).add(TagEntry.optionalElement(blockId(AsgardFlowers.asgardandelion)));
		sink.tag(BotaniaTags.Blocks.GENERATING_FLOATING_FLOWERS).add(TagEntry.optionalElement(blockId(AsgardFlowers.asgardandelionFloating)));
		sink.tag(BlockTags.FLOWER_POTS).add(TagEntry.optionalElement(blockId(AsgardFlowers.asgardandelionPotted)));
	}
}
