package velrondevs.botania.integration.curios;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import velrondevs.botania.client.render.AccessoryRenderRegistry;
import velrondevs.botania.client.render.AccessoryRenderer;
import velrondevs.botania.common.item.equipment.bauble.BaubleItem;

public final class CurioRenderer implements ICurioRenderer {
	private static final CurioRenderer INSTANCE = new CurioRenderer();

	private CurioRenderer() {}

	public static void register(Item item) {
		CuriosRendererRegistry.register(item, () -> INSTANCE);
	}

	@Override
	public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(ItemStack stack, SlotContext slotContext,
			PoseStack poseStack, SubmitNodeCollector collector, int light, S renderState, RenderLayerParent<S, M> renderLayerParent,
			EntityRendererProvider.Context context, float yRot, float xRot) {
		LivingEntity living = slotContext.entity();
		if (living == null || !(stack.getItem() instanceof BaubleItem item) || !item.hasRender(stack, living)) {
			return;
		}
		if (!(renderLayerParent.getModel() instanceof HumanoidModel<?> humanoid)) {
			return;
		}

		ItemStack cosmetic = item.getCosmeticItem(stack);
		ItemStack target = cosmetic.isEmpty() ? stack : cosmetic;

		poseStack.pushPose();
		AccessoryRenderer renderer = AccessoryRenderRegistry.get(target);
		if (renderer != null) {
			renderer.doRender(humanoid, target, living, poseStack, collector, light,
					renderState.walkAnimationPos, renderState.walkAnimationSpeed, renderState.partialTick,
					renderState.ageInTicks, yRot, xRot);
		} else if (!cosmetic.isEmpty() && !(cosmetic.getItem() instanceof BaubleItem)) {
			ICurioRenderer other = ICurioRenderer.getOrNull(cosmetic);
			if (other != null && other != INSTANCE) {
				other.render(cosmetic, slotContext, poseStack, collector, light, renderState, renderLayerParent, context, yRot, xRot);
			}
		}
		poseStack.popPose();
	}
}
