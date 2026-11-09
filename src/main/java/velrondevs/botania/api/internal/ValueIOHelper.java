package velrondevs.botania.api.internal;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import org.jetbrains.annotations.Nullable;

public final class ValueIOHelper {
	private static final MapCodec<CompoundTag> WHOLE_TAG = MapCodec.assumeMapUnsafe(CompoundTag.CODEC);
	private static RegistryAccess builtin;

	private ValueIOHelper() {}

	@SuppressWarnings("deprecation")
	public static CompoundTag readAll(ValueInput input) {
		return input.read(WHOLE_TAG).map(CompoundTag::copy).orElseGet(CompoundTag::new);
	}

	public static void writeAll(ValueOutput output, CompoundTag tag) {
		output.store(tag);
	}

	public static HolderLookup.Provider registries(@Nullable Level level) {
		if (level != null) {
			return level.registryAccess();
		}
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server != null) {
			return server.registryAccess();
		}
		if (builtin == null) {
			builtin = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		}
		return builtin;
	}
}
