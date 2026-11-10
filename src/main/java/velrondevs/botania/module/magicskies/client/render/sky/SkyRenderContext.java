package velrondevs.botania.module.magicskies.client.render.sky;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.multiplayer.ClientLevel;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;

import velrondevs.botania.client.render.world.SkyDrawHelper;

public record SkyRenderContext(
		PoseStack poseStack,
		Matrix4fc modelView,
		ClientLevel level,
		float partialTicks,
		float voidFade,
		SkyPalette palette,
		SkyDrawHelper.Frame frame) {

	public float baseIntensity() {
		float rain = 1.0F - frame.rainLevel();
		return rain * frame.effectiveCelestialAngle();
	}

	public float celestialAngle() {
		return frame.timeOfDay();
	}

	public float rainLevel() {
		return frame.rainLevel();
	}

	public float starBrightness() {
		return frame.starBrightness();
	}

	public long dayTime() {
		return frame.dayTime();
	}

	public float animTicks() {
		return frame.animTicks();
	}

	public Matrix4f currentModelView() {
		return SkyDrawHelper.modelView(modelView, poseStack);
	}
}
