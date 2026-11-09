package velrondevs.botania.client.render.world;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.common.item.AstrolabeItem;

import java.util.List;

public final class AstrolabePreviewHandler {
	public static void onWorldRenderLast(Vec3 camera, PoseStack ms, MultiBufferSource buffers, Level level) {
		for (Player player : level.players()) {
			ItemStack currentStack = player.getMainHandItem();
			InteractionHand hand = InteractionHand.MAIN_HAND;
			if (currentStack.isEmpty() || !(currentStack.getItem() instanceof AstrolabeItem)) {
				currentStack = player.getOffhandItem();
				hand = InteractionHand.OFF_HAND;
			}

			if (!currentStack.isEmpty() && currentStack.getItem() instanceof AstrolabeItem) {
				Block block = AstrolabeItem.getBlock(currentStack, level.holderLookup(Registries.BLOCK));
				if (block != Blocks.AIR) {
					renderPlayerLook(camera, ms, buffers, player, currentStack, hand);
				}
			}
		}
	}

	private static void renderPlayerLook(Vec3 camera, PoseStack ms, MultiBufferSource buffers, Player player, ItemStack stack, InteractionHand hand) {
		Block blockToPlace = AstrolabeItem.getBlock(stack, player.level().holderLookup(Registries.BLOCK));
		int size = AstrolabeItem.getSize(stack);
		BlockPlaceContext ctx = AstrolabeItem.getBlockPlaceContext(player, hand, blockToPlace);
		List<BlockPos> placePositions = AstrolabeItem.getPlacePositions(ctx, size);
		if (ctx != null && AstrolabeItem.hasBlocks(stack, player, placePositions.size(), blockToPlace)) {
			for (BlockPos pos : placePositions) {
				BlockPlaceContext placeContext = getPlaceContext(player, ctx, pos);
				BlockState state = blockToPlace.getStateForPlacement(placeContext);
				if (state != null && placeContext.canPlace() && state.canSurvive(player.level(), pos)) {
					renderBlockAt(camera, ms, buffers, state, pos);
				}
			}
		}
	}

	@NotNull
	private static BlockPlaceContext getPlaceContext(Player player, BlockPlaceContext ctx, BlockPos pos) {
		Vec3 newHitVec = new Vec3(pos.getX() + Mth.frac(ctx.getClickLocation().x()),
				pos.getY() + Mth.frac(ctx.getClickLocation().y()),
				pos.getZ() + Mth.frac(ctx.getClickLocation().z()));
		BlockHitResult newHit = new BlockHitResult(newHitVec, ctx.getClickedFace(), pos, false);
		return new BlockPlaceContext(player, ctx.getHand(), ctx.getItemInHand(), newHit);
	}

	private static void renderBlockAt(Vec3 camera, PoseStack ms, MultiBufferSource buffers, BlockState state, BlockPos pos) {
		ms.pushPose();
		ms.translate(pos.getX() - camera.x(), pos.getY() - camera.y(), pos.getZ() - camera.z());
		RenderHelper.renderBlockState(state, ms, buffers, RenderHelper.ASTROLABE_PREVIEW, 0x66FFFFFF, 0xF000F0, OverlayTexture.NO_OVERLAY);
		ms.popPose();
	}

	private AstrolabePreviewHandler() {}
}
