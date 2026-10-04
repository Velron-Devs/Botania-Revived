package velrondevs.botania.mixin.client;

import com.google.common.collect.ImmutableMap;

import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.SkullBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import velrondevs.botania.client.model.GaiaHeadModel;
import velrondevs.botania.client.render.block_entity.GaiaHeadBlockEntityRenderer;
import velrondevs.botania.common.block.GaiaHeadBlock;

import java.util.Map;

@Mixin(SkullBlockRenderer.class)
public abstract class SkullBlockRendererMixin {
	@Inject(
		method = "createSkullRenderers",
		at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;", remap = false),
		locals = LocalCapture.CAPTURE_FAILSOFT
	)
	private static void registerModel(EntityModelSet entityModelSet, CallbackInfoReturnable<Map<SkullBlock.Type, SkullModelBase>> cir,
			ImmutableMap.Builder<SkullBlock.Type, SkullModelBase> builder) {
		builder.put(GaiaHeadBlock.GAIA_TYPE, new GaiaHeadModel());

		SkullBlockRenderer.SKIN_BY_TYPE.put(GaiaHeadBlock.GAIA_TYPE, DefaultPlayerSkin.getDefaultTexture());
	}

	@Inject(at = @At("HEAD"), method = "getRenderType", cancellable = true)
	private static void hookGetRenderType(SkullBlock.Type type, ResolvableProfile profile, CallbackInfoReturnable<RenderType> cir) {
		if (type == GaiaHeadBlock.GAIA_TYPE) {
			GaiaHeadBlockEntityRenderer.hookGetRenderType(cir);
		}
	}
}
