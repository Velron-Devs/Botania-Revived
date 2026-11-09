package velrondevs.botania.client.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.common.block.block_entity.SparkTinkererBlockEntity;
import velrondevs.botania.common.helper.VecHelper;

public class SparkTinkererBlockEntityRenderer extends BotaniaBlockEntityRenderer<SparkTinkererBlockEntity> {

	public SparkTinkererBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	protected void render(@NotNull SparkTinkererBlockEntity tileentity, float pticks, PoseStack ms, MultiBufferSource buffers, SubmitNodeCollector collector, int light, int overlay) {
		ms.pushPose();
		ms.mulPose(VecHelper.rotateX(90));
		ms.translate(1.0F, -0.125F, -0.25F);
		ItemStack stack = tileentity.getItemHandler().getItem(0);
		if (!stack.isEmpty()) {
			ms.mulPose(VecHelper.rotateY(180));
			ms.translate(0.5F, 0.5F, 0);
			RenderHelper.renderItem(stack, ItemDisplayContext.GROUND, ms, collector, light, overlay, tileentity.getLevel(), 0);
		}
		ms.popPose();
	}

}
