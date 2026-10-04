package velrondevs.botania.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.scores.PlayerTeam;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import velrondevs.botania.common.block.flower.functional.LooniumBlockEntity;
import velrondevs.botania.common.item.EquestrianVirusItem;
import velrondevs.botania.common.item.equipment.bauble.CrimsonPendantItem;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.registry.BotaniaDamageTypes;
import velrondevs.botania.xplat.XplatAbstractions;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow
	abstract EntityType<?> getType();

	@Inject(at = @At("HEAD"), method = "isInvulnerableTo", cancellable = true)
	private void checkInvulnerabilities(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if (((Object) this) instanceof LivingEntity self) {
			if (EquestrianVirusItem.onLivingHurt(self, source)) {
				cir.setReturnValue(true);
			} else if (CrimsonPendantItem.onDamage(self, source)) {
				cir.setReturnValue(true);
			}
		}
		if (source.is(BotaniaDamageTypes.KEY_EXPLOSION) && this.getType().is(BotaniaTags.Entities.KEY_IMMUNE)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(at = @At("HEAD"), method = "getTeam", cancellable = true)
	private void getLooniumTeam(CallbackInfoReturnable<PlayerTeam> cir) {
		if (((Object) this) instanceof Mob self) {
			var looniumComponent = XplatAbstractions.INSTANCE.looniumComponent(self);
			if (looniumComponent != null && looniumComponent.isSlowDespawn() && !looniumComponent.getDrop().isEmpty()
					&& LooniumBlockEntity.LOONIUM_TEAM instanceof PlayerTeam looniumTeam) {
				cir.setReturnValue(looniumTeam);
			}
		}
	}
}
