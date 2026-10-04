package velrondevs.botania.common.helper;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FilterHelper {

	public static final String ITEMS_TAG = "Items";

	public static List<ItemStack> getFilterItems(ItemFrame filterFrame) {
		ItemStack filterStack = filterFrame.getItem();
		if (filterStack.isEmpty()) {
			return List.of();
		}
		return filterFrame instanceof GlowItemFrame ? getFilterStacks(filterStack) : List.of(filterStack);
	}

	public static List<ItemStack> getFilterStacks(ItemStack filterStack) {
		if (filterStack.is(Items.BUNDLE)) {

			BundleContents bundleContents = filterStack.get(DataComponents.BUNDLE_CONTENTS);
			List<ItemStack> bundledItems = bundleContents == null ? List.of() : bundleContents.itemCopyStream().toList();
			if (!bundledItems.isEmpty()) {
				return bundledItems;
			}
		} else {

			ItemContainerContents containerContents = filterStack.get(DataComponents.CONTAINER);
			if (containerContents != null) {
				List<ItemStack> containedItems = containerContents.nonEmptyStream().map(ItemStack::copy).toList();
				if (!containedItems.isEmpty()) {
					return containedItems;
				}
			}
			CustomData data = filterStack.getItem() instanceof BlockItem
					? filterStack.get(DataComponents.BLOCK_ENTITY_DATA)
					: filterStack.get(DataComponents.CUSTOM_DATA);
			CompoundTag tag = data == null ? null : data.copyTag();
			if (tag != null && tag.contains(ITEMS_TAG, Tag.TAG_LIST)) {

				List<ItemStack> items = getItemStacks(tag);
				if (items != null) {
					return items;
				}
			}
		}
		return List.of(filterStack);
	}

	@Nullable
	private static List<ItemStack> getItemStacks(CompoundTag tag) {
		try {
			ListTag contents = tag.getList(ITEMS_TAG, CompoundTag.TAG_COMPOUND);
			List<ItemStack> items = new ArrayList<>(contents.size());
			for (int i = 0; i < contents.size(); i++) {
				CompoundTag entry = contents.getCompound(i);
				ItemStack stack = ItemStack.OPTIONAL_CODEC.parse(NbtOps.INSTANCE, entry).result().orElse(ItemStack.EMPTY);
				if (!stack.isEmpty()) {
					items.add(stack);
				}
			}
			if (!items.isEmpty()) {
				return items;
			}
		} catch (ClassCastException ce) {

		}
		return null;
	}

	public record WeightedItemStack(ItemStack stack, Weight weight) implements WeightedEntry {
		public static WeightedItemStack of(ItemStack stack, int weight) {
			return new WeightedItemStack(stack, Weight.of(weight));
		}

		@NotNull
		@Override
		public Weight getWeight() {
			return weight;
		}
	}
}
