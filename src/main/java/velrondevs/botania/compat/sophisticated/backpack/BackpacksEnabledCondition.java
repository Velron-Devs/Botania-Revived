package velrondevs.botania.compat.sophisticated.backpack;

import com.mojang.serialization.MapCodec;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

import velrondevs.botania.common.lib.LibMisc;

public record BackpacksEnabledCondition() implements ICondition {
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "backpacks_enabled");
	public static final BackpacksEnabledCondition INSTANCE = new BackpacksEnabledCondition();
	public static final MapCodec<BackpacksEnabledCondition> CODEC = MapCodec.unit(INSTANCE);

	@Override
	public boolean test(IContext context) {
		return BackpackSwitch.enabled();
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}

	@Override
	public String toString() {
		return "backpacks_enabled";
	}
}
