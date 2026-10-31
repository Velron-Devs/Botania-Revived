package velrondevs.botania.compat.sophisticated;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedcore.common.gui.SlotSuppliedHandler;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;

import java.util.function.Supplier;

public abstract class StationUpgradeContainer<W extends StationUpgradeWrapper<W, ?>, C extends StationUpgradeContainer<W, C>> extends UpgradeContainerBase<W, C> {
	protected StationUpgradeContainer(Player player, int containerId, W wrapper, UpgradeContainerType<W, C> type) {
		super(player, containerId, wrapper, type);
		Supplier<IItemHandler> handler = supplyFromWrapper(station -> station.getInventory());
		int count = wrapper.getInventory().getSlots();
		for (int slot = 0; slot < count; slot++) {
			slots.add(new SlotSuppliedHandler(handler, slot, -100, -100));
		}
	}

	@Override
	public void handlePacket(CompoundTag data) {}

	public StationState getState() {
		return getUpgradeStack().getOrDefault(SophisticatedComponents.STATION, StationState.EMPTY);
	}
}
