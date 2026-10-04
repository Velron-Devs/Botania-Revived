package velrondevs.botania.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.entity.GaiaGuardianEntity;

import java.util.Optional;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class GaiaGuardianNoArmorTrigger extends SimpleCriterionTrigger<GaiaGuardianNoArmorTrigger.Instance> {
	public static final ResourceLocation ID = prefix("gaia_guardian_no_armor");
	public static final GaiaGuardianNoArmorTrigger INSTANCE = new GaiaGuardianNoArmorTrigger();

	private GaiaGuardianNoArmorTrigger() {}

	@NotNull
	@Override
	public Codec<Instance> codec() {
		return Instance.CODEC;
	}

	public void trigger(ServerPlayer player, GaiaGuardianEntity guardian, DamageSource src) {
		trigger(player, instance -> instance.test(player, guardian, src));
	}

	public record Instance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> guardian,
			Optional<DamageSourcePredicate> killingBlow) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("guardian").forGetter(Instance::guardian),
				DamageSourcePredicate.CODEC.optionalFieldOf("killing_blow").forGetter(Instance::killingBlow)
		).apply(instance, Instance::new));

		public static Criterion<Instance> noArmor(Optional<ContextAwarePredicate> guardian, Optional<DamageSourcePredicate> killingBlow) {
			return INSTANCE.createCriterion(new Instance(Optional.empty(), guardian, killingBlow));
		}

		public static Criterion<Instance> noArmor() {
			return noArmor(Optional.empty(), Optional.empty());
		}

		boolean test(ServerPlayer player, GaiaGuardianEntity guardian, DamageSource src) {
			return (this.guardian.isEmpty() || this.guardian.get().matches(EntityPredicate.createContext(player, guardian)))
					&& (this.killingBlow.isEmpty() || this.killingBlow.get().matches(player, src));
		}

		@Override
		public void validate(@NotNull CriterionValidator validator) {
			SimpleCriterionTrigger.SimpleInstance.super.validate(validator);
			validator.validateEntity(this.guardian, ".guardian");
		}
	}
}
