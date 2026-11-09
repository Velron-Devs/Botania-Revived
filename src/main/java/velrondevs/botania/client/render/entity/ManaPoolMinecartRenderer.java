package velrondevs.botania.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.AbstractMinecartRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

import velrondevs.botania.api.internal.ManaBurst;
import velrondevs.botania.client.render.block_entity.BotaniaBlockEntityRenderer;
import velrondevs.botania.client.render.block_entity.ManaPoolBlockEntityRenderer;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.common.entity.ManaPoolMinecartEntity;
import velrondevs.botania.registry.BotaniaBlocks;

public class ManaPoolMinecartRenderer extends AbstractMinecartRenderer<ManaPoolMinecartEntity, ManaPoolMinecartRenderer.State> {
	private static final ManaPoolBlockEntity DUMMY = new ManaPoolBlockEntity(ManaBurst.NO_SOURCE, BotaniaBlocks.manaPool.defaultBlockState());

	public static class State extends MinecartRenderState {
		public int mana;
	}

	public ManaPoolMinecartRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, ModelLayers.MINECART);
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(ManaPoolMinecartEntity entity, State state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.mana = entity.getMana();
	}

	@Override
	protected void submitMinecartContents(State state, BlockModelRenderState blockModel, PoseStack poseStack, SubmitNodeCollector collector, int lightCoords) {
		super.submitMinecartContents(state, blockModel, poseStack, collector, lightCoords);
		ManaPoolBlockEntityRenderer.cartMana = state.mana;
		Object dummyRenderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(DUMMY);
		if (dummyRenderer instanceof BotaniaBlockEntityRenderer<?> renderer) {
			renderer.renderWithoutLevel(poseStack, collector, lightCoords, OverlayTexture.NO_OVERLAY);
		}
	}
}
