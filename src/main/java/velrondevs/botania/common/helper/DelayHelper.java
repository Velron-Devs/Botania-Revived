package velrondevs.botania.common.helper;

import net.minecraft.world.entity.item.ItemEntity;

import velrondevs.botania.api.block_entity.FunctionalFlowerBlockEntity;
import velrondevs.botania.api.block_entity.GeneratingFlowerBlockEntity;
import velrondevs.botania.api.block_entity.SpecialFlowerBlockEntity;
import velrondevs.botania.xplat.XplatAbstractions;

public class DelayHelper {
	public static final int FUNCTIONAL_INHERENT_DELAY = 60;
	public static final int GENERATING_INHERENT_DELAY = FUNCTIONAL_INHERENT_DELAY - 1;

	public static boolean canInteractWithImmediate(SpecialFlowerBlockEntity tile, ItemEntity item) {
		return item.isAlive() && !item.getItem().isEmpty()
				&& XplatAbstractions.INSTANCE.itemFlagsComponent(item).timeCounter > tile.getModulatedDelay();
	}

	public static boolean canInteractWith(SpecialFlowerBlockEntity tile, ItemEntity item) {
		if (!item.isAlive() || item.getItem().isEmpty()) {
			return false;
		}
		var flags = XplatAbstractions.INSTANCE.itemFlagsComponent(item);
		int inherentDelay = 0;
		if (tile instanceof FunctionalFlowerBlockEntity) {
			inherentDelay = FUNCTIONAL_INHERENT_DELAY;
		} else if (tile instanceof GeneratingFlowerBlockEntity) {
			inherentDelay = GENERATING_INHERENT_DELAY;
		}
		return flags.timeCounter > inherentDelay + tile.getModulatedDelay();
	}
}
