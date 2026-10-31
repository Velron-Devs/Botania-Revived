package velrondevs.botania.compat.sophisticated;

import net.minecraft.world.entity.player.Player;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;

public class ElvenPortalUpgradeContainer extends StationUpgradeContainer<ElvenPortalUpgradeItem.Wrapper, ElvenPortalUpgradeContainer> {
	public static final UpgradeContainerType<ElvenPortalUpgradeItem.Wrapper, ElvenPortalUpgradeContainer> TYPE = new UpgradeContainerType<>(ElvenPortalUpgradeContainer::new);

	public ElvenPortalUpgradeContainer(Player player, int containerId, ElvenPortalUpgradeItem.Wrapper wrapper, UpgradeContainerType<ElvenPortalUpgradeItem.Wrapper, ElvenPortalUpgradeContainer> type) {
		super(player, containerId, wrapper, type);
	}
}
