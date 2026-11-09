package velrondevs.botania.client.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

import velrondevs.botania.client.core.helper.DeferredBufferSource;

public abstract class BotaniaBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T, BotaniaBlockEntityRenderer.State<T>> {
	public static class State<T extends BlockEntity> extends BlockEntityRenderState {
		public @Nullable T blockEntity;
		public float partialTicks;
	}

	@Override
	public State<T> createRenderState() {
		return new State<>();
	}

	@Override
	public void extractRenderState(T blockEntity, State<T> state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.blockEntity = blockEntity;
		state.partialTicks = partialTicks;
	}

	@Override
	public void submit(State<T> state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		if (state.blockEntity == null || state.blockEntity.isRemoved()) {
			return;
		}
		DeferredBufferSource buffers = new DeferredBufferSource(collector);
		render(state.blockEntity, state.partialTicks, poseStack, buffers, collector, state.lightCoords, OverlayTexture.NO_OVERLAY);
	}

	public void renderWithoutLevel(PoseStack poseStack, SubmitNodeCollector collector, int light, int overlay) {
		render(null, 0, poseStack, new DeferredBufferSource(collector), collector, light, overlay);
	}

	protected abstract void render(@Nullable T blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffers, SubmitNodeCollector collector, int light, int overlay);
}
