package velrondevs.botania.mixin.client;

import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.SkullBlock;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import velrondevs.botania.client.render.block_entity.GaiaHeadBlockEntityRenderer;
import velrondevs.botania.common.block.GaiaHeadBlock;

@Mixin(SkullBlockRenderer.class)
public abstract class SkullBlockRendererMixin {
	@Inject(method = "getSkullRenderType", at = @At("HEAD"), cancellable = true)
	private static void botania$gaiaRenderType(SkullBlock.Type type, @Nullable Identifier texture, CallbackInfoReturnable<RenderType> cir) {
		if (type == GaiaHeadBlock.GAIA_TYPE && texture == null) {
			cir.setReturnValue(GaiaHeadBlockEntityRenderer.getViewRenderType());
		}
	}
}
