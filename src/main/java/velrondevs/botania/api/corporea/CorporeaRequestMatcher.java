package velrondevs.botania.api.corporea;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public interface CorporeaRequestMatcher extends Predicate<ItemStack> {

	@Override
	default boolean test(ItemStack stack) {
		return false;
	}

	default void writeToNBT(CompoundTag tag, HolderLookup.Provider registries) {}

	default Component getRequestName() {
		return Component.literal("missingno");
	}

	enum Dummy implements CorporeaRequestMatcher {
		INSTANCE
	}
}
