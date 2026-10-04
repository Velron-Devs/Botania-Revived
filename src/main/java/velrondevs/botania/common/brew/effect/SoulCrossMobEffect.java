package velrondevs.botania.common.brew.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import velrondevs.botania.registry.BotaniaMobEffects;

public class SoulCrossMobEffect extends MobEffect {

	public SoulCrossMobEffect() {
		super(MobEffectCategory.BENEFICIAL, 0x47453d);
	}

	public static void onEntityKill(LivingEntity dying, LivingEntity killer) {
		if (killer.hasEffect(BotaniaMobEffects.soulCross)) {
			killer.heal(dying.getMaxHealth() / 20);
		}
	}

}
