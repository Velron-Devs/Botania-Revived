package velrondevs.botania.common.item.equipment.tool.elementium;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.common.item.equipment.tool.BlockStartBreakItem;
import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.common.item.equipment.tool.ToolCommons;
import velrondevs.botania.common.item.equipment.tool.manasteel.ManasteelShovelItem;

public class ElementiumShovelItem extends ManasteelShovelItem implements BlockStartBreakItem {

	public ElementiumShovelItem(Properties props) {
		super(BotaniaAPI.instance().getElementiumItemTier(), props);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
		Level world = player.level();
		BlockState blockState = world.getBlockState(pos);
		if (this.getDestroySpeed(stack, blockState) <= 1.0F) {
			return false;
		}

		Block blk = blockState.getBlock();
		if (blk instanceof FallingBlock) {
			ToolCommons.removeBlocksInIteration(player, stack, world, pos, new Vec3i(0, -12, 0),
					new Vec3i(1, 12, 1),
					state -> state.is(blk));
		}

		return false;
	}

}
