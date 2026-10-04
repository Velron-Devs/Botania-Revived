package velrondevs.botania.mixin;

import net.minecraft.world.level.redstone.CollectingNeighborUpdater;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import velrondevs.botania.common.CollectingNeighborUpdaterAccess;

@Mixin(CollectingNeighborUpdater.class)
abstract class CollectingNeighborUpdaterMixin implements CollectingNeighborUpdaterAccess {
	@Shadow
	private int count;
	@Shadow
	@Final
	private int maxChainedNeighborUpdates;

	@Shadow
	protected abstract void runUpdates();

	@Unique
	private boolean delayUpdates = false;

	@Inject(method = "runUpdates", at = @At("HEAD"), cancellable = true)
	void skipUpdatesWhenDelayed(final CallbackInfo ci) {
		if (this.delayUpdates

				&& this.count < this.maxChainedNeighborUpdates) {
			ci.cancel();
		}
	}

	@Override
	public void botania$pauseUpdates() {
		this.delayUpdates = true;
	}

	@Override
	public void botania$resumeUpdates() {
		this.delayUpdates = false;
		this.runUpdates();
	}
}
