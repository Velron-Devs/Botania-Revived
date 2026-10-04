package velrondevs.botania.api.mana;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface BasicLensItem extends LensEffectItem {

	int getLensColor(ItemStack stack, Level level);

	boolean canCombineLenses(ItemStack sourceLens, ItemStack compositeLens);

	ItemStack getCompositeLens(ItemStack stack);

	ItemStack setCompositeLens(ItemStack sourceLens, ItemStack compositeLens);

}
