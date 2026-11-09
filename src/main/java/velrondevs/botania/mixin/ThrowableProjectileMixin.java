package velrondevs.botania.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import velrondevs.botania.common.entity.ManaBurstEntity;

import java.util.function.Predicate;

@Mixin(ThrowableProjectile.class)
public class ThrowableProjectileMixin {
	@ModifyArg(method = "applyInertia", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;"))
	private double noDrag(double origScale) {

		if ((Object) this instanceof ManaBurstEntity) {
			return 1;
		}
		return origScale;
	}

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getHitResultOnMoveVector(Lnet/minecraft/world/entity/Entity;Ljava/util/function/Predicate;)Lnet/minecraft/world/phys/HitResult;"))
	private HitResult burstHitBeforeMove(Entity source, Predicate<Entity> matching, Operation<HitResult> original) {
		HitResult result = original.call(source, matching);
		if ((Object) this instanceof ManaBurstEntity burst) {
			return burst.processHitBeforeMove(result);
		}
		return result;
	}
}
