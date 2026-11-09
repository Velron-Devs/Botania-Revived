package velrondevs.botania.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;

import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.client.core.proxy.ClientProxy;
import velrondevs.botania.client.lib.ResourcesLib;
import velrondevs.botania.client.model.BotaniaModelLayers;
import velrondevs.botania.client.model.PixieModel;
import velrondevs.botania.common.entity.PixieEntity;

public class PixieRenderer extends MobRenderer<PixieEntity, PixieRenderer.State, PixieModel> {
	public static class State extends LivingEntityRenderState {
		public boolean evil;
	}

	public PixieRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new PixieModel(ctx.bakeLayer(BotaniaModelLayers.PIXIE)), 0.0F);
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(PixieEntity entity, State state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.evil = entity.getPixieType() == 1;
	}

	@Override
	protected @Nullable RenderType getRenderType(State state, boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing) {
		if (state.evil && isBodyVisible) {
			return RenderHelper.getDopplegangerLayer(getTextureLocation(state));
		}
		return super.getRenderType(state, isBodyVisible, forceTransparent, appearGlowing);
	}

	@Override
	public Identifier getTextureLocation(State state) {
		return ClientProxy.dootDoot
				? Identifier.parse(ResourcesLib.MODEL_PIXIE_HALLOWEEN)
				: Identifier.parse(ResourcesLib.MODEL_PIXIE);
	}
}
