package velrondevs.botania.api.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

public interface PoolOverlayProvider {

	Identifier getIcon(Level world, BlockPos pos);

}
