package velrondevs.botania.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record MysticalFlowerConfig(float tallChance, BlockStateProvider toPlace) implements FeatureConfiguration {
	public static final Codec<MysticalFlowerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.fieldOf("tall_chance").forGetter(MysticalFlowerConfig::tallChance),
			BlockStateProvider.CODEC.fieldOf("to_place").forGetter(MysticalFlowerConfig::toPlace)
	).apply(instance, MysticalFlowerConfig::new));
}
