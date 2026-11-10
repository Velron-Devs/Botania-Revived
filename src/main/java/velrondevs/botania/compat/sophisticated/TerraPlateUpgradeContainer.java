package velrondevs.botania.compat.sophisticated;

import net.minecraft.world.entity.player.Player;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;

public class TerraPlateUpgradeContainer extends StationUpgradeContainer<TerraPlateUpgradeItem.Wrapper, TerraPlateUpgradeContainer> {
	public static final UpgradeContainerType<TerraPlateUpgradeItem.Wrapper, TerraPlateUpgradeContainer> TYPE = new UpgradeContainerType<>(TerraPlateUpgradeContainer::new);

	public TerraPlateUpgradeContainer(Player player, int containerId, TerraPlateUpgradeItem.Wrapper wrapper, UpgradeContainerType<TerraPlateUpgradeItem.Wrapper, TerraPlateUpgradeContainer> type) {
		super(player, containerId, wrapper, type);
	}
}
