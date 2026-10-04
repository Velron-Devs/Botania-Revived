package velrondevs.botania.common.item.equipment.bauble;

public class NimbusAmuletItem extends CirrusAmuletItem {

	public NimbusAmuletItem(Properties props) {
		super(props);
	}

	@Override
	public int getMaxAllowedJumps() {
		return 3;
	}
}
