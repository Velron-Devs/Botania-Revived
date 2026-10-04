package velrondevs.botania.mixin;

import net.minecraft.world.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import velrondevs.botania.common.item.material.SelfReturningItem;

@Mixin(Item.class)
public class ItemMixin {
	@Inject(at = @At("HEAD"), method = "getCraftingRemainingItem", cancellable = true)
	private void returnSelf(CallbackInfoReturnable<Item> cir) {
		Item self = (Item) (Object) this;
		if (self instanceof SelfReturningItem) {
			cir.setReturnValue(self);
		}
	}
}
