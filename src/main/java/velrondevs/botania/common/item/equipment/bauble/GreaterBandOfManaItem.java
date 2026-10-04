package velrondevs.botania.common.item.equipment.bauble;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.common.item.CustomCreativeTabContents;

public class GreaterBandOfManaItem extends BandOfManaItem implements CustomCreativeTabContents {

	private static final int MAX_MANA = BandOfManaItem.MAX_MANA * 4;

	public GreaterBandOfManaItem(Properties props) {
		super(props);
	}

	@Override
	public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
		output.accept(this);

		ItemStack full = new ItemStack(this);
		setMana(full, MAX_MANA);
		output.accept(full);
	}

	public static class GreaterManaItemImpl extends ManaItemImpl {
		public GreaterManaItemImpl(ItemStack stack) {
			super(stack);
		}

		@Override
		public int getMaxMana() {
			return MAX_MANA * stack.getCount();
		}
	}
}
