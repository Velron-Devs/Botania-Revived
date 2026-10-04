package velrondevs.botania.mixin;

import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import velrondevs.botania.common.lib.BotaniaTags;

import java.util.function.Predicate;

@Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeePollinateGoal")
public class PollinateGoalMixin {
	@Shadow
	@Mutable
	@Final
	private Predicate<BlockState> VALID_POLLINATION_BLOCKS;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void extendPredicate(Bee outer, CallbackInfo ci) {
		VALID_POLLINATION_BLOCKS = VALID_POLLINATION_BLOCKS.or(b -> b.is(BotaniaTags.Blocks.SPECIAL_FLOWERS));
	}
}
