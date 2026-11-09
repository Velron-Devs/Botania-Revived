package velrondevs.botania.client.render.entity;

import velrondevs.botania.client.core.helper.RenderHelper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.client.core.handler.MiscellaneousModels;
import velrondevs.botania.common.item.equipment.armor.terrasteel.TerrasteelHelmItem;

public class TerrasteelHelmetLayer extends BotaniaPlayerLayer {
	public TerrasteelHelmetLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
		super(renderer);
	}

	@Override
	protected void render(PoseStack ms, MultiBufferSource buffers, SubmitNodeCollector collector, int light, AbstractClientPlayer player, float partialTicks) {
		ItemStack helm = player.getItemBySlot(EquipmentSlot.HEAD);
		if (!helm.isEmpty() && helm.getItem() instanceof TerrasteelHelmItem terraHelm) {
			if (TerrasteelHelmItem.hasAnyWill(helm) && !terraHelm.hasPhantomInk(helm)) {
				ms.pushPose();
				getParentModel().head.translateAndRotate(ms);
				ms.translate(-0.2, -0.15, -0.3);
				ms.scale(0.4F, -0.4F, -0.4F);
				RenderHelper.renderModelPart(MiscellaneousModels.INSTANCE.terrasteelHelmWillModel, ms, buffers, light, OverlayTexture.NO_OVERLAY);
				ms.popPose();
			}
		}
	}
}
