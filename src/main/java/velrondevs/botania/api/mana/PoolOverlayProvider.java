package velrondevs.botania.api.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public interface PoolOverlayProvider {

	ResourceLocation getIcon(Level world, BlockPos pos);

}
