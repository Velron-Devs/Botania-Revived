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
import velrondevs.botania.api.mana.BasicLensItem;
import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.common.block.block_entity.mana.ManaPrismBlockEntity;
import velrondevs.botania.common.helper.VecHelper;

public class ManaPrismBlockEntityRenderer extends BotaniaBlockEntityRenderer<ManaPrismBlockEntity> {

	public ManaPrismBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	protected void render(@NotNull ManaPrismBlockEntity prism, float partTicks, PoseStack ms, MultiBufferSource buffers, SubmitNodeCollector collector, int light, int overlay) {
		float pos = (float) Math.sin((ClientTickHandler.ticksInGame + partTicks) * 0.05F) * 0.5F * (1F - 1F / 16F) * 0.997F - 0.5F;

		ItemStack stack = prism.getItemHandler().getItem(0);

		if (!stack.isEmpty()) {
			if (stack.getItem() instanceof BasicLensItem) {
				ms.pushPose();
				ms.mulPose(VecHelper.rotateX(90));
				ms.translate(0.5F, 0.5F, pos);
				ms.scale(1.003F, 1.003F, 1F);
				RenderHelper.renderItem(stack, ItemDisplayContext.NONE, ms, collector, light, overlay, prism.getLevel(), 0);
				ms.popPose();
			}
		}
	}

}
