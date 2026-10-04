package velrondevs.botania.common.item;

import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.block.FloatingFlower.IslandType;

public interface FloatingFlowerVariant {
	IslandType getIslandType(ItemStack stack);
}
