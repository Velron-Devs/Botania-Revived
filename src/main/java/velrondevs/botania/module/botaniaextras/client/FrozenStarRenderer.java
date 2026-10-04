package velrondevs.botania.module.botaniaextras.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.module.botaniaextras.block.FrozenStarBlockEntity;
import velrondevs.botania.module.botaniaextras.item.FrozenStarItem;

import java.util.Random;

public class FrozenStarRenderer implements BlockEntityRenderer<FrozenStarBlockEntity> {
	public FrozenStarRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(FrozenStarBlockEntity star, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		BlockPos pos = star.getBlockPos();
		long seed = pos.getX() ^ pos.getY() ^ pos.getZ();
		int color = star.getColor();
		if (color == FrozenStarItem.RAINBOW) {
			float time = ClientTickHandler.ticksInGame + partialTicks + new Random(seed).nextInt(100000);
			color = Mth.hsvToRgb((time * 0.005F) % 1F, 1F, 1F);
		}
		float size = star.getSize();
		ms.pushPose();
		ms.translate(0.5, 0.5, 0.5);
		RenderHelper.renderStar(ms, buffers, color & 0xFFFFFF, size, size, size, seed);
		ms.popPose();
	}
}
