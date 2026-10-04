package velrondevs.botania.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonStructureResolver;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import velrondevs.botania.common.helper.ForcePushHelper;

@Mixin(value = PistonStructureResolver.class)
public class PistonStructureResolverMixin {
	@Final
	@Mutable
	@Shadow
	private BlockPos pistonPos;

	@Inject(method = "<init>", at = @At(value = "RETURN"))
	private void modifyForcePushOrigin(Level level, BlockPos pistonPos, Direction pistonDirection, boolean extending, CallbackInfo ci) {
		this.pistonPos = ForcePushHelper.isForcePush() ? ForcePushHelper.getForcePushOrigin() : pistonPos;
	}
}
