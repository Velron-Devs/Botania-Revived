package velrondevs.botania.integration.sodium;

import net.caffeinemc.mods.sodium.api.texture.SpriteUtil;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class SodiumHelper {
	public static void markSpriteActive(TextureAtlasSprite sprite) {
		SpriteUtil.INSTANCE.markSpriteActive(sprite);
	}
}
