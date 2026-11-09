package velrondevs.botania.datagen.providers.botaniaextras;

import net.minecraft.tags.TagEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
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

	private static Identifier itemId(ItemLike item) {
		return BuiltInRegistries.ITEM.getKey(item.asItem());
	}

	private static Identifier curios(String slot) {
		return Identifier.fromNamespaceAndPath("curios", slot);
	}

	public static void addBlockTags(ModuleTagSink<Block> sink) {
		sink.tag(BlockTags.MINEABLE_WITH_AXE).add(TagEntry.optionalElement(BuiltInRegistries.BLOCK.getKey(BotaniaExtrasUtilities.livingwoodFunnel)));
	}

	public static void addItemTags(ModuleTagSink<Item> sink) {
		var blacklist = sink.tag(ToolbeltItem.BLACKLIST);
		blacklist.add(TagEntry.optionalElement(itemId(BotaniaExtrasUtilities.toolbelt)));
		blacklist.add(TagEntry.element(itemId(BotaniaItems.baubleBox)));
	}

	public static void addAccessoryTags(ModuleTagSink<Item> sink) {
		sink.tag(ItemTags.create(curios("belt"))).add(TagEntry.optionalElement(itemId(BotaniaExtrasUtilities.toolbelt)));
		sink.tag(ItemTags.create(curios("ring"))).add(TagEntry.optionalElement(itemId(BotaniaExtrasUtilities.clericalColorizer)));
		var necklace = sink.tag(ItemTags.create(curios("necklace")));
		for (Item coat : BotaniaExtrasUtilities.coats()) {
			necklace.add(TagEntry.optionalElement(itemId(coat)));
		}
	}
}
