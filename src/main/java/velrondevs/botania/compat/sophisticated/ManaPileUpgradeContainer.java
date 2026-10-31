package velrondevs.botania.compat.sophisticated;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedcore.common.gui.SlotSuppliedHandler;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;

import java.util.function.Supplier;

public class ManaPileUpgradeContainer extends UpgradeContainerBase<ManaPileUpgradeItem.Wrapper, ManaPileUpgradeContainer> {
	public static final UpgradeContainerType<ManaPileUpgradeItem.Wrapper, ManaPileUpgradeContainer> TYPE = new UpgradeContainerType<>(ManaPileUpgradeContainer::new);

	public ManaPileUpgradeContainer(Player player, int containerId, ManaPileUpgradeItem.Wrapper wrapper, UpgradeContainerType<ManaPileUpgradeItem.Wrapper, ManaPileUpgradeContainer> type) {
		super(player, containerId, wrapper, type);
		Supplier<IItemHandler> handler = supplyFromWrapper(pile -> pile.getSlotInventory());
		slots.add(new SlotSuppliedHandler(handler, 0, -100, -100));
	}

	@Override
	public void handlePacket(CompoundTag data) {}

	public int getMana() {
		return Math.max(0, getUpgradeStack().getOrDefault(SophisticatedComponents.MANA, 0));
	}

	public int getCapacity() {
		return upgradeWrapper.getCapacity();
	}

	public boolean isPileEnabled() {
		return upgradeWrapper.isEnabled();
	}
}
