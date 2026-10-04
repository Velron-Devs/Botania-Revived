package velrondevs.botania.capability;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.EmptyItemHandler;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.block.block_entity.red_string.RedStringContainerBlockEntity;

public final class RedStringContainerCapProvider {
	public static IItemHandler getItemHandler(RedStringContainerBlockEntity container, @Nullable Direction side) {
		BlockEntity binding = container.getTileAtBinding();
		if (binding != null && binding.getLevel() != null) {
			IItemHandler handler = binding.getLevel().getCapability(Capabilities.ItemHandler.BLOCK,
					binding.getBlockPos(), binding.getBlockState(), binding, side);
			if (handler != null) {
				return handler;
			}
		}
		return EmptyItemHandler.INSTANCE;
	}

	private RedStringContainerCapProvider() {}
}
