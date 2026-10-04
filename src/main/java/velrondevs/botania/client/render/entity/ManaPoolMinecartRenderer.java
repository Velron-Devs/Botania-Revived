package velrondevs.botania.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.internal.ManaBurst;
import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.client.render.block_entity.ManaPoolBlockEntityRenderer;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.common.entity.ManaPoolMinecartEntity;
import velrondevs.botania.registry.BotaniaBlocks;

public class ManaPoolMinecartRenderer extends MinecartRenderer<ManaPoolMinecartEntity> {
	private static final ManaPoolBlockEntity DUMMY = new ManaPoolBlockEntity(ManaBurst.NO_SOURCE, BotaniaBlocks.manaPool.defaultBlockState());

	public ManaPoolMinecartRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, ModelLayers.MINECART);
	}

	@Override
	protected void renderMinecartContents(ManaPoolMinecartEntity poolCart, float partialTicks, @NotNull BlockState state, PoseStack ms, MultiBufferSource buffers, int light) {
		super.renderMinecartContents(poolCart, partialTicks, state, ms, buffers, light);
		ManaPoolBlockEntityRenderer.cartMana = poolCart.getMana();
		Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(DUMMY)
				.render(null, ClientTickHandler.partialTicks, ms, buffers, light, OverlayTexture.NO_OVERLAY);
	}

}
