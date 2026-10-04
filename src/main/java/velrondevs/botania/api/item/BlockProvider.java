package velrondevs.botania.api.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.Nullable;

public interface BlockProvider {

	boolean provideBlock(Player player, ItemStack requestor, Block block, boolean doit);

	int getBlockCount(Player player, ItemStack requestor, Block block);

	@Nullable
	default Block getProvidedBlock(Player player, ItemStack requestor) {
		return null;
	}
}
