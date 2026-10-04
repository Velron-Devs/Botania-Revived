package velrondevs.botania.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.NaturalSpawner;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import velrondevs.botania.common.block.flower.generating.NarslimmusBlockEntity;
import velrondevs.botania.common.brew.effect.BloodthirstMobEffect;
import velrondevs.botania.common.brew.effect.EmptinessMobEffect;

@Mixin(NaturalSpawner.class)
public class NaturalSpawnerMixin {

	@ModifyArg(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V"),
		method = "spawnCategoryForPosition(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V"
	)
	private static Entity onSpawned(Entity entity) {
		NarslimmusBlockEntity.onSpawn(entity);
		return entity;
	}

	@Inject(at = @At("HEAD"), method = "isValidPositionForMob", cancellable = true)
	private static void emptiness(ServerLevel world, Mob entity, double squaredDistance, CallbackInfoReturnable<Boolean> cir) {
		if (EmptinessMobEffect.shouldCancel(entity)) {
			cir.setReturnValue(false);
		}
	}

	@ModifyExpressionValue(method = "isValidPositionForMob", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/event/EventHooks;checkSpawnPosition(Lnet/minecraft/world/entity/Mob;Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;)Z"))
	private static boolean bloodthirstOverride(boolean original, @Local(argsOnly = true) ServerLevel world, @Local(argsOnly = true) Mob entity) {
		if (BloodthirstMobEffect.overrideSpawn(world, entity.blockPosition(), entity.getType().getCategory())) {
			return true;
		}
		return original;
	}
}
