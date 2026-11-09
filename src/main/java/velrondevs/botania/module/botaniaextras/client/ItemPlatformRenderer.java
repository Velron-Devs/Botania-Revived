package velrondevs.botania.module.botaniaextras.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.client.render.block_entity.BotaniaBlockEntityRenderer;
import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.module.botaniaextras.block.ItemPlatformBlockEntity;

public class ItemPlatformRenderer extends BotaniaBlockEntityRenderer<ItemPlatformBlockEntity> {
	public ItemPlatformRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	protected void render(@Nullable ItemPlatformBlockEntity platform, float partialTicks, PoseStack ms, MultiBufferSource buffers, SubmitNodeCollector collector, int light, int overlay) {
		if (platform == null) {
			return;
		}
		ItemStack stack = platform.getDisplayed();
		if (stack.isEmpty()) {
			return;
		}
		double time = ClientTickHandler.ticksInGame + partialTicks;
		ms.pushPose();
		ms.translate(0.5, 0.95 + 0.075 * Math.sin(time / 7.5), 0.5);
		ms.mulPose(VecHelper.rotateY((float) (time * 2) % 360F));
		ms.scale(1.2F, 1.2F, 1.2F);
		RenderHelper.renderItem(stack, ItemDisplayContext.GROUND, ms, collector, light, overlay, platform.getLevel(), 0);
		ms.popPose();
	}
}
