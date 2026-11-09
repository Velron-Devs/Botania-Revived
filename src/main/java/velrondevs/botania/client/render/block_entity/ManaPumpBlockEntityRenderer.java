package velrondevs.botania.client.render.block_entity;

import velrondevs.botania.client.core.helper.RenderHelper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import velrondevs.botania.common.block.block_entity.mana.ManaPumpBlockEntity;
import velrondevs.botania.common.helper.VecHelper;

public class ManaPumpBlockEntityRenderer extends BotaniaBlockEntityRenderer<ManaPumpBlockEntity> {
	public static BlockStateModelPart headModel = null;

	public ManaPumpBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	protected void render(ManaPumpBlockEntity pump, float partialTicks, PoseStack ms, MultiBufferSource buffers, SubmitNodeCollector collector, int light, int overlay) {
		ms.pushPose();
		ms.translate(0.5, 0, 0.5);
		float angle = 0;
		switch (pump.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)) {
			default:
			case NORTH:
				break;
			case SOUTH:
				angle = 180;
				break;
			case EAST:
				angle = -90;
				break;
			case WEST:
				angle = 90;
				break;
		}
		ms.mulPose(VecHelper.rotateY(angle));
		ms.translate(-0.5, 0, -0.5);
		double diff = Math.max(0F, Math.min(8F, pump.innerRingPos + pump.moving * partialTicks));
		ms.translate(0, 0, diff / 14);
		if (headModel != null) {
			RenderHelper.renderModelPart(headModel, ms, buffers, light, overlay);
		}
		ms.popPose();
	}
}
