package velrondevs.botania.client.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.common.block.block_entity.TerrestrialAgglomerationPlateBlockEntity;
import velrondevs.botania.common.helper.VecHelper;

import java.util.Objects;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class TerrestrialAgglomerationPlateBlockEntityRenderer implements BlockEntityRenderer<TerrestrialAgglomerationPlateBlockEntity> {
	private final TextureAtlasSprite overlaySprite;

	public TerrestrialAgglomerationPlateBlockEntityRenderer(BlockEntityRendererProvider.Context manager) {
		this.overlaySprite = Objects.requireNonNull(
				Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
						.apply(prefix("block/terra_plate_overlay"))
		);
	}

	@Override
	public void render(@NotNull TerrestrialAgglomerationPlateBlockEntity plate, float f, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		float alphaMod = Math.min(1.0F, plate.getCompletion() / 0.1F);

		ms.pushPose();
		ms.translate(0F, 3F / 16F + 0.001F, 0F);
		ms.mulPose(VecHelper.rotateX(90F));

		float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 5D + 0.6D) * alphaMod;

		VertexConsumer buffer = buffers.getBuffer(RenderHelper.TERRA_PLATE);
		RenderHelper.renderIconFullBright(ms, buffer, this.overlaySprite, alpha);

		ms.popPose();
	}

}
