package velrondevs.botania.api.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.ServiceUtil;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiFunction;

public interface CorporeaHelper {
	CorporeaHelper INSTANCE = ServiceUtil.findService(CorporeaHelper.class, () -> new CorporeaHelper() {});

	static CorporeaHelper instance() {
		return INSTANCE;
	}

	default Set<CorporeaNode> getNodesOnNetwork(CorporeaSpark spark) {
		return Collections.emptySet();
	}

	default CorporeaRequestMatcher createMatcher(ItemStack stack, boolean checkNBT) {
		return CorporeaRequestMatcher.Dummy.INSTANCE;
	}

	default CorporeaRequestMatcher createMatcher(String name) {
		return CorporeaRequestMatcher.Dummy.INSTANCE;
	}

	default CorporeaResult requestItem(CorporeaRequestMatcher matcher, int itemCount, CorporeaSpark spark, @Nullable LivingEntity requestor, boolean doit) {
		return CorporeaResult.Dummy.INSTANCE;
	}

	@Nullable
	default CorporeaSpark getSparkForBlock(Level world, BlockPos pos) {
		return null;
	}

	default boolean doesBlockHaveSpark(Level world, BlockPos pos) {
		return getSparkForBlock(world, pos) != null;
	}

	default int signalStrengthForRequestSize(int requestSize) {
		return 0;
	}

	default <T extends CorporeaRequestMatcher> void registerRequestMatcher(ResourceLocation id, Class<T> clazz, BiFunction<CompoundTag, HolderLookup.Provider, T> deserializer) {}
}
