package velrondevs.botania.client.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.client.lib.ResourcesLib;
import velrondevs.botania.client.model.BellowsModel;
import velrondevs.botania.client.model.BotaniaModelLayers;
import velrondevs.botania.common.block.block_entity.mana.BellowsBlockEntity;
import velrondevs.botania.common.helper.VecHelper;

public class BellowsBlockEntityRenderer implements BlockEntityRenderer<BellowsBlockEntity> {
	private static final ResourceLocation texture = ResourceLocation.parse(ResourcesLib.MODEL_BELLOWS);
	private final BellowsModel model;

	public BellowsBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		model = new BellowsModel(ctx.bakeLayer(BotaniaModelLayers.BELLOWS));
	}

	@Override
	public void render(@Nullable BellowsBlockEntity bellows, float f, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ms.pushPose();
		ms.translate(0.5F, 1.5F, 0.5F);
		ms.scale(1F, -1F, -1F);
		float angle = 0;
		if (bellows != null) {
			switch (bellows.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)) {
				case SOUTH:
					break;
				case NORTH:
					angle = 180F;
					break;
				case EAST:
					angle = 270F;
					break;
				case WEST:
					angle = 90F;
					break;
			}
		}
		ms.mulPose(VecHelper.rotateY(angle));
		float fract = Math.max(0.1F, 1F - (bellows == null ? 0 : bellows.movePos + bellows.moving * f + 0.1F));
		VertexConsumer buffer = buffers.getBuffer(model.renderType(texture));
		model.render(ms, buffer, light, overlay, -1, fract);
		ms.popPose();
	}

}
