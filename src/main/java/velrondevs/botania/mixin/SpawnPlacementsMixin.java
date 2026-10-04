package velrondevs.botania.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.ServerLevelAccessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import velrondevs.botania.common.brew.effect.BloodthirstMobEffect;

@Mixin(SpawnPlacements.class)
public class SpawnPlacementsMixin {

	@Inject(at = @At("RETURN"), cancellable = true, method = "checkSpawnRules")
	private static <T extends Entity> void bloodthirstOverride(EntityType<T> type, ServerLevelAccessor world, MobSpawnType reason, BlockPos position, RandomSource random, CallbackInfoReturnable<Boolean> cir) {
		if (reason == MobSpawnType.NATURAL && BloodthirstMobEffect.overrideSpawn(world, position, type.getCategory())) {
			cir.setReturnValue(true);
		}
	}

}
