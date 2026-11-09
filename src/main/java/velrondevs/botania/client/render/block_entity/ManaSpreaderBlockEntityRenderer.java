package velrondevs.botania.client.render.block_entity;

import net.minecraft.client.renderer.rendertype.RenderTypes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.api.state.BotaniaStateProperties;
import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.client.core.handler.MiscellaneousModels;
import velrondevs.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import velrondevs.botania.common.block.mana.ManaSpreaderBlock;
import velrondevs.botania.common.helper.VecHelper;

import java.util.Random;

public class ManaSpreaderBlockEntityRenderer extends BotaniaBlockEntityRenderer<ManaSpreaderBlockEntity> {

	public ManaSpreaderBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	protected void render(@NotNull ManaSpreaderBlockEntity spreader, float partialTicks, PoseStack ms, MultiBufferSource buffers, SubmitNodeCollector collector, int light, int overlay) {
		ms.pushPose();

		ms.translate(0.5F, 0.5, 0.5F);

		Quaternionf transform = VecHelper.rotateY(spreader.rotationX + 90F);
		transform.mul(VecHelper.rotateX(spreader.rotationY));
		ms.mulPose(transform);

		ms.translate(-0.5F, -0.5F, -0.5F);

		double time = ClientTickHandler.ticksInGame + partialTicks;

		float r = 1, g = 1, b = 1;
		if (spreader.getVariant() == ManaSpreaderBlock.Variant.GAIA) {
			int color = Mth.hsvToRgb((float) ((time * 2 + new Random(spreader.getBlockPos().hashCode()).nextInt(10000)) % 360) / 360F, 0.4F, 0.9F);
			r = (color >> 16 & 0xFF) / 255F;
			g = (color >> 8 & 0xFF) / 255F;
			b = (color & 0xFF) / 255F;
		}

		int color = ARGB.colorFromFloat(1F, r, g, b);
		RenderHelper.renderBlockState(spreader.getBlockState(), ms, buffers, null, color, light, overlay);

		ms.pushPose();
		ms.translate(0.5, 0.5, 0.5);
		ms.mulPose(VecHelper.rotateY((float) time % 360));
		ms.translate(-0.5, -0.5, -0.5);
		ms.translate(0F, (float) Math.sin(time / 20.0) * 0.05F, 0F);
		BlockStateModelPart core = getCoreModel(spreader);
		RenderHelper.renderModelPart(core, ms, buffers, light, overlay);
		ms.popPose();

		ItemStack stack = spreader.getItemHandler().getItem(0);
		if (!stack.isEmpty()) {
			ms.pushPose();
			ms.translate(0.5F, 0.5F, 0.094F);
			ms.mulPose(VecHelper.rotateZ(180));
			ms.mulPose(VecHelper.rotateX(180));

			ms.scale(0.997F, 0.997F, 1F);
			RenderHelper.renderItem(stack, ItemDisplayContext.NONE, ms, collector, light, overlay, spreader.getLevel(), 0);
			ms.popPose();
		}

		if (spreader.paddingColor != null) {
			ms.pushPose();

			ms.translate(0.5F, 0.5F, 0.5F);
			ms.mulPose(VecHelper.rotateX(-90));
			ms.mulPose(VecHelper.rotateY(180));
			ms.translate(-0.5F, -0.5F, -0.5F);
			BlockStateModelPart paddingModel = getPaddingModel(spreader.paddingColor);
			RenderHelper.renderModelPart(paddingModel, color, ms, buffers, light, overlay);
			ms.popPose();
		}

		ms.popPose();

		if (spreader.getBlockState().getValue(BotaniaStateProperties.HAS_SCAFFOLDING)) {
			BlockStateModelPart scaffolding = getScaffoldingModel(spreader);
			RenderHelper.renderModelPart(scaffolding, color, ms, buffers, light, overlay);
		}

	}

	private BlockStateModelPart getCoreModel(ManaSpreaderBlockEntity tile) {
		return switch (tile.getVariant()) {
			case GAIA -> MiscellaneousModels.INSTANCE.gaiaSpreaderCore;
			case REDSTONE -> MiscellaneousModels.INSTANCE.redstoneSpreaderCore;
			case ELVEN -> MiscellaneousModels.INSTANCE.elvenSpreaderCore;
			case MANA -> MiscellaneousModels.INSTANCE.manaSpreaderCore;
		};
	}

	private BlockStateModelPart getPaddingModel(DyeColor color) {
		return MiscellaneousModels.INSTANCE.spreaderPaddings.get(color);
	}

	private BlockStateModelPart getScaffoldingModel(ManaSpreaderBlockEntity tile) {
		return switch (tile.getVariant()) {
			case MANA, REDSTONE -> MiscellaneousModels.INSTANCE.manaSpreaderScaffolding;
			case ELVEN -> MiscellaneousModels.INSTANCE.elvenSpreaderScaffolding;
			case GAIA -> MiscellaneousModels.INSTANCE.gaiaSpreaderScaffolding;
		};
	}
}
