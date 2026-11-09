package velrondevs.botania.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.common.entity.ManaStormEntity;

public class ManaStormRenderer extends BotaniaEntityRenderer<ManaStormEntity> {

	public ManaStormRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	protected void render(ManaStormEntity storm, float yaw, float pticks, PoseStack ms, MultiBufferSource buffers, SubmitNodeCollector collector, int light) {
		ms.pushPose();
		float maxScale = 1.95F;
		float scale = 0.05F + ((float) storm.burstsFired / ManaStormEntity.TOTAL_BURSTS - (storm.deathTime == 0 ? 0 : storm.deathTime + pticks) / ManaStormEntity.DEATH_TIME) * maxScale;
		RenderHelper.renderStar(ms, buffers, 0x00FF00, scale, scale, scale, storm.getUUID().getMostSignificantBits());
		ms.popPose();
	}
}
