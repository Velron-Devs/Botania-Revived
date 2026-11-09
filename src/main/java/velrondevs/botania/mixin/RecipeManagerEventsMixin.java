package velrondevs.botania.mixin;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import net.neoforged.neoforge.common.NeoForge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import velrondevs.botania.event.BotaniaEvents;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerEventsMixin {
	@ModifyVariable(method = "apply(Lnet/minecraft/world/item/crafting/RecipeMap;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("HEAD"), argsOnly = true)
	private RecipeMap botania_registerMachineRecipes(RecipeMap recipes) {
		Set<Identifier> existing = recipes.values().stream().map(holder -> holder.id().identifier()).collect(Collectors.toSet());
		var event = new BotaniaEvents.RegisterMachineRecipes(existing);
		NeoForge.EVENT_BUS.post(event);
		if (event.getAdded().isEmpty()) {
			return recipes;
		}
		var all = new ArrayList<RecipeHolder<?>>(recipes.values());
		all.addAll(event.getAdded());
		return RecipeMap.create(all);
	}
}
