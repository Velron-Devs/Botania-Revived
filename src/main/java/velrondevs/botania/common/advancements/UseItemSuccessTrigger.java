package velrondevs.botania.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class UseItemSuccessTrigger extends SimpleCriterionTrigger<UseItemSuccessTrigger.Instance> {
	public static final ResourceLocation ID = prefix("use_item_success");
	public static final UseItemSuccessTrigger INSTANCE = new UseItemSuccessTrigger();

	private UseItemSuccessTrigger() {}

	@NotNull
	@Override
	public Codec<Instance> codec() {
		return Instance.CODEC;
	}

	public void trigger(ServerPlayer player, ItemStack stack, ServerLevel world, double x, double y, double z) {
		trigger(player, instance -> instance.test(stack, world, x, y, z));
	}

	public record Instance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item,
			Optional<LocationPredicate> location) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
				ItemPredicate.CODEC.optionalFieldOf("item").forGetter(Instance::item),
				LocationPredicate.CODEC.optionalFieldOf("location").forGetter(Instance::location)
		).apply(instance, Instance::new));

		public static Criterion<Instance> useItem(ItemPredicate item, Optional<LocationPredicate> location) {
			return INSTANCE.createCriterion(new Instance(Optional.empty(), Optional.of(item), location));
		}

		public static Criterion<Instance> useItem(ItemPredicate item) {
			return useItem(item, Optional.empty());
		}

		boolean test(ItemStack stack, ServerLevel world, double x, double y, double z) {
			return (this.item.isEmpty() || this.item.get().test(stack))
					&& (this.location.isEmpty() || this.location.get().matches(world, x, y, z));
		}
	}
}
