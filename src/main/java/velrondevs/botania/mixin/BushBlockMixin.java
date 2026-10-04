package velrondevs.botania.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import velrondevs.botania.common.block.BotaniaGrassBlock;
import velrondevs.botania.common.block.EnchantedSoilBlock;

@Mixin(BushBlock.class)
public class BushBlockMixin {
	@Inject(at = @At("HEAD"), method = "mayPlaceOn", cancellable = true)
	private void canPlant(BlockState floor, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (floor.getBlock() instanceof EnchantedSoilBlock || floor.getBlock() instanceof BotaniaGrassBlock) {
			cir.setReturnValue(true);
		}
	}
}
