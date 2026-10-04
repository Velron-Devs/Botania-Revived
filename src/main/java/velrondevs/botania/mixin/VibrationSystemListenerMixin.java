package velrondevs.botania.mixin;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import velrondevs.botania.common.block.flower.functional.BergamuteBlockEntity;

@Mixin(VibrationSystem.Listener.class)
public class VibrationSystemListenerMixin {

	@Inject(method = "isOccluded", at = @At("HEAD"), cancellable = true)
	private static void checkBergamuteOcclusion(Level level, Vec3 sourcePos, Vec3 destPos, CallbackInfoReturnable<Boolean> cir) {
		if (BergamuteBlockEntity.isBergamuteOccludingVibration(level, sourcePos, destPos)) {
			cir.setReturnValue(true);
		}
	}
}
