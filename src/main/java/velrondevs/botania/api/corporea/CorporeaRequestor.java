package velrondevs.botania.api.corporea;

import net.minecraft.world.entity.LivingEntity;

import org.jetbrains.annotations.Nullable;

public interface CorporeaRequestor {

	void doCorporeaRequest(CorporeaRequestMatcher request, int count, CorporeaSpark spark, @Nullable LivingEntity entity);

}
