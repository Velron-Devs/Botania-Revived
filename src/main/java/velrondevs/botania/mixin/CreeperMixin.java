package velrondevs.botania.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import velrondevs.botania.common.block.flower.functional.TigerseyeBlockEntity;

@Mixin(Creeper.class)
public class CreeperMixin extends Monster {
	protected CreeperMixin(EntityType<? extends Monster> type, Level level) {
		super(type, level);
	}

	@Inject(method = "registerGoals", at = @At("TAIL"))
	private void addTigerseyeGoal(CallbackInfo ci) {
		goalSelector.addGoal(3, new TigerseyeBlockEntity.CreeperAvoidPlayerGoal((Creeper) (Object) this));
	}
}
