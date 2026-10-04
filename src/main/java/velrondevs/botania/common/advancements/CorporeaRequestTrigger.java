package velrondevs.botania.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class CorporeaRequestTrigger extends SimpleCriterionTrigger<CorporeaRequestTrigger.Instance> {
	public static final ResourceLocation ID = prefix("corporea_index_request");
	public static final CorporeaRequestTrigger INSTANCE = new CorporeaRequestTrigger();

	private CorporeaRequestTrigger() {}

	@NotNull
	@Override
	public Codec<Instance> codec() {
		return Instance.CODEC;
	}

	public void trigger(ServerPlayer player, ServerLevel world, BlockPos pos, int count) {
		this.trigger(player, instance -> instance.test(world, pos, count));
	}

	public record Instance(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints count,
			Optional<LocationPredicate> indexPos) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("extracted", MinMaxBounds.Ints.ANY).forGetter(Instance::count),
				LocationPredicate.CODEC.optionalFieldOf("location").forGetter(Instance::indexPos)
		).apply(instance, Instance::new));

		public static Criterion<Instance> request(MinMaxBounds.Ints count, Optional<LocationPredicate> indexPos) {
			return INSTANCE.createCriterion(new Instance(Optional.empty(), count, indexPos));
		}

		public static Criterion<Instance> request(MinMaxBounds.Ints count) {
			return request(count, Optional.empty());
		}

		boolean test(ServerLevel world, BlockPos pos, int count) {
			return this.count.matches(count)
					&& (this.indexPos.isEmpty() || this.indexPos.get().matches(world, pos.getX(), pos.getY(), pos.getZ()));
		}
	}

}
