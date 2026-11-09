package velrondevs.botania.compat.sophisticated.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase", remap = false)
public class StorageScreenHeightMixin {
	private static final int MAX_VISIBLE_ROWS = 25;

	@ModifyExpressionValue(method = "updateDimensionsAndSlotPositions", remap = false,
			at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I", ordinal = 0, remap = false))
	private int botania$capVisibleRows(int rows) {
		return Math.min(rows, MAX_VISIBLE_ROWS);
	}
}
