package velrondevs.botania.common.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public interface CustomCreativeTabContents {

	void addToCreativeTab(Item me, CreativeModeTab.Output output);

	static void addItem(Item item, CreativeModeTab.Output output) {
		if (item instanceof CustomCreativeTabContents cc) {
			cc.addToCreativeTab(item, output);
		} else if (item instanceof BlockItem bi && bi.getBlock() instanceof CustomCreativeTabContents cc) {
			cc.addToCreativeTab(item, output);
		} else {
			output.accept(item);
		}
	}
}
