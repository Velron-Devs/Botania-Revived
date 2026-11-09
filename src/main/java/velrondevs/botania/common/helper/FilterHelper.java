package velrondevs.botania.common.helper;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.random.Weighted;
import net.minecraft.world.item.component.TypedEntityData;
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
				List<ItemStack> containedItems = containerContents.nonEmptyItemCopyStream().toList();
				if (!containedItems.isEmpty()) {
					return containedItems;
				}
			}
			CompoundTag tag;
			if (filterStack.getItem() instanceof BlockItem) {
				TypedEntityData<?> data = filterStack.get(DataComponents.BLOCK_ENTITY_DATA);
				tag = data == null ? null : data.copyTagWithoutId();
			} else {
				CustomData data = filterStack.get(DataComponents.CUSTOM_DATA);
				tag = data == null ? null : data.copyTag();
			}
			if (tag != null && tag.contains(ITEMS_TAG)) {

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
			ListTag contents = tag.getListOrEmpty(ITEMS_TAG);
			List<ItemStack> items = new ArrayList<>(contents.size());
			for (int i = 0; i < contents.size(); i++) {
				CompoundTag entry = contents.getCompoundOrEmpty(i);
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

	public record WeightedItemStack(ItemStack stack, int weight) {
		public static WeightedItemStack of(ItemStack stack, int weight) {
			return new WeightedItemStack(stack, weight);
		}

		public int getWeight() {
			return weight;
		}

		@NotNull
		public Weighted<WeightedItemStack> weighted() {
			return new Weighted<>(this, weight);
		}
	}
}
