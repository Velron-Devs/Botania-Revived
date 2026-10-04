package velrondevs.botania.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.MeshData;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import velrondevs.botania.module.BotaniaModules;
import velrondevs.botania.module.magicskies.MagicSkiesModule;
import velrondevs.botania.module.magicskies.client.render.sky.MagicSkyRenderer;

@Mixin(LevelRenderer.class)
public abstract class MagicSkiesLevelRendererMixin {
	@Unique
	private static final ResourceLocation BOTANIA_MOON = ResourceLocation.withDefaultNamespace("textures/environment/moon_phases.png");

	@Unique
	private static final ResourceLocation BOTANIA_SUN = ResourceLocation.withDefaultNamespace("textures/environment/sun.png");

	@Shadow
	@Nullable
	private ClientLevel level;

	@Unique
	private boolean botania$skipCelestialDraw = false;

	@Redirect(
		method = "renderSky",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"),
		require = 0
	)
	private void botania$trackCelestialTexture(int index, ResourceLocation texture) {
		botania$skipCelestialDraw = (texture.equals(BOTANIA_MOON) || texture.equals(BOTANIA_SUN)) && botania$magicSkyActive();
		RenderSystem.setShaderTexture(index, texture);
	}

	@Redirect(
		method = "renderSky",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferUploader;drawWithShader(Lcom/mojang/blaze3d/vertex/MeshData;)V"),
		require = 0
	)
	private void botania$skipCelestialDraw(MeshData meshData) {
		if (botania$skipCelestialDraw) {
			botania$skipCelestialDraw = false;
			meshData.close();
		} else {
			BufferUploader.drawWithShader(meshData);
		}
	}

	@Unique
	private boolean botania$magicSkyActive() {
		return level != null
				&& BotaniaModules.isEnabled(MagicSkiesModule.ID)
				&& MagicSkyRenderer.isActive(level);
	}
}
