package velrondevs.botania.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;

import velrondevs.botania.client.core.helper.DeferredBufferSource;

public abstract class BotaniaPlayerLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
	public BotaniaPlayerLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
		super(renderer);
	}

	@Override
	public void submit(PoseStack ms, SubmitNodeCollector collector, int light, AvatarRenderState state, float yRot, float xRot) {
		var level = Minecraft.getInstance().level;
		if (level != null && level.getEntity(state.id) instanceof AbstractClientPlayer player) {
			render(ms, new DeferredBufferSource(collector), collector, light, player, state.partialTick);
		}
	}

	protected abstract void render(PoseStack ms, MultiBufferSource buffers, SubmitNodeCollector collector, int light, AbstractClientPlayer player, float partialTicks);
}
