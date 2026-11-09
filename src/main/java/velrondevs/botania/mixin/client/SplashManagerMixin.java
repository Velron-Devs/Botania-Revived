package velrondevs.botania.mixin.client;

import net.minecraft.client.resources.SplashManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import velrondevs.botania.xplat.BotaniaConfig;

import java.util.ArrayList;
import java.util.List;

@Mixin(SplashManager.class)
public class SplashManagerMixin {
	@Shadow
	private List<Component> splashes;

	@Inject(at = @At("RETURN"), method = "apply(Ljava/util/List;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V")
	public void addSplashes(List<Component> preparations, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
		if (BotaniaConfig.client() != null && BotaniaConfig.client().splashesEnabled()) {
			List<Component> list = new ArrayList<>(this.splashes);
			list.add(SplashManager.literalSplash("Do not feed bread to elves!"));
			this.splashes = List.copyOf(list);
		}
	}
}
