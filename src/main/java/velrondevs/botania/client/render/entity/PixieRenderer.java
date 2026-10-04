package velrondevs.botania.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.client.core.helper.CoreShaders;
import velrondevs.botania.client.core.proxy.ClientProxy;
import velrondevs.botania.client.lib.ResourcesLib;
import velrondevs.botania.client.model.BotaniaModelLayers;
import velrondevs.botania.client.model.PixieModel;
import velrondevs.botania.common.entity.PixieEntity;

public class PixieRenderer extends MobRenderer<PixieEntity, PixieModel> {

	public PixieRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new PixieModel(ctx.bakeLayer(BotaniaModelLayers.PIXIE)), 0.0F);
	}

	@Override
	public void render(PixieEntity mob, float yaw, float partialTicks, PoseStack pos, MultiBufferSource buffers, int light) {
		ShaderInstance shader = CoreShaders.doppleganger();
		if (shader != null) {
			shader.safeGetUniform("BotaniaDisfiguration").set(GaiaGuardianRenderer.DEFAULT_DISFIGURATION);
			shader.safeGetUniform("BotaniaGrainIntensity").set(GaiaGuardianRenderer.DEFAULT_GRAIN_INTENSITY);
		}
		super.render(mob, yaw, partialTicks, pos, buffers, light);
	}

	@NotNull
	@Override
	public ResourceLocation getTextureLocation(@NotNull PixieEntity entity) {
		return ClientProxy.dootDoot
				? ResourceLocation.parse(ResourcesLib.MODEL_PIXIE_HALLOWEEN)
				: ResourceLocation.parse(ResourcesLib.MODEL_PIXIE);
	}
}
