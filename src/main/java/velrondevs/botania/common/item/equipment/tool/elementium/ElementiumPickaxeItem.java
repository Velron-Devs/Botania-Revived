package velrondevs.botania.common.item.equipment.tool.elementium;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.common.item.equipment.tool.manasteel.ManasteelPickaxeItem;
import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.registry.BotaniaItems;

public class ElementiumPickaxeItem extends ManasteelPickaxeItem {

	public ElementiumPickaxeItem(Properties props) {
		super(BotaniaAPI.instance().getElementiumItemTier(), props, -2.8F);
	}

	public static boolean shouldFilterOut(Entity e, ItemStack tool, ItemStack drop) {
		if (!tool.isEmpty() && (tool.is(BotaniaItems.elementiumPick)
				|| tool.is(BotaniaItems.terraPick) && TerraShattererItem.isTipped(tool))) {
			return !drop.isEmpty() && (isDisposable(drop) || isSemiDisposable(drop) && !e.isShiftKeyDown());
		}
		return false;
	}

	private static boolean isDisposable(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}

		return stack.is(BotaniaTags.Items.DISPOSABLE);
	}

	private static boolean isSemiDisposable(ItemStack stack) {
		return stack.is(BotaniaTags.Items.SEMI_DISPOSABLE);
	}
}
