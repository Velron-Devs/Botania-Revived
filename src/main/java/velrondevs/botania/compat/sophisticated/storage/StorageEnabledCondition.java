package velrondevs.botania.compat.sophisticated.storage;

import com.mojang.serialization.MapCodec;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.conditions.ICondition;

import velrondevs.botania.common.lib.LibMisc;

public record StorageEnabledCondition() implements ICondition {
	public static final Identifier ID = Identifier.fromNamespaceAndPath(LibMisc.MOD_ID, "storage_enabled");
	public static final StorageEnabledCondition INSTANCE = new StorageEnabledCondition();
	public static final MapCodec<StorageEnabledCondition> CODEC = MapCodec.unit(INSTANCE);

	@Override
	public boolean test(IContext context) {
		return StorageSwitch.enabled();
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}

	@Override
	public String toString() {
		return "storage_enabled";
	}
}
