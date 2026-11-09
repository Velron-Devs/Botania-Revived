package velrondevs.botania.common.item.equipment.tool.manasteel;

import org.jetbrains.annotations.Nullable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.item.SortableTool;
import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.common.item.equipment.CustomDamageItem;
import velrondevs.botania.common.item.equipment.tool.ToolCommons;

import java.util.function.Consumer;

public class ManasteelHoeItem extends HoeItem implements CustomDamageItem, SortableTool {
	private final ToolMaterial material;
	private static final int MANA_PER_DAMAGE = 60;

	public ManasteelHoeItem(Properties props) {
		this(BotaniaAPI.instance().getManasteelItemTier(), props, -1f);
	}

	public ManasteelHoeItem(ToolMaterial mat, Properties properties, float attackSpeed) {
		super(mat, -mat.attackDamageBonus(), attackSpeed, properties);
		this.material = mat;
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<Item> onBroken) {
		int manaPer = ((ManasteelHoeItem) stack.getItem()).getManaPerDamage();
		return ToolCommons.damageItemIfPossible(stack, amount, entity, manaPer);
	}

	public ToolMaterial getTier() {
		return material;
	}

	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot eqSlot) {
		if (!world.isClientSide() && entity instanceof Player player && stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExactForTool(stack, player, getManaPerDamage() * 2, true)) {
			stack.setDamageValue(stack.getDamageValue() - 1);
		}
	}

	@Override
	public int getSortingPriority(ItemStack stack, BlockState state) {
		return ToolCommons.getToolPriority(stack);
	}
}
