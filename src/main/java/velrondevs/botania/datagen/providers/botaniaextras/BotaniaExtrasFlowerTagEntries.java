package velrondevs.botania.datagen.providers.botaniaextras;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.module.ModuleTagSink;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasFlowers;

public final class BotaniaExtrasFlowerTagEntries {
	private BotaniaExtrasFlowerTagEntries() {}

	private static ResourceLocation blockId(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block);
	}

	public static void addBlockTags(ModuleTagSink<Block> sink) {
		sink.tag(BotaniaTags.Blocks.GENERATING_SPECIAL_FLOWERS).addOptional(blockId(BotaniaExtrasFlowers.crysanthermum));
		sink.tag(BotaniaTags.Blocks.GENERATING_FLOATING_FLOWERS).addOptional(blockId(BotaniaExtrasFlowers.crysanthermumFloating));
		sink.tag(BlockTags.FLOWER_POTS).addOptional(blockId(BotaniaExtrasFlowers.crysanthermumPotted));
	}
}
