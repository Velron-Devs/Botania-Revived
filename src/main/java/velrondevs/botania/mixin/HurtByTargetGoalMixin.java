package velrondevs.botania.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HurtByTargetGoal.class)
public class HurtByTargetGoalMixin {

	@Inject(method = "alertOther", at = @At("HEAD"), cancellable = true)
	private void checkSelfTargeting(Mob mobToAlert, LivingEntity targetToAttack, CallbackInfo ci) {
		if (mobToAlert == targetToAttack) {
			ci.cancel();
		}
	}
}
