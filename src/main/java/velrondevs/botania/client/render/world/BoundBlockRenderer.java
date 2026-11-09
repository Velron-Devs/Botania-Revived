package velrondevs.botania.client.render.world;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.item.WireframeCoordinateListProvider;
import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.xplat.BotaniaConfig;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.List;

public final class BoundBlockRenderer {
	private BoundBlockRenderer() {}

	public static void onWorldRenderLast(Vec3 camera, PoseStack ms, MultiBufferSource buffers, Level level) {
		Player player = Minecraft.getInstance().player;
		if (!BotaniaConfig.client().boundBlockWireframe()
				|| player == null
				|| player.level() != level) {
			return;
		}

		ms.pushPose();

		int color = 0xFF000000 | Mth.hsvToRgb(ClientTickHandler.ticksInGame % 200 / 200F, 0.6F, 1F);

		if (!player.getMainHandItem().isEmpty()) {
			var coordBoundItem = XplatAbstractions.INSTANCE.findCoordBoundItem(player.getMainHandItem());
			if (coordBoundItem != null) {
				BlockPos coords = coordBoundItem.getBinding(player.level());
				if (coords != null) {
					renderBlockOutlineAt(camera, ms, buffers, player.level(), coords, color);
				}
			}
		}

		if (!player.getOffhandItem().isEmpty()) {
			var coordBoundItem = XplatAbstractions.INSTANCE.findCoordBoundItem(player.getOffhandItem());
			if (coordBoundItem != null) {
				BlockPos coords = coordBoundItem.getBinding(player.level());
				if (coords != null) {
					renderBlockOutlineAt(camera, ms, buffers, player.level(), coords, color);
				}
			}
		}

		renderWireframeProviders(camera, player.getInventory(), player, ms, buffers, color);
		renderWireframeProviders(camera, BotaniaAPI.instance().getAccessoriesInventory(player), player, ms, buffers, color);

		ms.popPose();
	}

	private static void renderWireframeProviders(Vec3 camera, Container inv,
			Player player, PoseStack ms, MultiBufferSource buffers, int color) {
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stackInSlot = inv.getItem(i);

			if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof WireframeCoordinateListProvider provider) {
				List<BlockPos> coordsList = provider.getWireframesToDraw(player, stackInSlot);
				for (BlockPos coords : coordsList) {
					renderBlockOutlineAt(camera, ms, buffers, player.level(), coords, color);
				}

				BlockPos coords = provider.getSourceWireframe(player, stackInSlot);
				if (coords != null && coords.getY() != Integer.MIN_VALUE) {
					renderBlockOutlineAt(camera, ms, buffers, player.level(), coords, color, true);
				}
			}
		}
	}

	private static void renderBlockOutlineAt(Vec3 camera, PoseStack ms, MultiBufferSource buffers, Level level, BlockPos pos, int color) {
		renderBlockOutlineAt(camera, ms, buffers, level, pos, color, false);
	}

	private static void renderBlockOutlineAt(Vec3 camera, PoseStack ms, MultiBufferSource buffers, Level level, BlockPos pos, int color, boolean thick) {
		VoxelShape shape = level.getBlockState(pos).getShape(level, pos);

		if (!shape.isEmpty()) {
			ms.pushPose();
			ms.translate(pos.getX() - camera.x(), pos.getY() - camera.y(), pos.getZ() - camera.z());

			VertexConsumer buffer = buffers.getBuffer(thick ? RenderHelper.LINE_5_NO_DEPTH : RenderHelper.LINE_1_NO_DEPTH);
			ShapeRenderer.renderShape(ms, buffer, shape, 0, 0, 0, color, thick ? 5 : 1);

			buffer = buffers.getBuffer(thick ? RenderHelper.LINE_8_NO_DEPTH : RenderHelper.LINE_4_NO_DEPTH);
			int alpha = 64;
			color = (color & ~0xff000000) | (alpha << 24);
			ShapeRenderer.renderShape(ms, buffer, shape, 0, 0, 0, color, thick ? 8 : 4);

			ms.popPose();
		}
	}
}
