package velrondevs.botania.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;

import velrondevs.botania.common.lib.BotaniaTags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaBannerPatterns {
	private static final List<ResourceKey<BannerPattern>> ALL = new ArrayList<>();
	public static final ResourceKey<BannerPattern> FLOWER = make("flower");
	public static final ResourceKey<BannerPattern> LEXICON = make("lexicon");
	public static final ResourceKey<BannerPattern> LOGO = make("logo");
	public static final ResourceKey<BannerPattern> SAPLING = make("sapling");
	public static final ResourceKey<BannerPattern> TINY_POTATO = make("tiny_potato");
	public static final ResourceKey<BannerPattern> SPARK_DISPERSIVE = make("spark_dispersive");
	public static final ResourceKey<BannerPattern> SPARK_DOMINANT = make("spark_dominant");
	public static final ResourceKey<BannerPattern> SPARK_RECESSIVE = make("spark_recessive");
	public static final ResourceKey<BannerPattern> SPARK_ISOLATED = make("spark_isolated");

	public static final ResourceKey<BannerPattern> FISH = make("fish");
	public static final ResourceKey<BannerPattern> AXE = make("axe");
	public static final ResourceKey<BannerPattern> HOE = make("hoe");
	public static final ResourceKey<BannerPattern> PICKAXE = make("pickaxe");
	public static final ResourceKey<BannerPattern> SHOVEL = make("shovel");
	public static final ResourceKey<BannerPattern> SWORD = make("sword");

	public static final TagKey<BannerPattern> PATTERN_ITEM_LIVINGWOOD_TWIG = BotaniaTags.BannerPatterns.PATTERN_ITEM_LIVINGWOOD_TWIG;
	public static final TagKey<BannerPattern> PATTERN_ITEM_LEXICON = BotaniaTags.BannerPatterns.PATTERN_ITEM_LEXICON;
	public static final TagKey<BannerPattern> PATTERN_ITEM_TERRASTEEL = BotaniaTags.BannerPatterns.PATTERN_ITEM_TERRASTEEL;
	public static final TagKey<BannerPattern> PATTERN_ITEM_DREAMWOOD_TWIG = BotaniaTags.BannerPatterns.PATTERN_ITEM_DREAMWOOD_TWIG;
	public static final TagKey<BannerPattern> PATTERN_ITEM_TINY_POTATO = BotaniaTags.BannerPatterns.PATTERN_ITEM_TINY_POTATO;
	public static final TagKey<BannerPattern> PATTERN_ITEM_SPARK_DISPERSIVE = BotaniaTags.BannerPatterns.PATTERN_ITEM_SPARK_DISPERSIVE;
	public static final TagKey<BannerPattern> PATTERN_ITEM_SPARK_DOMINANT = BotaniaTags.BannerPatterns.PATTERN_ITEM_SPARK_DOMINANT;
	public static final TagKey<BannerPattern> PATTERN_ITEM_SPARK_RECESSIVE = BotaniaTags.BannerPatterns.PATTERN_ITEM_SPARK_RECESSIVE;
	public static final TagKey<BannerPattern> PATTERN_ITEM_SPARK_ISOLATED = BotaniaTags.BannerPatterns.PATTERN_ITEM_SPARK_ISOLATED;

	private static ResourceKey<BannerPattern> make(String hashName) {
		ResourceKey<BannerPattern> key = ResourceKey.create(Registries.BANNER_PATTERN, prefix(hashName));
		ALL.add(key);
		return key;
	}

	public static List<ResourceKey<BannerPattern>> all() {
		return Collections.unmodifiableList(ALL);
	}

	public static BannerPattern create(ResourceKey<BannerPattern> key) {
		return new BannerPattern(key.location(), "block.minecraft.banner." + key.location().toShortLanguageKey());
	}

	public static void bootstrap(BootstrapContext<BannerPattern> context) {
		for (var key : ALL) {
			context.register(key, create(key));
		}
	}
}
