package velrondevs.botania.common.integration.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.corporea.CorporeaNode;
import velrondevs.botania.api.corporea.CorporeaNodeDetector;
import velrondevs.botania.api.corporea.CorporeaSpark;
import velrondevs.botania.common.impl.corporea.SidedVanillaCorporeaNode;
import velrondevs.botania.common.impl.corporea.VanillaCorporeaNode;

public class VanillaNodeDetector implements CorporeaNodeDetector {
	@Nullable
	@Override
	public CorporeaNode getNode(Level level, CorporeaSpark spark) {

		Container container = null;
		BlockPos blockPos = spark.getAttachPos();
		BlockState blockState = level.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if (block instanceof WorldlyContainerHolder worldlyContainer) {
			container = worldlyContainer.getContainer(blockState, level, blockPos);
		} else if (blockState.hasBlockEntity() && level.getBlockEntity(blockPos) instanceof Container beContainer) {
			container = beContainer;
			if (container instanceof ChestBlockEntity && block instanceof ChestBlock chest) {
				container = ChestBlock.getContainer(chest, blockState, level, blockPos, true);
			}
		}

		if (container instanceof WorldlyContainer worldlyContainer) {
			return new SidedVanillaCorporeaNode(level, spark.getAttachPos(), spark, worldlyContainer, Direction.UP);
		} else if (container != null) {
			return new VanillaCorporeaNode(level, spark.getAttachPos(), container, spark);
		}
		return null;
	}
}
