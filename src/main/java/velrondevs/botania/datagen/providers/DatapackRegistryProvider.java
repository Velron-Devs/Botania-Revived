package velrondevs.botania.datagen.providers;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.common.lib.LibItemNames;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.common.world.MysticalFlowerConfig;
import velrondevs.botania.registry.BotaniaBannerPatterns;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaDamageTypes;
import velrondevs.botania.registry.BotaniaFeatures;
import velrondevs.botania.registry.BotaniaSounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class DatapackRegistryProvider extends DatapackBuiltinEntriesProvider {
	public static final ResourceKey<ConfiguredFeature<?, ?>> MYSTICAL_FLOWERS = configuredKey("mystical_flowers");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MYSTICAL_MUSHROOMS = configuredKey("mystical_mushrooms");
	public static final ResourceKey<BiomeModifier> ADD_MYSTICAL_FLOWERS = biomeModifierKey("add_mystical_flowers");
	public static final ResourceKey<BiomeModifier> ADD_MYSTICAL_MUSHROOMS = biomeModifierKey("add_mystical_mushrooms");
	public static final ResourceKey<BiomeModifier> REMOVE_MYSTICAL_FLOWERS = biomeModifierKey("remove_mystical_flowers");
	public static final ResourceKey<BiomeModifier> REMOVE_MYSTICAL_MUSHROOMS = biomeModifierKey("remove_mystical_mushrooms");

	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, DatapackRegistryProvider::configuredFeatures)
			.add(Registries.PLACED_FEATURE, DatapackRegistryProvider::placedFeatures)
			.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, DatapackRegistryProvider::biomeModifiers)
			.add(Registries.BANNER_PATTERN, BotaniaBannerPatterns::bootstrap)
			.add(Registries.DAMAGE_TYPE, DatapackRegistryProvider::damageTypes)
			.add(Registries.JUKEBOX_SONG, DatapackRegistryProvider::jukeboxSongs);

	public DatapackRegistryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BUILDER, Set.of(LibMisc.MOD_ID));
	}

	@Override
	public String getName() {
		return "Botania datapack registries";
	}

	public static ResourceKey<ConfiguredFeature<?, ?>> flowerPatchKey(DyeColor color) {
		return configuredKey(color.getSerializedName() + "_mystical_flower_patch");
	}

	@SuppressWarnings("unchecked")
	private static void configuredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		Feature<MysticalFlowerConfig> mysticalFlower = (Feature<MysticalFlowerConfig>) BuiltInRegistries.FEATURE.get(prefix("mystical_flower"));
		HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);

		List<Holder<PlacedFeature>> patches = new ArrayList<>();
		for (DyeColor color : DyeColor.values()) {
			ResourceKey<ConfiguredFeature<?, ?>> key = flowerPatchKey(color);
			context.register(key, new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(32, 6, 4,
					PlacementUtils.filtered(mysticalFlower,
							new MysticalFlowerConfig(0.05F, BlockStateProvider.simple(BotaniaBlocks.getFlower(color))),
							BlockPredicate.matchesBlocks(Blocks.AIR)))));
			patches.add(PlacementUtils.inlinePlaced(configured.getOrThrow(key)));
		}
		context.register(MYSTICAL_FLOWERS, new ConfiguredFeature<>(Feature.SIMPLE_RANDOM_SELECTOR,
				new SimpleRandomFeatureConfiguration(HolderSet.direct(patches))));

		SimpleWeightedRandomList.Builder<BlockState> mushrooms = SimpleWeightedRandomList.builder();
		for (DyeColor color : DyeColor.values()) {
			mushrooms.add(BotaniaBlocks.getMushroom(color).defaultBlockState(), 1);
		}
		context.register(MYSTICAL_MUSHROOMS, new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(40, 16, 4,
				PlacementUtils.filtered(Feature.SIMPLE_BLOCK,
						new SimpleBlockConfiguration(new WeightedStateProvider(mushrooms)),
						BlockPredicate.matchesBlocks(Blocks.AIR)))));
	}

	private static void placedFeatures(BootstrapContext<PlacedFeature> context) {
		HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);
		context.register(BotaniaFeatures.MYSTICAL_FLOWERS_PLACED_FEATURE, new PlacedFeature(configured.getOrThrow(MYSTICAL_FLOWERS), List.of(
				CountPlacement.of(2),
				RarityFilter.onAverageOnceEvery(16),
				InSquarePlacement.spread(),
				HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
				BiomeFilter.biome())));
		context.register(BotaniaFeatures.MYSTICAL_MUSHROOMS_PLACED_FEATURE, new PlacedFeature(configured.getOrThrow(MYSTICAL_MUSHROOMS), List.of(
				InSquarePlacement.spread(),
				HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.absolute(30)),
				BiomeFilter.biome())));
	}

	private static void biomeModifiers(BootstrapContext<BiomeModifier> context) {
		HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
		HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
		HolderSet<PlacedFeature> flowers = HolderSet.direct(placed.getOrThrow(BotaniaFeatures.MYSTICAL_FLOWERS_PLACED_FEATURE));
		HolderSet<PlacedFeature> mushrooms = HolderSet.direct(placed.getOrThrow(BotaniaFeatures.MYSTICAL_MUSHROOMS_PLACED_FEATURE));

		context.register(ADD_MYSTICAL_FLOWERS, new BiomeModifiers.AddFeaturesBiomeModifier(
				biomes.getOrThrow(BotaniaTags.Biomes.MYSTICAL_FLOWER_SPAWNLIST), flowers, GenerationStep.Decoration.VEGETAL_DECORATION));
		context.register(ADD_MYSTICAL_MUSHROOMS, new BiomeModifiers.AddFeaturesBiomeModifier(
				biomes.getOrThrow(BotaniaTags.Biomes.MYSTICAL_MUSHROOM_SPAWNLIST), mushrooms, GenerationStep.Decoration.VEGETAL_DECORATION));
		context.register(REMOVE_MYSTICAL_FLOWERS, BiomeModifiers.RemoveFeaturesBiomeModifier.allSteps(
				biomes.getOrThrow(BotaniaTags.Biomes.MYSTICAL_FLOWER_BLOCKLIST), flowers));
		context.register(REMOVE_MYSTICAL_MUSHROOMS, BiomeModifiers.RemoveFeaturesBiomeModifier.allSteps(
				biomes.getOrThrow(BotaniaTags.Biomes.MYSTICAL_MUSHROOM_BLOCKLIST), mushrooms));
	}

	private static void damageTypes(BootstrapContext<DamageType> context) {
		context.register(BotaniaDamageTypes.PLAYER_ATTACK_ARMOR_PIERCING, BotaniaDamageTypes.PLAYER_AP);
		context.register(BotaniaDamageTypes.RELIC_DAMAGE, BotaniaDamageTypes.RELIC);
		context.register(BotaniaDamageTypes.KEY_EXPLOSION, BotaniaDamageTypes.KEY);
		context.register(BotaniaDamageTypes.LACK_OF_FAITH, BotaniaDamageTypes.FAITH);
	}

	private static void jukeboxSongs(BootstrapContext<JukeboxSong> context) {
		jukeboxSong(context, LibItemNames.RECORD_GAIA1, BotaniaSounds.gaiaMusic1, 202);
		jukeboxSong(context, LibItemNames.RECORD_GAIA2, BotaniaSounds.gaiaMusic2, 227);
	}

	private static void jukeboxSong(BootstrapContext<JukeboxSong> context, String name, SoundEvent sound, int lengthInSeconds) {
		context.register(ResourceKey.create(Registries.JUKEBOX_SONG, prefix(name)), new JukeboxSong(
				BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound),
				Component.translatable("item.botania." + name + ".desc"), lengthInSeconds, 1));
	}

	private static ResourceKey<ConfiguredFeature<?, ?>> configuredKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, prefix(name));
	}

	private static ResourceKey<BiomeModifier> biomeModifierKey(String name) {
		return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, prefix(name));
	}
}
