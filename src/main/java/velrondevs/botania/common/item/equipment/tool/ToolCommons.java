package velrondevs.botania.common.item.equipment.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.common.item.ItemStackSerialization;
import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import velrondevs.botania.registry.BotaniaItems;

import java.util.function.Predicate;

public final class ToolCommons {

	private static boolean recCall = false;

	public static int damageItemIfPossible(ItemStack stack, int amount, LivingEntity entity, int manaPerDamage) {
		if (!(entity instanceof Player player) || amount == 0) {
			return amount;
		}

		final int unbreaking = getEnchantmentLevel(entity.level().registryAccess(), Enchantments.UNBREAKING, stack);

		while (amount > 0) {
			if (ManaItemHandler.instance().requestManaExactForTool(stack, player, manaPerDamage, false)) {
				if (entity.level().getRandom().nextInt(unbreaking + 1) == 0) {
					ManaItemHandler.instance().requestManaExactForTool(stack, player, manaPerDamage, true);
				}
				amount--;
			} else {
				break;
			}
		}

		return amount;
	}

	public static void removeBlocksInIteration(Player player, ItemStack stack, Level world, BlockPos centerPos,
			Vec3i startDelta, Vec3i endDelta, Predicate<BlockState> filter) {
		if (recCall) {
			return;
		}

		recCall = true;
		try {
			for (BlockPos iterPos : BlockPos.betweenClosed(centerPos.offset(startDelta),
					centerPos.offset(endDelta))) {

				if (iterPos.equals(centerPos)) {
					continue;
				}
				removeBlockWithDrops(player, stack, world, iterPos, filter);
			}
		} finally {
			recCall = false;
		}
	}

	public static void removeBlockWithDrops(Player player, ItemStack stack, Level world, BlockPos pos,
			Predicate<BlockState> filter) {
		if (!world.hasChunkAt(pos)) {
			return;
		}

		BlockState blockstate = world.getBlockState(pos);
		boolean unminable = blockstate.getDestroyProgress(player, world, pos) == 0;

		if (!world.isClientSide && !unminable && filter.test(blockstate) && !blockstate.isAir()) {
			ItemStack save = player.getMainHandItem();
			player.setItemInHand(InteractionHand.MAIN_HAND, stack);
			((ServerPlayer) player).connection.send(
					new ClientboundLevelEventPacket(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(blockstate), false));
			((ServerPlayer) player).gameMode.destroyBlock(pos);
			player.setItemInHand(InteractionHand.MAIN_HAND, save);
		}
	}

	public static int getToolPriority(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		}

		Item item = stack.getItem();
		if (!(item instanceof DiggerItem tool)) {
			return 0;
		}

		Tier material = tool.getTier();
		int materialLevel = 0;
		if (material == BotaniaAPI.instance().getManasteelItemTier()) {
			materialLevel = 10;
		}
		if (material == BotaniaAPI.instance().getElementiumItemTier()) {
			materialLevel = 11;
		}
		if (material == BotaniaAPI.instance().getTerrasteelItemTier()) {
			materialLevel = 20;
		}

		int modifier = 0;
		if (stack.is(BotaniaItems.terraPick)) {
			modifier = TerraShattererItem.getLevel(stack);
		}

		int efficiency = getEnchantmentLevel(ItemStackSerialization.registries(), Enchantments.EFFICIENCY, stack);
		return materialLevel * 100 + modifier * 10 + efficiency;
	}

	public static boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
		return !stack.isEmpty() && stack.getItem() instanceof BlockStartBreakItem item && item.onBlockStartBreak(stack, pos, player);
	}

	public static int getEnchantmentLevel(HolderLookup.Provider registries, ResourceKey<Enchantment> key, ItemStack stack) {
		return registries.lookup(Registries.ENCHANTMENT)
				.flatMap(lookup -> lookup.get(key))
				.map(holder -> EnchantmentHelper.getItemEnchantmentLevel(holder, stack))
				.orElse(0);
	}

	public static BlockHitResult raytraceFromEntity(Entity e, double distance, boolean fluids) {
		return (BlockHitResult) e.pick(distance, 1, fluids);
	}
}
