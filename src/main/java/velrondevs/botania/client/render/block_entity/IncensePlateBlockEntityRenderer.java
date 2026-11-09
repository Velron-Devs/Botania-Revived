package velrondevs.botania.client.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.common.block.block_entity.IncensePlateBlockEntity;
import velrondevs.botania.common.helper.VecHelper;

public class IncensePlateBlockEntityRenderer extends BotaniaBlockEntityRenderer<IncensePlateBlockEntity> {
	public IncensePlateBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	protected void render(@NotNull IncensePlateBlockEntity plate, float ticks, PoseStack ms, MultiBufferSource buffers, SubmitNodeCollector collector, int light, int overlay) {
		ItemStack stack = plate.getItemHandler().getItem(0);
		if (stack.isEmpty()) {
			return;
		}

		Direction facing = plate.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

		ms.pushPose();
		ms.translate(0.5F, 1.5F, 0.5F);
		int degrees = switch (facing) {
			default -> 0;
			case WEST -> 90;
			case SOUTH -> 180;
			case EAST -> 270;
		};
		ms.mulPose(VecHelper.rotateY(degrees));
		float s = 0.6F;
		ms.translate(-0.11F, -1.35F, 0F);
		ms.scale(s, s, s);
		RenderHelper.renderItem(stack, ItemDisplayContext.GROUND, ms, collector, light, overlay, plate.getLevel(), 0);
		ms.popPose();
	}

}
