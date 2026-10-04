package velrondevs.botania.common.item.equipment.tool.manasteel;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.common.item.equipment.CustomDamageItem;
import velrondevs.botania.common.item.equipment.tool.ToolCommons;

import java.util.function.Consumer;

public class ManasteelSwordItem extends SwordItem implements CustomDamageItem {

	public static final int MANA_PER_DAMAGE = 60;

	public ManasteelSwordItem(Properties props) {
		this(BotaniaAPI.instance().getManasteelItemTier(), props);
	}

	public ManasteelSwordItem(Tier mat, Properties props) {
		this(mat, 3, -2.4F, props);
	}

	public ManasteelSwordItem(Tier mat, int attackDamage, float attackSpeed, Properties props) {
		this(mat, createAttributes(mat, attackDamage, attackSpeed), props);
	}

	public ManasteelSwordItem(Tier mat, ItemAttributeModifiers attributes, Properties props) {
		super(mat, props.attributes(attributes));
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<Item> onBroken) {
		int manaPerDamage = ((ManasteelSwordItem) stack.getItem()).getManaPerDamage();
		return ToolCommons.damageItemIfPossible(stack, amount, entity, manaPerDamage);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if (!world.isClientSide && entity instanceof Player player && stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExactForTool(stack, player, getManaPerDamage() * 2, true)) {
			stack.setDamageValue(stack.getDamageValue() - 1);
		}
	}

	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}
}
