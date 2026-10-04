package velrondevs.botania.datagen.providers.botaniaextras;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import velrondevs.botania.module.ModuleTagSink;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasUtilities;
import velrondevs.botania.module.botaniaextras.item.ToolbeltItem;
import velrondevs.botania.registry.BotaniaItems;

public final class BotaniaExtrasUtilityTags {
	private BotaniaExtrasUtilityTags() {}

	private static ResourceLocation itemId(ItemLike item) {
		return BuiltInRegistries.ITEM.getKey(item.asItem());
	}

	private static ResourceLocation curios(String slot) {
		return ResourceLocation.fromNamespaceAndPath("curios", slot);
	}

	public static void addBlockTags(ModuleTagSink<Block> sink) {
		sink.tag(BlockTags.MINEABLE_WITH_AXE).addOptional(BuiltInRegistries.BLOCK.getKey(BotaniaExtrasUtilities.livingwoodFunnel));
	}

	public static void addItemTags(ModuleTagSink<Item> sink) {
		var blacklist = sink.tag(ToolbeltItem.BLACKLIST);
		blacklist.addOptional(itemId(BotaniaExtrasUtilities.toolbelt));
		blacklist.add(ResourceKey.create(Registries.ITEM, itemId(BotaniaItems.baubleBox)));
		sink.tag(ItemTags.DYEABLE).addOptional(itemId(BotaniaExtrasUtilities.clericalColorizer));
	}

	public static void addAccessoryTags(ModuleTagSink<Item> sink) {
		sink.tag(ItemTags.create(curios("belt"))).addOptional(itemId(BotaniaExtrasUtilities.toolbelt));
		sink.tag(ItemTags.create(curios("ring"))).addOptional(itemId(BotaniaExtrasUtilities.clericalColorizer));
		var necklace = sink.tag(ItemTags.create(curios("necklace")));
		for (Item coat : BotaniaExtrasUtilities.coats()) {
			necklace.addOptional(itemId(coat));
		}
	}
}
