package velrondevs.botania.common.block.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class ConjurationCatalystBlock extends AlchemyCatalystBlock {
	private static final ResourceLocation OVERLAY_ICON = prefix("block/conjuration_catalyst_overlay");

	public ConjurationCatalystBlock(Properties builder) {
		super(builder);
	}

	@Override
	public ResourceLocation getIcon(Level world, BlockPos pos) {
		return OVERLAY_ICON;
	}
}
