package velrondevs.botania.capability;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.EmptyResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.block.block_entity.red_string.RedStringContainerBlockEntity;

public final class RedStringContainerCapProvider {
	public static ResourceHandler<ItemResource> getItemHandler(RedStringContainerBlockEntity container, @Nullable Direction side) {
		BlockEntity binding = container.getTileAtBinding();
		if (binding != null && binding.getLevel() != null) {
			ResourceHandler<ItemResource> handler = binding.getLevel().getCapability(Capabilities.Item.BLOCK,
					binding.getBlockPos(), binding.getBlockState(), binding, side);
			if (handler != null) {
				return handler;
			}
		}
		return EmptyResourceHandler.instance();
	}

	private RedStringContainerCapProvider() {}
}
