package velrondevs.botania.client.render.block_entity;

import velrondevs.botania.client.core.helper.RenderHelper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.common.block.block_entity.corporea.CorporeaCrystalCubeBlockEntity;
import velrondevs.botania.common.helper.VecHelper;

public class CorporeaCrystalCubeBlockEntityRenderer extends BotaniaBlockEntityRenderer<CorporeaCrystalCubeBlockEntity> {

	public static BlockStateModelPart cubeModel = null;

	public CorporeaCrystalCubeBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	protected void render(@Nullable CorporeaCrystalCubeBlockEntity cube, float f, PoseStack ms, MultiBufferSource buffers, SubmitNodeCollector collector, int light, int overlay) {
		ItemStack stack = ItemStack.EMPTY;
		if (cube != null) {
			stack = cube.getRequestTarget();
		}
		float age = ClientTickHandler.ticksInGame + f;

		Minecraft mc = Minecraft.getInstance();
		ms.pushPose();
		ms.translate(0.5F, 1.5F, 0.5F);
		ms.scale(1F, -1F, -1F);

		ms.translate(0F, (Mth.sin(age / 10.0F) * 0.1F + 0.1F) / -7F, 0F);

		if (!stack.isEmpty()) {
			ms.pushPose();
			ms.translate(0F, 0.96F, 0F);
			ms.scale(0.64F, 0.64F, 0.64F);
			ms.mulPose(VecHelper.rotateZ(180F));
			ms.translate(0F, 0.25F, 0F);
			ms.mulPose(VecHelper.rotateY(age * 9F));
			RenderHelper.renderItem(stack, ItemDisplayContext.GROUND, ms, collector, light, overlay, cube != null ? cube.getLevel() : null, 0);
			ms.popPose();
		}

		if (cubeModel != null) {
			ms.pushPose();
			ms.translate(-0.5F, 0.25F, -0.5F);
			RenderHelper.renderQuads(ms.last(), buffers.getBuffer(Sheets.translucentBlockSheet()), RenderHelper.allQuads(cubeModel), 0xFFFFFFFF, light, overlay);
			ms.popPose();
		}

		if (!stack.isEmpty() && cube != null && !cube.hideCount) {
			int count = cube.getItemCount();
			String countStr = String.valueOf(count);
			int color = 0xFFFFFF;
			if (count > 9_999) {
				countStr = count / 1_000 + "K";
				color = 0xFFFF00;
				if (count > 9_999_999) {
					countStr = count / 1_000_000 + "M";
					color = 0x00FF00;
				}
			}
			color |= 0xA0 << 24;
			int colorShade = (color & 16579836) >> 2 | color & -16777216;

			float s = 1F / 64F;
			ms.scale(s, s, s);
			int l = mc.font.width(countStr);

			ms.translate(0F, 55F, 0F);
			float tr = -16.5F;
			for (int i = 0; i < 4; i++) {
				ms.mulPose(VecHelper.rotateY(90F));
				ms.translate(0F, 0F, tr);
				mc.font.drawInBatch(countStr, -l / 2, 0, color, false, ms.last().pose(), buffers, Font.DisplayMode.NORMAL, 0, light);
				ms.translate(0F, 0F, 0.1F);
				mc.font.drawInBatch(countStr, -l / 2 + 1, 1, colorShade, false, ms.last().pose(), buffers, Font.DisplayMode.NORMAL, 0, light);
				ms.translate(0F, 0F, -tr - 0.1F);
			}
		}

		ms.popPose();
	}
}
