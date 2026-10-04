package velrondevs.botania.common.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BannerPattern;

public class BotaniaBannerPatternItem extends Item implements ItemWithBannerPattern {
	private final TagKey<BannerPattern> pattern;

	public BotaniaBannerPatternItem(TagKey<BannerPattern> pattern, Properties settings) {
		super(settings);
		this.pattern = pattern;
	}

	@Override
	public TagKey<BannerPattern> getBannerPattern() {
		return pattern;
	}
}
