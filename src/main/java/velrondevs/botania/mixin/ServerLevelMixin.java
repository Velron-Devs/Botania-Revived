package velrondevs.botania.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import velrondevs.botania.common.world.SkyblockWorldEvents;
import velrondevs.botania.xplat.XplatAbstractions;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
	@Inject(at = @At("RETURN"), method = "addPlayer")
	private void onEntityAdd(ServerPlayer entity, CallbackInfo ci) {
		if (XplatAbstractions.INSTANCE.gogLoaded()) {
			SkyblockWorldEvents.syncGogStatus(entity);
		}
	}
}
