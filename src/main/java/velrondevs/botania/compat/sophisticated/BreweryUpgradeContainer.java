package velrondevs.botania.compat.sophisticated;

import net.minecraft.world.entity.player.Player;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;

public class BreweryUpgradeContainer extends StationUpgradeContainer<BreweryUpgradeItem.Wrapper, BreweryUpgradeContainer> {
	public static final UpgradeContainerType<BreweryUpgradeItem.Wrapper, BreweryUpgradeContainer> TYPE = new UpgradeContainerType<>(BreweryUpgradeContainer::new);

	public BreweryUpgradeContainer(Player player, int containerId, BreweryUpgradeItem.Wrapper wrapper, UpgradeContainerType<BreweryUpgradeItem.Wrapper, BreweryUpgradeContainer> type) {
		super(player, containerId, wrapper, type);
	}
}
