package velrondevs.botania.datagen.providers.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;

import velrondevs.botania.common.lib.BotaniaTags;

import java.util.concurrent.CompletableFuture;

public class BiomeTagProvider extends TagsProvider<Biome> {
	public BiomeTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, Registries.BIOME, lookupProvider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {

		tag(BiomeTags.IS_OVERWORLD);
		tag(BiomeTags.IS_NETHER);

		tag(BotaniaTags.Biomes.MYSTICAL_FLOWER_SPAWNLIST).addOptionalTag(BiomeTags.IS_OVERWORLD);
		tag(BotaniaTags.Biomes.MYSTICAL_FLOWER_BLOCKLIST)
				.addOptionalTag(Tags.Biomes.IS_MUSHROOM);

		tag(BotaniaTags.Biomes.MYSTICAL_MUSHROOM_SPAWNLIST).addOptionalTag(BiomeTags.IS_OVERWORLD)
				.addOptionalTag(BiomeTags.IS_NETHER);
		tag(BotaniaTags.Biomes.MYSTICAL_MUSHROOM_BLOCKLIST);

		tag(BotaniaTags.Biomes.MARIMORPHOSIS_DESERT_BONUS).addOptionalTag(Tags.Biomes.IS_DESERT).addOptionalTag(BiomeTags.IS_SAVANNA);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_FOREST_BONUS).addOptionalTag(BiomeTags.IS_FOREST);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_FUNGAL_BONUS).addOptionalTag(Tags.Biomes.IS_MUSHROOM).addOptionalTag(Tags.Biomes.IS_UNDERGROUND);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_MESA_BONUS).addOptionalTag(BiomeTags.IS_BADLANDS).addOptionalTag(BiomeTags.IS_SAVANNA);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_MOUNTAIN_BONUS).addOptionalTag(BiomeTags.IS_MOUNTAIN);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_PLAINS_BONUS).addOptionalTag(Tags.Biomes.IS_PLAINS).addOptionalTag(BiomeTags.IS_BEACH);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_SWAMP_BONUS).addOptionalTag(Tags.Biomes.IS_SWAMP).addOptionalTag(BiomeTags.IS_JUNGLE);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_TAIGA_BONUS).addOptionalTag(Tags.Biomes.IS_CONIFEROUS_TREE).addOptionalTag(Tags.Biomes.IS_COLD).addOptionalTag(Tags.Biomes.IS_SNOWY);
	}
}
