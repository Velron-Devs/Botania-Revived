package velrondevs.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.client.core.handler.MiscellaneousModels;
import velrondevs.botania.client.render.AccessoryRenderRegistry;
import velrondevs.botania.client.render.AccessoryRenderer;
import velrondevs.botania.common.proxy.Proxy;
import velrondevs.botania.network.serverbound.JumpPacket;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.xplat.ClientXplatAbstractions;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class CirrusAmuletItem extends BaubleItem {

	private static final Set<Player> JUMPING_PLAYERS = Collections.newSetFromMap(new WeakHashMap<>());

	private static int timesJumped;
	private static boolean jumpDown;

	public CirrusAmuletItem(Properties props) {
		super(props);
		Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		Proxy.INSTANCE.runOnClient(() -> () -> {
			if (living == Minecraft.getInstance().player) {
				LocalPlayer playerSp = (LocalPlayer) living;

				if (playerSp.onGround()) {
					timesJumped = 0;
				} else {
					if (timesJumped == 0) {

						timesJumped = 1;
						jumpDown = true;
					}
					if (playerSp.input.jumping) {
						if (!jumpDown && timesJumped < ((CirrusAmuletItem) stack.getItem()).getMaxAllowedJumps()) {
							playerSp.jumpFromGround();
							ClientXplatAbstractions.INSTANCE.sendToServer(JumpPacket.INSTANCE);
							timesJumped++;
						}
						jumpDown = true;
					} else {
						jumpDown = false;
					}
				}
			}
		});
	}

	public static void setJumping(Player entity) {
		JUMPING_PLAYERS.add(entity);
	}

	public static boolean popJumping(Player entity) {
		if (entity.level().isClientSide) {
			return timesJumped > 0;
		}
		return JUMPING_PLAYERS.remove(entity);
	}

	public static class Renderer implements AccessoryRenderer {
		@Override
		public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			boolean armor = !living.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
			bipedModel.body.translateAndRotate(ms);
			ms.translate(-0.3, 0.4, armor ? 0.05 : 0.12);
			ms.scale(0.5F, -0.5F, -0.5F);

			BakedModel model = stack.is(BotaniaItems.superCloudPendant)
					? MiscellaneousModels.INSTANCE.nimbusGem
					: MiscellaneousModels.INSTANCE.cirrusGem;
			VertexConsumer buffer = buffers.getBuffer(Sheets.cutoutBlockSheet());
			Minecraft.getInstance().getBlockRenderer().getModelRenderer()
					.renderModel(ms.last(), buffer, null, model, 1, 1, 1, light, OverlayTexture.NO_OVERLAY);
		}
	}

	public int getMaxAllowedJumps() {
		return 2;
	}

}
