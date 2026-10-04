package velrondevs.botania.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.common.NeoForge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import velrondevs.botania.event.BotaniaEvents;

import java.util.ArrayList;
import java.util.Map;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerEventsMixin {
	@Shadow
	private Map<ResourceLocation, RecipeHolder<?>> byName;

	@Shadow
	public abstract void replaceRecipes(Iterable<RecipeHolder<?>> recipes);

	@Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("TAIL"))
	private void botania_registerMachineRecipes(Map<ResourceLocation, ?> recipes, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
		var event = new BotaniaEvents.RegisterMachineRecipes(byName.keySet());
		NeoForge.EVENT_BUS.post(event);
		if (!event.getAdded().isEmpty()) {
			var all = new ArrayList<RecipeHolder<?>>(byName.values());
			all.addAll(event.getAdded());
			replaceRecipes(all);
		}
	}
}
