package velrondevs.botania.common.brew.effect;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import org.jetbrains.annotations.NotNull;

public class AbsolutionMobEffect extends InstantenousMobEffect {

	public AbsolutionMobEffect() {
		super(MobEffectCategory.NEUTRAL, 0xFFFFFF);
	}

	@Override
	public void applyInstantenousEffect(Entity e, Entity e1, @NotNull LivingEntity e2, int t, double d) {
		e2.removeAllEffects();
	}

	@Override
	public boolean applyEffectTick(LivingEntity livingEntity, int t) {

		livingEntity.removeAllEffects();
		return true;
	}
}
