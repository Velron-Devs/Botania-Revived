package velrondevs.botania.api.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.Event;

import java.util.List;

public class ElvenPortalUpdateEvent extends Event {

	private final BlockEntity portalTile;
	private final AABB aabb;
	private final boolean open;
	private final List<ItemStack> stacksInside;

	public ElvenPortalUpdateEvent(BlockEntity te, AABB aabb, boolean open, List<ItemStack> stacks) {
		portalTile = te;
		this.aabb = aabb;
		this.open = open;
		stacksInside = stacks;
	}

	public BlockEntity getPortalTile() {
		return portalTile;
	}

	public AABB getAabb() {
		return aabb;
	}

	public boolean isOpen() {
		return open;
	}

	public List<ItemStack> getStacksInside() {
		return stacksInside;
	}
}
