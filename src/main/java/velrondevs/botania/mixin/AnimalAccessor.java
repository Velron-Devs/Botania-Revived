package velrondevs.botania.mixin;

import net.minecraft.world.entity.animal.Animal;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityReference;

@Mixin(Animal.class)
public interface AnimalAccessor {
	@Accessor("loveCause")
	void botania_setLoveCause(EntityReference<ServerPlayer> cause);
}
