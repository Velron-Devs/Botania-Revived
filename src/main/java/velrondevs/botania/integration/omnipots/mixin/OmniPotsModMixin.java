package velrondevs.botania.integration.omnipots.mixin;

import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import velrondevs.botania.integration.omnipots.OmniPotsSoils;

@Pseudo
@Mixin(targets = "com.kirarra.omnipots.ExampleMod")
public abstract class OmniPotsModMixin {
	@Inject(method = { "isSoil", "isDirt" }, at = @At("RETURN"), cancellable = true)
	private void botania$botaniaSoils(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValueZ() && OmniPotsSoils.isBotaniaSoil(stack)) {
			cir.setReturnValue(true);
		}
	}
}
