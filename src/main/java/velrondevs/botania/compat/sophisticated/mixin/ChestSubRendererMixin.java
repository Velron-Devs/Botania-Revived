package velrondevs.botania.compat.sophisticated.mixin;

import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.ChestType;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import velrondevs.botania.compat.sophisticated.client.BotaniaStorageClient;

@Mixin(targets = "net.p3pp3rf1y.sophisticatedstorage.client.render.ChestRenderer$ChestSubRenderer", remap = false)
public class ChestSubRendererMixin {
	@Shadow
	@Final
	private ChestType chestType;

	@Inject(method = "getTierMaterial", at = @At("HEAD"), cancellable = true, remap = false)
	private void botania$tierMaterial(Block block, CallbackInfoReturnable<SpriteId> cir) {
		SpriteId material = BotaniaStorageClient.chestMaterial(block, chestType);
		if (material != null) {
			cir.setReturnValue(material);
		}
	}
}
