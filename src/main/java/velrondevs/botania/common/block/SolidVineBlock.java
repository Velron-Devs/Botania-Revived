package velrondevs.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.mixin.BlockPropertiesAccessor;

public class SolidVineBlock extends VineBlock {

	public SolidVineBlock(Properties builder) {
		super(unsetVanillaProps(builder));
	}

	private static Properties unsetVanillaProps(Properties props) {

		((BlockPropertiesAccessor) props).botania_setHasCollision(true);
		((BlockPropertiesAccessor) props).botania_setIsRandomlyTicking(false);
		((BlockPropertiesAccessor) props).botania_setReplaceable(false);
		return props;
	}

	@NotNull
	@Override
	public ItemStack getCloneItemStack(@NotNull LevelReader world, @NotNull BlockPos pos, @NotNull BlockState state) {
		return new ItemStack(Blocks.VINE);
	}
}
