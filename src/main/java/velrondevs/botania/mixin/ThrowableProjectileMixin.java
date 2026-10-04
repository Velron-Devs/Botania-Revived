package velrondevs.botania.mixin;

import net.minecraft.world.entity.projectile.ThrowableProjectile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import velrondevs.botania.common.entity.ManaBurstEntity;

@Mixin(ThrowableProjectile.class)
public class ThrowableProjectileMixin {
	@ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;"))
	private double noDrag(double origScale) {

		if ((Object) this instanceof ManaBurstEntity) {
			return 1;
		}
		return origScale;
	}
}
