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

public class LokiPlaceTrigger extends SimpleCriterionTrigger<LokiPlaceTrigger.Instance> {
	public static final ResourceLocation ID = prefix("loki_placed_blocks");
	public static final LokiPlaceTrigger INSTANCE = new LokiPlaceTrigger();

	private LokiPlaceTrigger() {}

	@NotNull
	@Override
	public Codec<Instance> codec() {
		return Instance.CODEC;
	}

	public void trigger(ServerPlayer player, ItemStack ring, int blocksPlaced) {
		trigger(player, instance -> instance.test(ring, blocksPlaced));
	}

	public record Instance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> ring,
			MinMaxBounds.Ints blocksPlaced) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
				ItemPredicate.CODEC.optionalFieldOf("ring").forGetter(Instance::ring),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("blocks_placed", MinMaxBounds.Ints.ANY).forGetter(Instance::blocksPlaced)
		).apply(instance, Instance::new));

		public static Criterion<Instance> placeBlocks(Optional<ItemPredicate> ring, MinMaxBounds.Ints blocksPlaced) {
			return INSTANCE.createCriterion(new Instance(Optional.empty(), ring, blocksPlaced));
		}

		public static Criterion<Instance> placeBlocks(MinMaxBounds.Ints blocksPlaced) {
			return placeBlocks(Optional.empty(), blocksPlaced);
		}

		boolean test(ItemStack ring, int blocksPlaced) {
			return (this.ring.isEmpty() || this.ring.get().test(ring)) && this.blocksPlaced.matches(blocksPlaced);
		}
	}
}
