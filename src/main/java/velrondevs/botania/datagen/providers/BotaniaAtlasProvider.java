package velrondevs.botania.datagen.providers;

import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaAtlasProvider extends net.neoforged.neoforge.client.data.SpriteSourceProvider {
	private static final String[] SPRITES = {
			"block/alfheim_portal_swirl",
			"block/light_relay",
			"block/detector_light_relay",
			"block/fork_light_relay",
			"block/toggle_light_relay"
	};

	public BotaniaAtlasProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, String modId) {
		super(output, lookup, modId);
	}

	@Override
	protected void gather() {
		var atlas = atlas(AtlasIds.BLOCKS);
		for (String sprite : SPRITES) {
			atlas.addSource(new SingleFile(prefix(sprite)));
		}
	}
}
