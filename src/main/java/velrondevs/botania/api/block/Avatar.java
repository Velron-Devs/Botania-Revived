package velrondevs.botania.api.block;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;

import java.util.Map;
import java.util.UUID;

public interface Avatar {

	Container getInventory();

	Direction getAvatarFacing();

	int getElapsedFunctionalTicks();

	boolean isEnabled();

	Map<UUID, Integer> getBoostCooldowns();

}
