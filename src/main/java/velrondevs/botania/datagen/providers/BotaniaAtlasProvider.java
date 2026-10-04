package velrondevs.botania.datagen.providers;

import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class BotaniaAtlasProvider extends SpriteSourceProvider {
	private static final String[] SPRITES = {
			"item/corporea_spark",
			"item/corporea_spark_creative",
			"item/corporea_spark_master",
			"item/corporea_spark_star",
			"item/spark",
			"item/spark_upgrade_rune_dispersive",
			"item/spark_upgrade_rune_dominant",
			"item/spark_upgrade_rune_isolated",
			"item/spark_upgrade_rune_recessive",
			"block/alfheim_portal_swirl",
			"block/light_relay",
			"block/detector_light_relay",
			"block/fork_light_relay",
			"block/toggle_light_relay"
	};

	public BotaniaAtlasProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, String modId, ExistingFileHelper fileHelper) {
		super(output, lookup, modId, fileHelper);
	}

	@Override
	protected void gather() {
		var atlas = atlas(BLOCKS_ATLAS);
		for (String sprite : SPRITES) {
			atlas.addSource(new SingleFile(ResourceLocation.fromNamespaceAndPath("botania", sprite), Optional.empty()));
		}
	}
}
