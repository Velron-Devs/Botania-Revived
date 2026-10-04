package velrondevs.botania.api.mana.spark;

import net.minecraft.world.item.ItemStack;

public interface SparkAttachable {

	boolean canAttachSpark(ItemStack stack);

	default void attachSpark(ManaSpark entity) {}

	int getAvailableSpaceForMana();

	ManaSpark getAttachedSpark();

	boolean areIncomingTranfersDone();

}
