package velrondevs.botania.compat.sophisticated;

import net.minecraft.world.item.Item;
import net.p3pp3rf1y.sophisticatedcore.upgrades.stack.StackUpgradeItem;

public class BotaniaStackUpgradeItem extends StackUpgradeItem {
	public BotaniaStackUpgradeItem(double stackSizeMultiplier) {
		super(stackSizeMultiplier, UpgradeLimits.UNLIMITED, new Item.Properties());
	}
}
