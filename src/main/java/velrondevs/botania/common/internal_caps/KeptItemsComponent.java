package velrondevs.botania.common.internal_caps;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KeptItemsComponent extends SerializableComponent {
	private final List<ItemStack> stacks = new ArrayList<>();

	public void addAll(Collection<ItemStack> stack) {
		stacks.addAll(stack);
	}

	public List<ItemStack> getStacks() {
		return stacks;
	}

	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider registries) {
		stacks.clear();
		ListTag list = tag.getList("stacks", Tag.TAG_COMPOUND);
		for (Tag t : list) {
			stacks.add(ItemStack.parseOptional(registries, (CompoundTag) t));
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider registries) {
		ListTag list = new ListTag();
		for (ItemStack stack : stacks) {
			list.add(stack.saveOptional(registries));
		}
		tag.put("stacks", list);
	}
}
