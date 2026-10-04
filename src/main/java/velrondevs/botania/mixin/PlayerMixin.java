package velrondevs.botania.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import velrondevs.botania.common.PlayerAccess;
import velrondevs.botania.common.item.ResoluteIvyItem;
import velrondevs.botania.common.item.equipment.armor.terrasteel.TerrasteelHelmItem;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerAccess {
	protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@Unique
	private LivingEntity terraWillCritTarget;

	@Override
	public void botania$setCritTarget(LivingEntity entity) {
		this.terraWillCritTarget = entity;
	}

	@ModifyArg(
		method = "attack",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z")
	)
	private DamageSource onDamageTarget(DamageSource source, float amount) {
		if (this.terraWillCritTarget != null) {
			DamageSource newSource = TerrasteelHelmItem.onEntityAttacked(source, amount, (Player) (Object) this, terraWillCritTarget);
			this.terraWillCritTarget = null;
			return newSource;
		}
		return source;
	}

	@Inject(
		at = @At(value = "RETURN"), method = "attack",
		slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/CommonHooks;fireCriticalHit(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;ZF)Lnet/neoforged/neoforge/event/entity/player/CriticalHitEvent;"))
	)
	private void clearCritTarget(CallbackInfo ci) {
		this.terraWillCritTarget = null;
	}

	@Inject(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;dropAll()V"),
		method = "dropEquipment"
	)
	private void captureIvyDrops(CallbackInfo ci) {
		ResoluteIvyItem.keepDropsOnDeath((Player) (Object) this);
	}
}
