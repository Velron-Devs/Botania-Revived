package velrondevs.botania.common.item.equipment.tool.bow;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.common.item.equipment.CustomDamageItem;
import velrondevs.botania.common.item.equipment.tool.ToolCommons;
import velrondevs.botania.registry.BotaniaItems;

import java.util.function.Consumer;

public class LivingwoodBowItem extends BowItem implements CustomDamageItem {
	public static final int MANA_PER_DAMAGE = 40;

	public LivingwoodBowItem(Properties builder) {
		super(builder);
	}

	public float chargeVelocityMultiplier() {
		return 1F;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if (!world.isClientSide && entity instanceof Player player && stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExactForTool(stack, player, MANA_PER_DAMAGE * 2, true)) {
			stack.setDamageValue(stack.getDamageValue() - 1);
		}
	}

	@Override
	public boolean isValidRepairItem(ItemStack bow, ItemStack material) {
		return material.is(BotaniaItems.livingwoodTwig) || super.isValidRepairItem(bow, material);
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<Item> onBroken) {
		return ToolCommons.damageItemIfPossible(stack, amount, entity, MANA_PER_DAMAGE);
	}
}
