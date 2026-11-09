package velrondevs.botania.common.item;

import net.minecraft.nbt.NbtOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Optional;
import java.util.stream.Stream;

public final class ItemStackSerialization {
	private static final RegistryAccess STATIC_REGISTRIES = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);

	private static final HolderLookup.Provider RESOLVING_REGISTRIES = new HolderLookup.Provider() {
		@Override
		public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
			return STATIC_REGISTRIES.listRegistryKeys();
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
			HolderLookup.RegistryLookup<T> resolved = CommonHooks.resolveLookup((ResourceKey<? extends Registry<T>>) key);
			if (resolved != null) {
				return Optional.of(resolved);
			}
			return STATIC_REGISTRIES.<T>lookup(key).map(registry -> (HolderLookup.RegistryLookup<T>) registry);
		}
	};

	public static HolderLookup.Provider registries() {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server != null) {
			return server.registryAccess();
		}
		return RESOLVING_REGISTRIES;
	}

	public static ItemStack read(CompoundTag tag) {
		return read(registries(), tag);
	}

	public static ItemStack read(HolderLookup.Provider registries, CompoundTag tag) {
		return ItemStack.OPTIONAL_CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag).result().orElse(ItemStack.EMPTY);
	}

	public static CompoundTag write(ItemStack stack) {
		return write(registries(), stack);
	}

	public static CompoundTag write(HolderLookup.Provider registries, ItemStack stack) {
		if (stack.isEmpty()) {
			return new CompoundTag();
		}
		Tag tag = ItemStack.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), stack).result().orElse(null);
		return tag instanceof CompoundTag cmp ? cmp : new CompoundTag();
	}

	private ItemStackSerialization() {}
}
