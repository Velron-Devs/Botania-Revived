package velrondevs.botania.compat.sophisticated;

import net.minecraft.world.entity.player.Player;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;

public class PetalApothecaryUpgradeContainer extends StationUpgradeContainer<PetalApothecaryUpgradeItem.Wrapper, PetalApothecaryUpgradeContainer> {
	public static final UpgradeContainerType<PetalApothecaryUpgradeItem.Wrapper, PetalApothecaryUpgradeContainer> TYPE = new UpgradeContainerType<>(PetalApothecaryUpgradeContainer::new);

	public PetalApothecaryUpgradeContainer(Player player, int containerId, PetalApothecaryUpgradeItem.Wrapper wrapper, UpgradeContainerType<PetalApothecaryUpgradeItem.Wrapper, PetalApothecaryUpgradeContainer> type) {
		super(player, containerId, wrapper, type);
	}
}
