package velrondevs.botania.common.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;

import velrondevs.botania.common.item.material.ManaResourceItem;
import velrondevs.botania.common.lib.BotaniaTags;

public class TerrasteelIngotItem extends ManaResourceItem implements ItemWithBannerPattern {
	public TerrasteelIngotItem(Properties props) {
		super(props);
	}

	@Override
	public TagKey<BannerPattern> getBannerPattern() {
		return BotaniaTags.BannerPatterns.PATTERN_ITEM_TERRASTEEL;
	}
}
