package velrondevs.botania.integration.sodium;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.BotaniaAPI;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public final class SodiumHelper {
	@Nullable
	private static final MethodHandle MARK_SPRITE_ACTIVE = lookup();

	private SodiumHelper() {}

	@Nullable
	private static MethodHandle lookup() {
		try {
			Class<?> spriteUtil = Class.forName("net.caffeinemc.mods.sodium.api.texture.SpriteUtil");
			Object instance = spriteUtil.getField("INSTANCE").get(null);
			MethodHandle handle = MethodHandles.publicLookup()
					.findVirtual(spriteUtil, "markSpriteActive", MethodType.methodType(void.class, TextureAtlasSprite.class));
			return handle.bindTo(instance);
		} catch (ReflectiveOperationException | LinkageError e) {
			BotaniaAPI.LOGGER.warn("Sodium SpriteUtil API not available", e);
			return null;
		}
	}

	public static void markSpriteActive(TextureAtlasSprite sprite) {
		if (MARK_SPRITE_ACTIVE == null) {
			return;
		}
		try {
			MARK_SPRITE_ACTIVE.invokeExact(sprite);
		} catch (Throwable ignored) {
		}
	}
}
