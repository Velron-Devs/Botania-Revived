package velrondevs.botania.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.projectile.Projectile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Projectile.class)
public interface ProjectileAccessor {
	@Accessor("leftOwner")
	boolean botania_getLeftOwner();

	@Accessor("owner")
	EntityReference<Entity> botania_getOwnerReference();

	@Invoker("setOwner")
	void botania_setOwnerReference(EntityReference<Entity> owner);
}
