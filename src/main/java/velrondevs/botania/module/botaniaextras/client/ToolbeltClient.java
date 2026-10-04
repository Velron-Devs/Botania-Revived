package velrondevs.botania.module.botaniaextras.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import org.joml.Matrix4f;

import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.module.botaniaextras.item.ToolbeltItem;
import velrondevs.botania.module.botaniaextras.network.ToolbeltClickPacket;
import velrondevs.botania.xplat.ClientXplatAbstractions;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class ToolbeltClient {
	private static final ResourceLocation GLOW = prefix("textures/misc/botania_extras/toolbelt.png");
	private static final float SEGMENT_ANGLE = 360F / ToolbeltItem.SEGMENTS;
	private static final double OUTER = 3.0;
	private static final double INNER = 2.4;

	private static float rotationBase;
	private static boolean active;

	private ToolbeltClient() {}

	public static void init() {
		NeoForge.EVENT_BUS.addListener(ToolbeltClient::onClientTick);
		NeoForge.EVENT_BUS.addListener(ToolbeltClient::onRightClickItem);
		NeoForge.EVENT_BUS.addListener(ToolbeltClient::onRightClickEmpty);
		NeoForge.EVENT_BUS.addListener(ToolbeltClient::onRenderLevel);
	}

	private static boolean lookingAtWheel(Player player) {
		float pitch = player.getXRot();
		return pitch > -33.75F && pitch < 45F;
	}

	private static int segmentLookedAt(Player player) {
		float relative = Mth.wrapDegrees(player.getYRot() - rotationBase);
		return Math.floorMod((int) Math.floor((relative + SEGMENT_ANGLE / 2F) / SEGMENT_ANGLE), ToolbeltItem.SEGMENTS);
	}

	private static void onClientTick(ClientTickEvent.Post e) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		active = false;
		if (player == null || mc.level == null || ToolbeltItem.findBelt(player).isEmpty()) {
			return;
		}
		if (player.isShiftKeyDown()) {
			active = lookingAtWheel(player);
		} else {
			rotationBase = player.getYRot();
		}
	}

	private static boolean click(Player player, InteractionHand hand) {
		if (!active || hand != InteractionHand.MAIN_HAND || player != Minecraft.getInstance().player) {
			return false;
		}
		ItemStack belt = ToolbeltItem.findBelt(player);
		if (belt.isEmpty()) {
			return false;
		}
		int segment = segmentLookedAt(player);
		if (ToolbeltItem.getItem(belt, segment).isEmpty() && !ToolbeltItem.allowed(player.getMainHandItem())) {
			return false;
		}
		ClientXplatAbstractions.INSTANCE.sendToServer(new ToolbeltClickPacket(segment));
		return true;
	}

	private static void onRightClickItem(PlayerInteractEvent.RightClickItem e) {
		if (e.getLevel().isClientSide() && click(e.getEntity(), e.getHand())) {
			e.setCanceled(true);
			e.setCancellationResult(InteractionResult.SUCCESS);
		}
	}

	private static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty e) {
		if (e.getLevel().isClientSide()) {
			click(e.getEntity(), e.getHand());
		}
	}

	private static void vertex(BufferBuilder buffer, Matrix4f pose, double x, double y, double z, float u, float v, float shade, float alpha) {
		buffer.addVertex(pose, (float) x, (float) y, (float) z).setUv(u, v).setColor(shade, shade, shade, alpha);
	}

	private static void onRenderLevel(RenderLevelStageEvent e) {
		if (e.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES || !active) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null || mc.level == null) {
			return;
		}
		ItemStack belt = ToolbeltItem.findBelt(player);
		if (belt.isEmpty()) {
			return;
		}
		float partial = e.getPartialTick().getGameTimeDeltaPartialTick(false);
		Vec3 eye = player.getEyePosition(partial).subtract(e.getCamera().getPosition());
		int hot = segmentLookedAt(player);
		float alpha = (Mth.sin((ClientTickHandler.total()) * 0.2F) * 0.5F + 0.5F) * 0.4F + 0.3F;
		PoseStack ms = e.getPoseStack();
		Matrix4f pose = ms.last().pose();

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableCull();
		RenderSystem.depthMask(false);
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		RenderSystem.setShaderTexture(0, GLOW);
		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		for (int seg = 0; seg < ToolbeltItem.SEGMENTS; seg++) {
			boolean selected = seg == hot;
			float a = selected ? alpha + 0.3F : alpha;
			float shade = seg % 2 == 0 ? 0.6F : 1F;
			double outerY = eye.y + (selected ? 1.5 : 0);
			double innerY = eye.y - 1.5;
			float start = rotationBase + seg * SEGMENT_ANGLE - SEGMENT_ANGLE / 2F;
			for (int j = 0; j < SEGMENT_ANGLE; j++) {
				double yaw1 = Math.toRadians(start + j);
				double yaw2 = Math.toRadians(start + j + 1);
				double x1 = -Math.sin(yaw1);
				double z1 = Math.cos(yaw1);
				double x2 = -Math.sin(yaw2);
				double z2 = Math.cos(yaw2);
				vertex(buffer, pose, eye.x + x1 * OUTER, outerY, eye.z + z1 * OUTER, 1F, 0F, shade, a);
				vertex(buffer, pose, eye.x + x1 * INNER, innerY, eye.z + z1 * INNER, 1F, 0.25F, shade, a);
				vertex(buffer, pose, eye.x + x2 * INNER, innerY, eye.z + z2 * INNER, 0F, 0.25F, shade, a);
				vertex(buffer, pose, eye.x + x2 * OUTER, outerY, eye.z + z2 * OUTER, 0F, 0F, shade, a);
			}
		}
		BufferUploader.drawWithShader(buffer.buildOrThrow());
		RenderSystem.depthMask(true);
		RenderSystem.enableCull();
		RenderSystem.disableBlend();

		MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
		for (int seg = 0; seg < ToolbeltItem.SEGMENTS; seg++) {
			ItemStack stack = ToolbeltItem.getItem(belt, seg);
			if (stack.isEmpty()) {
				continue;
			}
			float yaw = rotationBase + seg * SEGMENT_ANGLE;
			double rad = Math.toRadians(yaw);
			ms.pushPose();
			ms.translate(eye.x - Math.sin(rad) * INNER, eye.y - 0.75, eye.z + Math.cos(rad) * INNER);
			ms.mulPose(Axis.YP.rotationDegrees(-yaw));
			ms.scale(1.25F, 1.25F, 1.25F);
			mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, 0xF000F0, OverlayTexture.NO_OVERLAY, ms, buffers, mc.level, seg);
			ms.popPose();
		}
		buffers.endBatch();
	}
}
