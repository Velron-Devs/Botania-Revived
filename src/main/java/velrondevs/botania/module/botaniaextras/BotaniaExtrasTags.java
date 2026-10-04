package velrondevs.botania.module.botaniaextras;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaExtrasTags {
	private BotaniaExtrasTags() {}

	public static final class Items {
		public static final TagKey<Item> IRIDESCENT_DIRT = TagKey.create(Registries.ITEM, prefix("iridescent_dirt"));
		public static final TagKey<Item> IRIDESCENT_GRASS = TagKey.create(Registries.ITEM, prefix("iridescent_grass"));
		public static final TagKey<Item> IRIDESCENT_SEEDS = TagKey.create(Registries.ITEM, prefix("iridescent_seeds"));
		public static final TagKey<Item> BIFROST_DYES = TagKey.create(Registries.ITEM, prefix("bifrost_dyes"));

		private Items() {}
	}

	public static final class Blocks {
		public static final TagKey<Block> IRIDESCENT_DIRT = TagKey.create(Registries.BLOCK, prefix("iridescent_dirt"));
		public static final TagKey<Block> IRIDESCENT_GRASS = TagKey.create(Registries.BLOCK, prefix("iridescent_grass"));
		public static final TagKey<Block> SUFFUSER_PLANKS = TagKey.create(Registries.BLOCK, prefix("suffuser_planks"));
		public static final TagKey<Block> SEALING_WOOD = TagKey.create(Registries.BLOCK, prefix("sealing_wood"));
		public static final TagKey<Block> SOUND_AMPLIFIERS = TagKey.create(Registries.BLOCK, prefix("sound_amplifiers"));

		private Blocks() {}
	}
}
