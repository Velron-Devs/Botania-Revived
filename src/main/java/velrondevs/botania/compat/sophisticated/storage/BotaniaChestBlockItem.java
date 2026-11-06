package velrondevs.botania.compat.sophisticated.storage;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.p3pp3rf1y.sophisticatedstorage.item.ChestBlockItem;

public class BotaniaChestBlockItem extends ChestBlockItem {
	public BotaniaChestBlockItem(Block block, Item.Properties properties) {
		super(block, properties);
	}

	@Override
	public void verifyComponentsAfterLoad(ItemStack stack) {
		super.verifyComponentsAfterLoad(stack);
		BotaniaStorageStacks.applyDefaultWood(stack);
	}
}
