package velrondevs.botania.common.item.equipment.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
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
	private static final Tier MATERIAL = new Tier() {
		@Override
		public int getUses() {
			return 125;
		}

		@Override
		public float getSpeed() {
			return 4.8F;
		}

		@Override
		public float getAttackDamageBonus() {
			return 0;
		}

		@Override
		public TagKey<Block> getIncorrectBlocksForDrops() {
			return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
		}

		@Override
		public int getEnchantmentValue() {
			return 10;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(Blocks.GLASS);
		}
	};

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
	public void inventoryTick(ItemStack stack, Level world, Entity player, int slot, boolean selected) {
		super.inventoryTick(stack, world, player, slot, selected);
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
