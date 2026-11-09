package velrondevs.botania.mixin;

import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import velrondevs.botania.common.lib.BotaniaTags;

@Mixin(Bee.class)
public abstract class BeeMixin {
	@Inject(method = "attractsBees", at = @At("HEAD"), cancellable = true)
	private static void isSpecialFlower(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (state.is(BotaniaTags.Blocks.SPECIAL_FLOWERS)) {
			cir.setReturnValue(true);
		}
	}
}
