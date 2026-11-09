package velrondevs.botania.client.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.client.model.BotaniaModelLayers;
import velrondevs.botania.client.model.BotanicalBreweryModel;
import velrondevs.botania.common.block.block_entity.BreweryBlockEntity;

public class BotanicalBreweryBlockEntityRenderer extends BotaniaBlockEntityRenderer<BreweryBlockEntity> {
	final BotanicalBreweryModel model;

	public BotanicalBreweryBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		model = new BotanicalBreweryModel(ctx.bakeLayer(BotaniaModelLayers.BREWERY));
	}

	@Override
	protected void render(@Nullable BreweryBlockEntity brewery, float f, PoseStack ms, MultiBufferSource buffers, SubmitNodeCollector collector, int light, int overlay) {
		ms.pushPose();

		ms.scale(1F, -1F, -1F);
		ms.translate(0.5F, -1.5F, -0.5F);

		double time = ClientTickHandler.ticksInGame + f;

		model.render(brewery, time, ms, buffers, collector, light, overlay);
		ms.popPose();
	}

}
