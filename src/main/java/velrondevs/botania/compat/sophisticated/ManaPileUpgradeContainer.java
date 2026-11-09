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
	private static final String DISCHARGE_KEY = "discharge";
	private static final String INFUSION_KEY = "infusion";

	public ManaPileUpgradeContainer(Player player, int containerId, ManaPileUpgradeItem.Wrapper wrapper, UpgradeContainerType<ManaPileUpgradeItem.Wrapper, ManaPileUpgradeContainer> type) {
		super(player, containerId, wrapper, type);
		Supplier<IItemHandler> handler = supplyFromWrapper(pile -> pile.getSlotInventory());
		slots.add(new SlotSuppliedHandler(handler, ManaPileUpgradeItem.Wrapper.SOURCE_SLOT, -100, -100));
		slots.add(new SlotSuppliedHandler(handler, ManaPileUpgradeItem.Wrapper.CATALYST_SLOT, -100, -100));
		slots.add(new SlotSuppliedHandler(handler, ManaPileUpgradeItem.Wrapper.INPUT_SLOT, -100, -100));
		slots.add(new SlotSuppliedHandler(handler, ManaPileUpgradeItem.Wrapper.OUTPUT_SLOT, -100, -100));
	}

	@Override
	public void handlePacket(CompoundTag data) {
		if (data.contains(DISCHARGE_KEY)) {
			upgradeWrapper.setDischarging(data.getBoolean(DISCHARGE_KEY));
		}
		if (data.contains(INFUSION_KEY)) {
			upgradeWrapper.setInfusing(data.getBoolean(INFUSION_KEY));
		}
	}

	public boolean isDischarging() {
		return upgradeWrapper.isDischarging();
	}

	public void setDischarging(boolean discharging) {
		upgradeWrapper.setDischarging(discharging);
		sendBooleanToServer(DISCHARGE_KEY, discharging);
	}

	public boolean isInfusing() {
		return upgradeWrapper.isInfusing();
	}

	public void setInfusing(boolean infusing) {
		upgradeWrapper.setInfusing(infusing);
		sendBooleanToServer(INFUSION_KEY, infusing);
	}

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
