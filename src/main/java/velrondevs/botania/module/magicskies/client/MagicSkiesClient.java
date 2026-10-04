package velrondevs.botania.module.magicskies.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;

import velrondevs.botania.module.magicskies.client.render.sky.MagicSkyRenderer;

public final class MagicSkiesClient {
	private MagicSkiesClient() {}

	public static void init() {
		NeoForge.EVENT_BUS.addListener(MagicSkiesClient::onRenderLevelStage);
	}

	private static void onRenderLevelStage(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) {
			return;
		}

		float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
		Camera camera = mc.gameRenderer.getMainCamera();
		PoseStack ms = new PoseStack();
		ms.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
		ms.mulPose(Axis.YP.rotationDegrees(camera.getYRot() + 180.0F));

		MagicSkyRenderer.render(ms, event.getProjectionMatrix(), mc.level, partialTick, 0F);
	}
}
