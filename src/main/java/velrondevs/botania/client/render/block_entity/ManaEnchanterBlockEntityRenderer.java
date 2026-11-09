package velrondevs.botania.client.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.common.block.block_entity.ManaEnchanterBlockEntity;
import velrondevs.botania.common.helper.VecHelper;

import java.util.Objects;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class ManaEnchanterBlockEntityRenderer extends BotaniaBlockEntityRenderer<ManaEnchanterBlockEntity> {

	private final TextureAtlasSprite overlaySprite;

	public ManaEnchanterBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		this.overlaySprite = Objects.requireNonNull(
				RenderHelper.blockSprite(prefix("block/enchanter_overlay"))
		);
	}

	@Override
	protected void render(@NotNull ManaEnchanterBlockEntity enchanter, float partTicks, PoseStack ms, MultiBufferSource buffers, SubmitNodeCollector collector, int light, int overlay) {
		float alphaMod = 0F;

		if (enchanter.stage == ManaEnchanterBlockEntity.State.GATHER_MANA) {
			alphaMod = Math.min(20, enchanter.stageTicks) / 20F;
		} else if (enchanter.stage == ManaEnchanterBlockEntity.State.RESET) {
			alphaMod = (20 - enchanter.stageTicks) / 20F;
		} else if (enchanter.stage == ManaEnchanterBlockEntity.State.DO_ENCHANT) {
			alphaMod = 1F;
		}

		ms.pushPose();
		if (!enchanter.itemToEnchant.isEmpty()) {
			ms.translate(0.5F, 1.25F, 0.5F);
			RenderHelper.renderItemLikeEntity(enchanter.itemToEnchant, ClientTickHandler.ticksInGame + partTicks, 0F, ms, collector, light, enchanter.getLevel());
			ms.translate(-0.5F, -1.25F, -0.5F);
		}

		ms.mulPose(VecHelper.rotateX(90F));
		ms.translate(-2F, -2F, -0.001F);

		float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + partTicks) / 8D) + 1D) / 5D + 0.4D) * alphaMod;

		if (alpha > 0) {
			if (enchanter.stage == ManaEnchanterBlockEntity.State.DO_ENCHANT || enchanter.stage == ManaEnchanterBlockEntity.State.RESET) {
				float ticks = enchanter.stageTicks + enchanter.stage3EndTicks + partTicks;
				float angle = ticks * 2;
				float yTranslation = Math.min(20, ticks) / 20F * 1.15F;
				float scale = ticks < 10 ? 1F : 1F - Math.min(20, ticks - 10) / 20F * 0.75F;

				ms.translate(2.5F, 2.5F, -yTranslation);
				ms.scale(scale, scale, 1F);
				ms.mulPose(VecHelper.rotateZ(angle));
				ms.translate(-2.5F, -2.5F, 0F);
			}

			VertexConsumer buffer = buffers.getBuffer(RenderHelper.ENCHANTER);
			RenderHelper.renderIconFullBright(
					ms, buffer,
					0, 0, 5, 5,
					0, 0, 16, 16,
					this.overlaySprite, 0xFFFFFF, alpha, light
			);
		}

		ms.popPose();
	}

}
