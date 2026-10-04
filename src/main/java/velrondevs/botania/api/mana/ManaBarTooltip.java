package velrondevs.botania.api.mana;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.xplat.XplatAbstractions;

public class ManaBarTooltip implements TooltipComponent {
	private final float percentageFull;
	private final int pickLevel;

	public static ManaBarTooltip fromManaItem(ItemStack stack) {
		var manaItem = XplatAbstractions.INSTANCE.findManaItem(stack);
		if (manaItem != null) {
			return new ManaBarTooltip(getFractionForDisplay(manaItem));
		}
		throw new IllegalArgumentException("Item does not have the capability " + ManaItem.class.getName());
	}

	public static float getFractionForDisplay(ManaItem item) {
		return item.getMana() / (float) item.getMaxMana();
	}

	public ManaBarTooltip(float percentageFull) {
		this(percentageFull, -1);
	}

	public ManaBarTooltip(float percentageFull, int pickLevel) {
		this.percentageFull = percentageFull;
		this.pickLevel = pickLevel;
	}

	public float getPercentageFull() {
		return percentageFull;
	}

	public int getPickLevel() {
		return pickLevel;
	}
}
