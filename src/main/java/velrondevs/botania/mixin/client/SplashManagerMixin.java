package velrondevs.botania.mixin.client;

import net.minecraft.client.resources.SplashManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import velrondevs.botania.xplat.BotaniaConfig;

import java.util.List;

@Mixin(SplashManager.class)
public class SplashManagerMixin {
	@Shadow
	@Final
	private List<String> splashes;

	@Inject(at = @At("RETURN"), method = "apply(Ljava/util/List;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V")
	public void addSplashes(List<String> splashes, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
		if (BotaniaConfig.client() != null && BotaniaConfig.client().splashesEnabled()) {
			this.splashes.add("Do not feed bread to elves!");
		}
	}
}
