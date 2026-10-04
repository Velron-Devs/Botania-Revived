package velrondevs.botania.common.block.block_entity.red_string;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.api.block_entity.SpecialFlowerBlockEntity;
import velrondevs.botania.registry.BotaniaBlockEntities;

public class RedStringSpooferBlockEntity extends RedStringBlockEntity {
	public RedStringSpooferBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.RED_STRING_RELAY, pos, state);
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		if (pos.equals(getBlockPos().above())) {
			return false;
		}

		Block block = level.getBlockState(pos).getBlock();
		if (isValidPlant(block)) {
			BlockEntity tile = level.getBlockEntity(pos);
			return !(tile instanceof SpecialFlowerBlockEntity);
		}
		return false;

	}

	private static boolean isValidPlant(Block block) {
		if (block instanceof FlowerPotBlock flowerPot) {
			block = flowerPot.getPotted();
		}
		return block instanceof FlowerBlock || block instanceof MushroomBlock || block instanceof FungusBlock || block instanceof DoublePlantBlock;
	}

}
