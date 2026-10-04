package velrondevs.botania.mixin;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.extensions.IBlockExtension;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import velrondevs.botania.common.block.BotaniaGrassBlock;

@Mixin(BotaniaGrassBlock.class)
public abstract class BotaniaGrassBlockToolModificationMixin implements IBlockExtension {
	@Override
	public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context,
			ItemAbility toolAction, boolean simulate) {
		if (toolAction == ItemAbilities.HOE_TILL && HoeItem.onlyIfAirAbove(context)) {
			return Blocks.FARMLAND.defaultBlockState();
		} else if (toolAction == ItemAbilities.SHOVEL_FLATTEN) {
			return Blocks.DIRT_PATH.defaultBlockState();
		}
		return null;
	}
}
