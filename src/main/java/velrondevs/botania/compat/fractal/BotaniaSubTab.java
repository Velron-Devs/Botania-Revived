package velrondevs.botania.compat.fractal;

import de.dafuqs.fractal.api.CreativeSubTab;
import de.dafuqs.fractal.interfaces.ICreativeTabParent;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;

import velrondevs.botania.registry.BotaniaCreativeSections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

final class BotaniaSubTab extends CreativeSubTab {
	private final BotaniaCreativeSections.Section section;
	private final boolean collectsLeftovers;

	BotaniaSubTab(CreativeModeTab parent, ResourceLocation id, Component title, BotaniaCreativeSections.Section section, boolean collectsLeftovers) {
		super(parent, id, title, (params, output) -> section.fill(output), DEFAULT_STYLE);
		this.section = section;
		this.collectsLeftovers = collectsLeftovers;
	}

	@Override
	public ItemStack getIconItem() {
		return section.icon();
	}

	Collection<ItemStack> ownItems() {
		return super.getDisplayItems();
	}

	@Override
	public Collection<ItemStack> getDisplayItems() {
		Collection<ItemStack> own = ownItems();
		if (!collectsLeftovers) {
			return own;
		}
		Set<ItemStack> assigned = ItemStackLinkedSet.createTypeAndComponentsSet();
		assigned.addAll(own);
		for (CreativeSubTab sibling : ((ICreativeTabParent) (Object) getParent()).fractal$getChildren()) {
			if (sibling != this) {
				assigned.addAll(sibling.getDisplayItems());
			}
		}
		List<ItemStack> result = new ArrayList<>(own);
		for (ItemStack stack : getParent().getSearchTabDisplayItems()) {
			if (!assigned.contains(stack)) {
				result.add(stack);
			}
		}
		return result;
	}
}
