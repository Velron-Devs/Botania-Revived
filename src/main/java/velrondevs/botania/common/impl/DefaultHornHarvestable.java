package velrondevs.botania.common.impl;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.block.HornHarvestable;

public class DefaultHornHarvestable implements HornHarvestable {
	public static final HornHarvestable INSTANCE = new DefaultHornHarvestable();

	@Override
	public boolean canHornHarvest(Level world, BlockPos pos, ItemStack stack, EnumHornType hornType, @Nullable LivingEntity living) {
		return false;
	}
}
