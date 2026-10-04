package velrondevs.botania.common.block.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.registry.BotaniaBlocks;

public class FelPumpkinBehavior extends OptionalDispenseItemBehavior {
	@NotNull
	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		Level world = source.level();
		BlockPos blockpos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
		Block blockcarvedpumpkin = BotaniaBlocks.felPumpkin;
		this.setSuccess(false);
		if (world.isEmptyBlock(blockpos) && world.getBlockState(blockpos.below()).is(Blocks.IRON_BARS)
				&& world.getBlockState(blockpos.below(2)).is(Blocks.IRON_BARS))
		{
			this.setSuccess(true);
			if (!world.isClientSide) {
				world.setBlockAndUpdate(blockpos, blockcarvedpumpkin.defaultBlockState());
			}

			stack.shrink(1);
		}

		return stack;
	}
}
