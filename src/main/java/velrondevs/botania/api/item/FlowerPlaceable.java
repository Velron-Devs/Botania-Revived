package velrondevs.botania.api.item;

import net.minecraft.world.item.context.BlockPlaceContext;

import velrondevs.botania.api.block_entity.SpecialFlowerBlockEntity;

public interface FlowerPlaceable {

	boolean tryPlace(SpecialFlowerBlockEntity flower, BlockPlaceContext ctx);
}
