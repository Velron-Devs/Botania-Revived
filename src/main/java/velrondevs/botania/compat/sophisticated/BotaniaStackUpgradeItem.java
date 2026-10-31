package velrondevs.botania.compat.sophisticated;

import net.p3pp3rf1y.sophisticatedcore.upgrades.stack.StackUpgradeItem;

public class BotaniaStackUpgradeItem extends StackUpgradeItem {
	public BotaniaStackUpgradeItem(double stackSizeMultiplier) {
		super(stackSizeMultiplier, UpgradeLimits.UNLIMITED);
	}
}
