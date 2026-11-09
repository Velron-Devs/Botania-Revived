package velrondevs.botania.common.block.block_entity;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class BlockEntityNbt {
	private BlockEntityNbt() {}

	public static Tag saveOptional(ItemStack stack, HolderLookup.Provider registries) {
		if (stack.isEmpty()) {
			return new CompoundTag();
		}
		return ItemStack.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), stack).result().orElseGet(CompoundTag::new);
	}

	public static Tag save(ItemStack stack, HolderLookup.Provider registries, CompoundTag into) {
		Tag tag = saveOptional(stack, registries);
		if (tag instanceof CompoundTag cmp) {
			into.merge(cmp);
		}
		return into;
	}

	public static ItemStack parseOptional(HolderLookup.Provider registries, @Nullable Tag tag) {
		if (tag == null || (tag instanceof CompoundTag cmp && cmp.isEmpty())) {
			return ItemStack.EMPTY;
		}
		return ItemStack.OPTIONAL_CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag).result().orElse(ItemStack.EMPTY);
	}

	public static void putUUID(CompoundTag tag, String key, UUID uuid) {
		tag.store(key, UUIDUtil.CODEC, uuid);
	}

	public static boolean hasUUID(CompoundTag tag, String key) {
		return tag.read(key, UUIDUtil.CODEC).isPresent();
	}

	@Nullable
	public static UUID getUUID(CompoundTag tag, String key) {
		return tag.read(key, UUIDUtil.CODEC).orElse(null);
	}

	public static void putComponent(CompoundTag tag, String key, Component component, HolderLookup.Provider registries) {
		tag.store(key, ComponentSerialization.CODEC, registries.createSerializationContext(NbtOps.INSTANCE), component);
	}

	@Nullable
	public static Component getComponent(CompoundTag tag, String key, HolderLookup.Provider registries) {
		return tag.read(key, ComponentSerialization.CODEC, registries.createSerializationContext(NbtOps.INSTANCE)).orElse(null);
	}

	public static void saveAllItems(CompoundTag tag, NonNullList<ItemStack> items, HolderLookup.Provider registries) {
		ListTag list = new ListTag();
		for (int i = 0; i < items.size(); i++) {
			ItemStack stack = items.get(i);
			if (!stack.isEmpty()) {
				Tag encoded = saveOptional(stack, registries);
				if (encoded instanceof CompoundTag cmp) {
					cmp.putByte("Slot", (byte) i);
					list.add(cmp);
				}
			}
		}
		tag.put("Items", list);
	}

	public static void loadAllItems(CompoundTag tag, NonNullList<ItemStack> items, HolderLookup.Provider registries) {
		ListTag list = tag.getListOrEmpty("Items");
		for (int i = 0; i < list.size(); i++) {
			CompoundTag cmp = list.getCompoundOrEmpty(i);
			int slot = cmp.getByteOr("Slot", (byte) 0) & 255;
			if (slot < items.size()) {
				CompoundTag copy = cmp.copy();
				copy.remove("Slot");
				items.set(slot, parseOptional(registries, copy));
			}
		}
	}
}
