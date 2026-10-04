package velrondevs.botania.common.item.equipment.tool.manasteel;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.api.item.SortableTool;
import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.common.item.ItemStackSerialization;
import velrondevs.botania.common.item.equipment.CustomDamageItem;
import velrondevs.botania.common.item.equipment.tool.ToolCommons;
import velrondevs.botania.registry.BotaniaItems;

import java.util.function.Consumer;

public class ManasteelShearsItem extends ShearsItem implements CustomDamageItem, SortableTool {

	public static final int MANA_PER_DAMAGE = 30;

	public ManasteelShearsItem(Properties props) {
		super(props.component(DataComponents.TOOL, createToolProperties()));
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<Item> onBroken) {
		return ToolCommons.damageItemIfPossible(stack, amount, entity, MANA_PER_DAMAGE);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if (!world.isClientSide && entity instanceof Player player && stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExactForTool(stack, player, MANA_PER_DAMAGE * 2, true)) {
			stack.setDamageValue(stack.getDamageValue() - 1);
		}
	}

	@Override
	public boolean isValidRepairItem(ItemStack shears, ItemStack material) {
		return material.is(BotaniaItems.manaSteel) || super.isValidRepairItem(shears, material);
	}

	@Override
	public int getSortingPriority(ItemStack stack, BlockState state) {
		return 1000 + ToolCommons.getEnchantmentLevel(ItemStackSerialization.registries(), Enchantments.EFFICIENCY, stack);
	}
}
