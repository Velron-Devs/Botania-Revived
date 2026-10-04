package velrondevs.botania.module.magicskies.client.render.sky;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.multiplayer.ClientLevel;

import org.joml.Matrix4f;

public record SkyRenderContext(
		PoseStack poseStack,
		Matrix4f projectionMatrix,
		ClientLevel level,
		float partialTicks,
		float voidFade,
		SkyPalette palette) {

	public float baseIntensity() {
		float rain = 1.0F - level.getRainLevel(partialTicks);
		float celestialAngle = level.getTimeOfDay(partialTicks);
		float effective = celestialAngle > 0.5F ? 0.5F - (celestialAngle - 0.5F) : celestialAngle;
		return rain * effective;
	}
}
