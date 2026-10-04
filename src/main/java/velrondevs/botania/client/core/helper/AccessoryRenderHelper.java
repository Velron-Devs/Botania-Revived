package velrondevs.botania.client.core.helper;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.LivingEntity;

import velrondevs.botania.common.helper.VecHelper;

public final class AccessoryRenderHelper {

	public static void rotateIfSneaking(PoseStack ms, LivingEntity living) {
		if (living.isCrouching()) {
			ms.translate(0F, 0.2F, 0F);
			ms.mulPose(VecHelper.rotateX(90F / (float) Math.PI));
		}
	}

}
