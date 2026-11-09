package velrondevs.botania.common.item.equipment.tool;

import org.jetbrains.annotations.Nullable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.neoforge.common.Tags;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.item.equipment.tool.manasteel.ManasteelPickaxeItem;
import velrondevs.botania.xplat.XplatAbstractions;

public class VitreousPickaxeItem extends ManasteelPickaxeItem implements BlockStartBreakItem {
	private static final String TAG_SILK_HACK = "botania:silk_hack";
	private static final int MANA_PER_DAMAGE = 160;
	private static final ToolMaterial MATERIAL = new ToolMaterial(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 125, 4.8F, 0, 10, Tags.Items.GLASS_BLOCKS_COLORLESS);

	public VitreousPickaxeItem(Properties props) {
		super(MATERIAL, props, -1);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
		BlockState state = player.level().getBlockState(pos);
		boolean hasSilk = ToolCommons.getEnchantmentLevel(player.level().registryAccess(), Enchantments.SILK_TOUCH, itemstack) > 0;
		if (hasSilk || !isGlass(state)) {
			return false;
		}

		player.level().registryAccess().lookup(Registries.ENCHANTMENT)
				.flatMap(lookup -> lookup.get(Enchantments.SILK_TOUCH))
				.ifPresent(silkTouch -> itemstack.enchant(silkTouch, 1));
		ItemNBTHelper.setBoolean(itemstack, TAG_SILK_HACK, true);

		return false;
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel world, Entity player, @Nullable EquipmentSlot eqSlot) {
		super.inventoryTick(stack, world, player, eqSlot);
		if (ItemNBTHelper.getBoolean(stack, TAG_SILK_HACK, false)) {
			ItemNBTHelper.removeEntry(stack, TAG_SILK_HACK);
			EnchantmentHelper.updateEnchantments(stack, enchantments -> enchantments.removeIf(ench -> ench.is(Enchantments.SILK_TOUCH)));
		}
	}

	private boolean isGlass(BlockState state) {
		return XplatAbstractions.INSTANCE.isInGlassTag(state);
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public int getSortingPriority(ItemStack stack, BlockState state) {
		return isGlass(state) ? Integer.MAX_VALUE : 0;
	}

}
