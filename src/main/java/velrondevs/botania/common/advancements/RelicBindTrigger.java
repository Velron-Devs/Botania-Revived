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

public class RelicBindTrigger extends SimpleCriterionTrigger<RelicBindTrigger.Instance> {
	public static final ResourceLocation ID = prefix("relic_bind");
	public static final RelicBindTrigger INSTANCE = new RelicBindTrigger();

	private RelicBindTrigger() {}

	@NotNull
	@Override
	public Codec<Instance> codec() {
		return Instance.CODEC;
	}

	public void trigger(ServerPlayer player, ItemStack relic) {
		trigger(player, instance -> instance.test(relic));
	}

	public record Instance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> predicate)
			implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
				ItemPredicate.CODEC.optionalFieldOf("relic").forGetter(Instance::predicate)
		).apply(instance, Instance::new));

		public static Criterion<Instance> bindRelic(ItemPredicate relic) {
			return INSTANCE.createCriterion(new Instance(Optional.empty(), Optional.of(relic)));
		}

		boolean test(ItemStack stack) {
			return predicate.isEmpty() || predicate.get().test(stack);
		}
	}
}
