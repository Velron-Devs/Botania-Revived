package velrondevs.botania.compat.fractal;

import de.dafuqs.fractal.api.CreativeSubTab;
import de.dafuqs.fractal.interfaces.ICreativeTabParent;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;

import velrondevs.botania.api.BotaniaRegistries;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.registry.BotaniaCreativeSections;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class FractalSubTabs {
	private FractalSubTabs() {}

	public static void register() {
		CreativeModeTab parent = BuiltInRegistries.CREATIVE_MODE_TAB.get(BotaniaRegistries.BOTANIA_TAB_KEY);
		if (parent == null) {
			return;
		}
		for (BotaniaCreativeSections.Section section : BotaniaCreativeSections.activeSections()) {
			ResourceLocation id = ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, section.id());
			CreativeSubTab tab = new BotaniaSubTab(parent, id, Component.translatable(section.titleKey()), section,
					BotaniaCreativeSections.MISC.equals(section.id()));
			CreativeSubTab.SUBTABS.add(tab);
		}
	}

	public static void fillParent(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
		CreativeModeTab parent = BuiltInRegistries.CREATIVE_MODE_TAB.get(BotaniaRegistries.BOTANIA_TAB_KEY);
		List<CreativeSubTab> children = parent == null ? List.of() : ((ICreativeTabParent) (Object) parent).fractal$getChildren();
		if (children.isEmpty()) {
			for (BotaniaCreativeSections.Section section : BotaniaCreativeSections.activeSections()) {
				section.fill(output);
			}
			return;
		}
		Set<ItemStack> added = ItemStackLinkedSet.createTypeAndComponentsSet();
		for (CreativeSubTab child : children) {
			child.buildContents(params);
			Collection<ItemStack> stacks = child instanceof BotaniaSubTab own ? own.ownItems() : child.getDisplayItems();
			for (ItemStack stack : stacks) {
				if (added.add(stack)) {
					output.accept(stack);
				}
			}
		}
	}
}
