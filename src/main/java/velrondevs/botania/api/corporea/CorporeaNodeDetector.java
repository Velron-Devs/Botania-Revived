package velrondevs.botania.api.corporea;

import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

public interface CorporeaNodeDetector {

	@Nullable
	CorporeaNode getNode(Level world, CorporeaSpark spark);
}
