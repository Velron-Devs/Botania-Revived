package velrondevs.botania.common.block.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

import velrondevs.botania.api.mana.PoolOverlayProvider;
import velrondevs.botania.common.block.BotaniaBlock;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class AlchemyCatalystBlock extends BotaniaBlock implements PoolOverlayProvider {
	private static final Identifier OVERLAY_ICON = prefix("block/alchemy_catalyst_overlay");

	public AlchemyCatalystBlock(Properties builder) {
		super(builder);
	}

	@Override
	public Identifier getIcon(Level world, BlockPos pos) {
		return OVERLAY_ICON;
	}

}
