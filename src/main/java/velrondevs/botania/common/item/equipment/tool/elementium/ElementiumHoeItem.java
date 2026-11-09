package velrondevs.botania.common.item.equipment.tool.elementium;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.common.item.equipment.tool.manasteel.ManasteelHoeItem;

public class ElementiumHoeItem extends ManasteelHoeItem {
	public ElementiumHoeItem(Properties props) {
		super(BotaniaAPI.instance().getElementiumItemTier(), props, -1f);
	}

	@NotNull
	@Override
	public InteractionResult useOn(@NotNull UseOnContext context) {
		InteractionResult result = super.useOn(context);
		if (result.consumesAction()) {
			Level world = context.getLevel();
			BlockPos pos = context.getClickedPos();
			BlockState state = world.getBlockState(pos);
			if (state.hasProperty(FarmlandBlock.MOISTURE)) {
				world.setBlockAndUpdate(pos, state.setValue(FarmlandBlock.MOISTURE, 7));
			}
		}
		return result;
	}
}
