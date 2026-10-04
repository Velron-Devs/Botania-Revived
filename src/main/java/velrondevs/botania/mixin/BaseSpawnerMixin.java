package velrondevs.botania.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import velrondevs.botania.common.block.block_entity.LifeImbuerBlockEntity;

@Mixin(BaseSpawner.class)
public class BaseSpawnerMixin {
	@Inject(at = @At("RETURN"), method = "isNearPlayer", cancellable = true)
	private void injectNearPlayer(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {

		if (!cir.getReturnValueZ()) {
			LifeImbuerBlockEntity.onSpawnerNearPlayer(level, pos, cir);
		}
	}
}
