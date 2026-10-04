package velrondevs.botania.api.corporea;

import net.minecraft.world.entity.LivingEntity;

import org.jetbrains.annotations.Nullable;

public interface CorporeaRequest {

	@Nullable
	LivingEntity getEntity();

	CorporeaRequestMatcher getMatcher();

	int getStillNeeded();

	int getFound();

	int getExtracted();

	void trackSatisfied(int count);

	void trackFound(int count);

	void trackExtracted(int count);

}
