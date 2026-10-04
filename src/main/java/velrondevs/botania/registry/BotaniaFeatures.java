package velrondevs.botania.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.BiConsumer;

import velrondevs.botania.common.world.MysticalFlowerConfig;
import velrondevs.botania.common.world.MysticalFlowerFeature;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaFeatures {
	public static final ResourceKey<PlacedFeature> MYSTICAL_FLOWERS_PLACED_FEATURE =
			ResourceKey.create(Registries.PLACED_FEATURE, prefix("mystical_flowers"));
	public static final ResourceKey<PlacedFeature> MYSTICAL_MUSHROOMS_PLACED_FEATURE =
			ResourceKey.create(Registries.PLACED_FEATURE, prefix("mystical_mushrooms"));

	public static void registerFeatures(BiConsumer<Feature<?>, ResourceLocation> r) {
		r.accept(new MysticalFlowerFeature(MysticalFlowerConfig.CODEC), prefix("mystical_flower"));
	}

}
