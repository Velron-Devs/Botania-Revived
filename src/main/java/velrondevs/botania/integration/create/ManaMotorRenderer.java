package velrondevs.botania.integration.create;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ManaMotorRenderer extends KineticBlockEntityRenderer<ManaMotorBlockEntity> {
	public ManaMotorRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected BlockState getRenderedBlockState(ManaMotorBlockEntity be) {
		return shaft(getRotationAxisOf(be));
	}

	@Override
	public void submit(BlockEntityRenderState state, PoseStack ms, SubmitNodeCollector collector, CameraRenderState camera) {
		if (!(state instanceof KineticRenderState kinetic) || !(kinetic.blockEntity instanceof ManaMotorBlockEntity be)) {
			return;
		}
		if (isInvalid(be)) {
			return;
		}
		List<BlockStateModelPart> parts = getRotatingModelParts(be, getRenderedBlockState(be));
		if (parts.isEmpty()) {
			return;
		}
		Direction facing = be.getBlockState().getValue(ManaMotorBlock.FACING);
		Direction.Axis axis = getRotationAxisOf(be);
		ms.pushPose();
		ms.translate(0.5, 0.5, 0.5);
		ms.mulPose(rotation(axis, getAngleForBe(be, be.getBlockPos(), axis, kinetic.partialTicks)));
		ms.translate(facing.getStepX() * 0.25, facing.getStepY() * 0.25, facing.getStepZ() * 0.25);
		switch (facing.getAxis()) {
			case X -> ms.scale(0.5f, 1, 1);
			case Y -> ms.scale(1, 0.5f, 1);
			case Z -> ms.scale(1, 1, 0.5f);
		}
		ms.translate(-0.5, -0.5, -0.5);
		collector.submitBlockModel(ms, getRotatingRenderType(parts), parts, BlockModelRenderState.EMPTY_TINTS, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		ms.popPose();
	}
}
