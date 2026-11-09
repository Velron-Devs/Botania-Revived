package velrondevs.botania.client.render.block_entity;

import velrondevs.botania.client.core.helper.RenderHelper;

import net.minecraft.client.renderer.rendertype.RenderTypes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.block.block_entity.CocoonBlockEntity;
import velrondevs.botania.common.helper.VecHelper;

public class CocoonBlockEntityRenderer extends BotaniaBlockEntityRenderer<CocoonBlockEntity> {
	public CocoonBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	protected void render(@NotNull CocoonBlockEntity cocoon, float partialTicks, PoseStack ms, MultiBufferSource buffers, SubmitNodeCollector collector, int light, int overlay) {
		float rot = 0F;
		float modval = 60F - (float) cocoon.timePassed / (float) CocoonBlockEntity.TOTAL_TIME * 30F;
		if (cocoon.timePassed % modval < 10) {
			float mod = (cocoon.timePassed + partialTicks) % modval;
			float v = mod / 5 * (float) Math.PI * 2;
			rot = (float) Math.sin(v) * (float) Math.log(cocoon.timePassed + partialTicks);
		}

		ms.pushPose();
		ms.translate(0.5, 0, 0);
		ms.mulPose(VecHelper.rotateX(rot));
		ms.translate(-0.5, 0, 0);
		BlockState state = cocoon.getBlockState();
		RenderHelper.renderBlockState(state, ms, buffers, Sheets.cutoutBlockSheet(), 0xFFFFFFFF, light, overlay);
		ms.popPose();
	}
}
