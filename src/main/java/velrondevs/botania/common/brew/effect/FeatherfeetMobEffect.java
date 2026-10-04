package velrondevs.botania.common.brew.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import org.jetbrains.annotations.NotNull;

public class FeatherfeetMobEffect extends MobEffect {

	public FeatherfeetMobEffect() {
		super(MobEffectCategory.BENEFICIAL, 0x26ADFF);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean applyEffectTick(@NotNull LivingEntity living, int amplified) {
		if (living.fallDistance > 2.5F) {
			living.fallDistance = 2.5F;
		}
		return true;
	}

}
