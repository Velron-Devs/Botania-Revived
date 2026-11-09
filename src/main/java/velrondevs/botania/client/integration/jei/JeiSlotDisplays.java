package velrondevs.botania.client.integration.jei;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

public final class JeiSlotDisplays {
	private JeiSlotDisplays() {}

	public static SlotDisplay of(List<ItemStack> stacks) {
		List<SlotDisplay> contents = stacks.stream()
				.filter(s -> !s.isEmpty())
				.<SlotDisplay>map(s -> new SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(s)))
				.toList();
		if (contents.isEmpty()) {
			return SlotDisplay.Empty.INSTANCE;
		}
		return contents.size() == 1 ? contents.get(0) : new SlotDisplay.Composite(contents);
	}

	public static List<SlotDisplay> ofAll(List<List<ItemStack>> inputs) {
		return inputs.stream().map(JeiSlotDisplays::of).toList();
	}
}
