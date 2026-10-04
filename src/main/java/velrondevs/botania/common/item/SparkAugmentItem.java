package velrondevs.botania.common.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;

import velrondevs.botania.api.mana.spark.SparkUpgradeType;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.registry.BotaniaItems;

public class SparkAugmentItem extends Item implements ItemWithBannerPattern {
	public final SparkUpgradeType type;

	public SparkAugmentItem(Properties builder, SparkUpgradeType type) {
		super(builder);
		this.type = type;
	}

	@Override
	public TagKey<BannerPattern> getBannerPattern() {
		return switch (this.type) {
			case DOMINANT -> BotaniaTags.BannerPatterns.PATTERN_ITEM_SPARK_DOMINANT;
			case RECESSIVE -> BotaniaTags.BannerPatterns.PATTERN_ITEM_SPARK_RECESSIVE;
			case DISPERSIVE -> BotaniaTags.BannerPatterns.PATTERN_ITEM_SPARK_DISPERSIVE;
			case ISOLATED -> BotaniaTags.BannerPatterns.PATTERN_ITEM_SPARK_ISOLATED;
			case NONE -> throw new IllegalArgumentException("SparkAugmentItem with none type");
		};
	}

	public static ItemStack getByType(SparkUpgradeType type) {
		return switch (type) {
			case DOMINANT -> new ItemStack(BotaniaItems.sparkUpgradeDominant);
			case RECESSIVE -> new ItemStack(BotaniaItems.sparkUpgradeRecessive);
			case DISPERSIVE -> new ItemStack(BotaniaItems.sparkUpgradeDispersive);
			case ISOLATED -> new ItemStack(BotaniaItems.sparkUpgradeIsolated);
			default -> ItemStack.EMPTY;
		};
	}

}
