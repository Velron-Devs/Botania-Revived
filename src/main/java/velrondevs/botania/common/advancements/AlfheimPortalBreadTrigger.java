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

public class AlfheimPortalBreadTrigger extends SimpleCriterionTrigger<AlfheimPortalBreadTrigger.Instance> {
	public static final ResourceLocation ID = prefix("alf_portal_bread");
	public static final AlfheimPortalBreadTrigger INSTANCE = new AlfheimPortalBreadTrigger();

	private AlfheimPortalBreadTrigger() {}

	@NotNull
	@Override
	public Codec<Instance> codec() {
		return Instance.CODEC;
	}

	public void trigger(ServerPlayer player, BlockPos portal) {
		this.trigger(player, instance -> instance.test(player.serverLevel(), portal));
	}

	public record Instance(Optional<ContextAwarePredicate> player, Optional<LocationPredicate> portal)
			implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
				LocationPredicate.CODEC.optionalFieldOf("portal_location").forGetter(Instance::portal)
		).apply(instance, Instance::new));

		public static Criterion<Instance> bread(Optional<LocationPredicate> portal) {
			return INSTANCE.createCriterion(new Instance(Optional.empty(), portal));
		}

		public static Criterion<Instance> bread() {
			return bread(Optional.empty());
		}

		boolean test(ServerLevel world, BlockPos portal) {
			return this.portal.isEmpty() || this.portal.get().matches(world, portal.getX(), portal.getY(), portal.getZ());
		}
	}
}
