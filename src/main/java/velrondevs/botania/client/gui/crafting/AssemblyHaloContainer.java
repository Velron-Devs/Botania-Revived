package velrondevs.botania.client.gui.crafting;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;

import org.jetbrains.annotations.NotNull;

public class AssemblyHaloContainer extends CraftingMenu {

	public AssemblyHaloContainer(int windowId, Inventory playerInv, ContainerLevelAccess wp) {
		super(windowId, playerInv, wp);
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		return true;
	}
}
