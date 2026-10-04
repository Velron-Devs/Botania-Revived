package velrondevs.botania.api.mana;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ManaItem {

	int getMana();

	int getMaxMana();

	void addMana(int mana);

	boolean canReceiveManaFromPool(BlockEntity pool);

	boolean canReceiveManaFromItem(ItemStack otherStack);

	boolean canExportManaToPool(BlockEntity pool);

	boolean canExportManaToItem(ItemStack otherStack);

	boolean isNoExport();

}
