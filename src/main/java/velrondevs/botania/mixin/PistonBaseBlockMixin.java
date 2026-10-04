package velrondevs.botania.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import velrondevs.botania.common.helper.EthicalTntHelper;
import velrondevs.botania.common.helper.ForcePushHelper;

@Mixin(value = PistonBaseBlock.class, priority = 999 )
public abstract class PistonBaseBlockMixin {
	@Inject(
		at = @At("HEAD"),
		method = "moveBlocks(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Z"
	)
	private void preMoveBlocks(Level level, BlockPos pos, Direction dir, boolean extending,
			CallbackInfoReturnable<Boolean> cir) {
		if (!level.isClientSide()) {
			EthicalTntHelper.startTrackingTntEntities();
			ForcePushHelper.pushMovementTypeContext(extending, dir);
		}
	}

	@Inject(
		at = @At(value = "RETURN"),
		method = "moveBlocks(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Z"
	)
	private void postMoveBlocks(Level level, BlockPos pos, Direction dir, boolean extending,
			CallbackInfoReturnable<Boolean> cir) {
		if (!level.isClientSide()) {
			ForcePushHelper.popMovementTypeContext();
			EthicalTntHelper.endTrackingTntEntitiesAndCheck();
		}
	}

	@ModifyVariable(
		method = "moveBlocks",
		at = @At(value = "LOAD", ordinal = 4),
		ordinal = 0,
		argsOnly = true
	)
	private boolean isExtendingNonForcePusher(boolean extending) {
		return !ForcePushHelper.isForcePush() && extending;
	}
}
