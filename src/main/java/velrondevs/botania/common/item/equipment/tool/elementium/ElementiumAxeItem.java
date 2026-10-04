package velrondevs.botania.common.item.equipment.tool.elementium;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.common.item.equipment.tool.manasteel.ManasteelAxeItem;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class ElementiumAxeItem extends ManasteelAxeItem {
	public static final ResourceLocation BEHEADING_LOOT_TABLE = prefix("elementium_axe_beheading");

	public ElementiumAxeItem(Properties props) {
		super(BotaniaAPI.instance().getElementiumItemTier(), 6F, -3.1F, props);
	}

	@Override
	public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
		return enchantment.is(Enchantments.LOOTING) || super.isPrimaryItemFor(stack, enchantment);
	}

	@Override
	public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
		return enchantment.is(Enchantments.LOOTING) || super.supportsEnchantment(stack, enchantment);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {

		stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
		return true;
	}

}
