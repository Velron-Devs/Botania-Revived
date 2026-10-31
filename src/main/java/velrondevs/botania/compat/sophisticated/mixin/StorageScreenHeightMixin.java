package velrondevs.botania.compat.sophisticated.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase", remap = false)
public class StorageScreenHeightMixin {
	private static final int MAX_SCREEN_HEIGHT = 114 + 25 * 18;

	@ModifyVariable(method = "updateDimensionsAndSlotPositions", at = @At("HEAD"), argsOnly = true, remap = false)
	private int botania$capScreenHeight(int guiScaledHeight) {
		return Math.min(guiScaledHeight, MAX_SCREEN_HEIGHT);
	}
}
