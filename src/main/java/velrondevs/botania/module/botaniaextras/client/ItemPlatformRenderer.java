package velrondevs.botania.module.botaniaextras.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.module.botaniaextras.block.ItemPlatformBlockEntity;

public class ItemPlatformRenderer implements BlockEntityRenderer<ItemPlatformBlockEntity> {
	public ItemPlatformRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(ItemPlatformBlockEntity platform, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		ItemStack stack = platform.getDisplayed();
		if (stack.isEmpty()) {
			return;
		}
		double time = ClientTickHandler.ticksInGame + partialTicks;
		ms.pushPose();
		ms.translate(0.5, 0.95 + 0.075 * Math.sin(time / 7.5), 0.5);
		ms.mulPose(VecHelper.rotateY((float) (time * 2) % 360F));
		ms.scale(1.2F, 1.2F, 1.2F);
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, ms, buffers, platform.getLevel(), 0);
		ms.popPose();
	}
}
