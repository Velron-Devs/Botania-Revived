package velrondevs.botania.module.botaniaextras.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.module.botaniaextras.item.ToolbeltItem;
import velrondevs.botania.module.botaniaextras.network.ToolbeltClickPacket;
import velrondevs.botania.xplat.ClientXplatAbstractions;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class ToolbeltClient {
	private static final Identifier GLOW = prefix("textures/misc/botania_extras/toolbelt.png");
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

	private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, double x, double y, double z, float u, float v, float shade, float alpha) {
		buffer.addVertex(pose, (float) x, (float) y, (float) z).setColor(shade, shade, shade, alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(pose, 0F, 1F, 0F);
	}

	private static void onRenderLevel(SubmitCustomGeometryEvent e) {
		if (!active) {
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
		float partial = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);
		Vec3 eye = player.getEyePosition(partial).subtract(e.getLevelRenderState().cameraRenderState.pos);
		int hot = segmentLookedAt(player);
		float alpha = (Mth.sin((ClientTickHandler.total()) * 0.2F) * 0.5F + 0.5F) * 0.4F + 0.3F;
		PoseStack ms = e.getPoseStack();
		SubmitNodeCollector collector = e.getSubmitNodeCollector();
		collector.submitCustomGeometry(ms, RenderTypes.entityTranslucentEmissive(GLOW), (pose, buffer) -> {
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
		});

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
			RenderHelper.renderItem(stack, ItemDisplayContext.GROUND, ms, collector, 0xF000F0, OverlayTexture.NO_OVERLAY, mc.level, seg);
			ms.popPose();
		}
	}
}
