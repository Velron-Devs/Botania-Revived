package velrondevs.botania.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class ManaBlasterTrigger extends SimpleCriterionTrigger<ManaBlasterTrigger.Instance> {
	public static final ResourceLocation ID = prefix("fire_mana_blaster");
	public static final ManaBlasterTrigger INSTANCE = new ManaBlasterTrigger();

	private ManaBlasterTrigger() {}

	@NotNull
	@Override
	public Codec<Instance> codec() {
		return Instance.CODEC;
	}

	public void trigger(ServerPlayer player, ItemStack stack) {
		trigger(player, instance -> instance.test(stack, player));
	}

	public record Instance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item,
			Optional<ContextAwarePredicate> user) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
				ItemPredicate.CODEC.optionalFieldOf("item").forGetter(Instance::item),
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("user").forGetter(Instance::user)
		).apply(instance, Instance::new));

		public static Criterion<Instance> shoot(Optional<ItemPredicate> item, Optional<ContextAwarePredicate> user) {
			return INSTANCE.createCriterion(new Instance(Optional.empty(), item, user));
		}

		public static Criterion<Instance> shoot() {
			return shoot(Optional.empty(), Optional.empty());
		}

		boolean test(ItemStack stack, ServerPlayer entity) {
			return (this.item.isEmpty() || this.item.get().test(stack))
					&& (this.user.isEmpty() || this.user.get().matches(EntityPredicate.createContext(entity, entity)));
		}

		@Override
		public void validate(@NotNull CriterionValidator validator) {
			SimpleCriterionTrigger.SimpleInstance.super.validate(validator);
			validator.validateEntity(this.user, ".user");
		}
	}
}
