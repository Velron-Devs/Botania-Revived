package velrondevs.botania.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;

import org.jspecify.annotations.Nullable;

import velrondevs.botania.client.core.helper.DeferredBufferSource;

public abstract class BotaniaEntityRenderer<T extends Entity> extends EntityRenderer<T, BotaniaEntityRenderer.State<T>> {
	public static class State<T extends Entity> extends EntityRenderState {
		public @Nullable T entity;
		public float yaw;
	}

	protected BotaniaEntityRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public State<T> createRenderState() {
		return new State<>();
	}

	@Override
	public void extractRenderState(T entity, State<T> state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.entity = entity;
		state.yaw = entity.getYRot(partialTicks);
	}

	@Override
	public void submit(State<T> state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, poseStack, collector, camera);
		if (state.entity != null) {
			render(state.entity, state.yaw, state.partialTick, poseStack, new DeferredBufferSource(collector), collector, state.lightCoords);
		}
	}

	protected abstract void render(T entity, float yaw, float partialTicks, PoseStack ms, MultiBufferSource buffers, SubmitNodeCollector collector, int light);
}
