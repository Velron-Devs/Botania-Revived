package velrondevs.botania.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface HornHarvestable {

	boolean canHornHarvest(Level level, BlockPos pos, ItemStack stack, EnumHornType hornType, @Nullable LivingEntity user);

	default boolean hasSpecialHornHarvest(Level level, BlockPos pos, ItemStack stack, EnumHornType hornType, @Nullable LivingEntity user) {
		return false;
	}

	default void harvestByHorn(Level level, BlockPos pos, ItemStack stack, EnumHornType hornType, @Nullable LivingEntity user) {}

	enum EnumHornType {

		WILD,

		CANOPY,

		COVERING
	}

}
