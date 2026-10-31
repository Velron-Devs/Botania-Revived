package velrondevs.botania.compat.sophisticated;

import net.minecraft.world.entity.player.Player;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;

public class RunicAltarUpgradeContainer extends StationUpgradeContainer<RunicAltarUpgradeItem.Wrapper, RunicAltarUpgradeContainer> {
	public static final UpgradeContainerType<RunicAltarUpgradeItem.Wrapper, RunicAltarUpgradeContainer> TYPE = new UpgradeContainerType<>(RunicAltarUpgradeContainer::new);

	public RunicAltarUpgradeContainer(Player player, int containerId, RunicAltarUpgradeItem.Wrapper wrapper, UpgradeContainerType<RunicAltarUpgradeItem.Wrapper, RunicAltarUpgradeContainer> type) {
		super(player, containerId, wrapper, type);
	}
}
