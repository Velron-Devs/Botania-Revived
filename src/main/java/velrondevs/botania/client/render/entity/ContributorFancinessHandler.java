package velrondevs.botania.client.render.entity;

import velrondevs.botania.client.core.helper.RenderHelper;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.client.core.handler.MiscellaneousModels;
import velrondevs.botania.common.handler.ContributorList;

import java.util.*;

public final class ContributorFancinessHandler extends BotaniaPlayerLayer {

	public ContributorFancinessHandler(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
		super(renderer);
	}

	@Override
	protected void render(PoseStack ms, MultiBufferSource buffers, SubmitNodeCollector collector, int light, AbstractClientPlayer player, float partialTicks) {
		ContributorList.firstStart();

		if (player.isInvisible()) {
			return;
		}

		String name = player.getGameProfile().name();

		if (name.equals("haighyorkie")) {
			renderGoldfish(ms, buffers);
		}

		if (player.isModelPartShown(PlayerModelPart.CAPE)) {
			ItemStack flower = ContributorList.getFlower(name.toLowerCase(Locale.ROOT));
			if (!flower.isEmpty()) {
				renderFlower(ms, collector, player, flower);
			}
		}

	}

	private void renderGoldfish(PoseStack ms, MultiBufferSource buffers) {
		ms.pushPose();
		getParentModel().head.translateAndRotate(ms);
		ms.translate(-0.15F, -0.60F, 0F);
		ms.scale(0.4F, -0.4F, -0.4F);
		RenderHelper.renderModelPart(MiscellaneousModels.INSTANCE.goldfishModel, ms, buffers, 0xF000F0, OverlayTexture.NO_OVERLAY);
		ms.popPose();
	}

	private void renderFlower(PoseStack ms, SubmitNodeCollector collector, Player player, ItemStack flower) {
		ms.pushPose();
		getParentModel().head.translateAndRotate(ms);
		ms.translate(0, -0.75, 0);
		ms.scale(0.5F, -0.5F, -0.5F);
		RenderHelper.renderItem(player, flower, ItemDisplayContext.NONE, ms, collector, 0xF000F0, OverlayTexture.NO_OVERLAY);
		ms.popPose();
	}
}
