package velrondevs.botania.client.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.common.block.block_entity.FloatingFlowerBlockEntity;
import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.xplat.BotaniaConfig;
import velrondevs.botania.xplat.ClientXplatAbstractions;

import java.util.Random;

public class FloatingFlowerBlockEntityRenderer implements BlockEntityRenderer<FloatingFlowerBlockEntity> {

	public FloatingFlowerBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(@NotNull FloatingFlowerBlockEntity tile, float t, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		renderFloatingIsland(tile, t, ms, buffers, overlay);
	}

	public static void renderFloatingIsland(BlockEntity tile, float t, PoseStack ms, MultiBufferSource buffers, int overlay) {
		if (BotaniaConfig.client().staticFloaters()) {
			return;
		}

		ms.pushPose();

		double worldTime = ClientTickHandler.ticksInGame + t;
		if (tile.getLevel() != null) {
			worldTime += new Random(tile.getBlockPos().hashCode()).nextInt(1000);
		}

		ms.translate(0.5F, 0, 0.5F);
		ms.mulPose(VecHelper.rotateY(-((float) worldTime * 0.5F)));
		ms.translate(-0.5, (float) Math.sin(worldTime * 0.05F) * 0.1F, 0.5);

		ms.mulPose(VecHelper.rotateX(4F * (float) Math.sin(worldTime * 0.04F)));
		ms.mulPose(VecHelper.rotateY(90.0F));

		ClientXplatAbstractions.INSTANCE.tessellateBlock(tile.getLevel(), tile.getBlockState(),
				tile.getBlockPos(), ms, buffers, overlay);

		ms.popPose();
	}

}
