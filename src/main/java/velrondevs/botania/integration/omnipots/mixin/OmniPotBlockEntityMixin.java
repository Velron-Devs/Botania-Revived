package velrondevs.botania.integration.omnipots.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import velrondevs.botania.integration.omnipots.OmniPotsSoils;

@Pseudo
@Mixin(targets = "com.kirarra.omnipots.OmniPotBlockEntity")
public abstract class OmniPotBlockEntityMixin {
	@Shadow
	private ItemStack soilItem;

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/kirarra/omnipots/OmniPotBlockEntity;getSoilSpeedMultiplier()F"))
	private void botania$manaSoilTick(CallbackInfo ci) {
		OmniPotsSoils.tickMana((BlockEntity) (Object) this, soilItem);
	}

	@Inject(method = "getSoilSpeedMultiplier", at = @At("RETURN"), cancellable = true)
	private void botania$soilSpeed(CallbackInfoReturnable<Float> cir) {
		float multiplier = OmniPotsSoils.speedMultiplier((BlockEntity) (Object) this, soilItem);
		if (multiplier > 0F) {
			cir.setReturnValue(multiplier);
		}
	}
}
