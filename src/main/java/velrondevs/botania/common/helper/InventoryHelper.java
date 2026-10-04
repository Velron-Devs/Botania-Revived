package velrondevs.botania.common.helper;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.common.block.block_entity.SimpleInventoryBlockEntity;
import velrondevs.botania.mixin.HopperBlockEntityAccessor;
import velrondevs.botania.mixin.InventoryAccessor;

import java.util.List;
import java.util.function.Function;

public class InventoryHelper {

	public static ItemStack simulateTransfer(Container to, ItemStack stack, @Nullable Direction side) {
		stack = stack.copy();

		if (to instanceof WorldlyContainer sidedInventory && side != null) {
			int[] is = sidedInventory.getSlotsForFace(side);

			for (int i = 0; i < is.length && !stack.isEmpty(); ++i) {
				stack = simulateTransfer(to, stack, is[i], side);
			}
		} else {
			int j = to.getContainerSize();

			for (int k = 0; k < j && !stack.isEmpty(); ++k) {
				stack = simulateTransfer(to, stack, k, side);
			}
		}

		return stack;
	}

	private static ItemStack simulateTransfer(Container to, ItemStack stack, int slot, @Nullable Direction direction) {
		ItemStack itemStack = to.getItem(slot);
		if (HopperBlockEntityAccessor.botania_canInsert(to, stack, slot, direction)) {
			boolean bl = false;
			boolean bl2 = to.isEmpty();
			if (itemStack.isEmpty()) {

				stack = ItemStack.EMPTY;
				bl = true;
			} else if (HopperBlockEntityAccessor.botania_canMerge(itemStack, stack)) {
				int i = stack.getMaxStackSize() - itemStack.getCount();
				int j = Math.min(stack.getCount(), i);
				stack.shrink(j);

				bl = j > 0;
			}

		}

		return stack;
	}

	public static void withdrawFromInventory(SimpleInventoryBlockEntity inv, Player player) {
		for (int i = inv.inventorySize() - 1; i >= 0; i--) {
			ItemStack stackAt = inv.getItemHandler().getItem(i);
			if (!stackAt.isEmpty()) {
				ItemStack copy = stackAt.copy();
				player.getInventory().placeItemBackInInventory(copy);
				inv.getItemHandler().setItem(i, ItemStack.EMPTY);
				inv.getLevel().gameEvent(null, GameEvent.BLOCK_CHANGE, inv.getBlockPos());
				break;
			}
		}
	}

	public static boolean overrideStackedOnOther(
			Function<ItemStack, Container> inventoryGetter,
			boolean selfGuiOpen,
			@NotNull ItemStack container, @NotNull Slot slot,
			@NotNull ClickAction clickAction, @NotNull Player player) {
		if (!selfGuiOpen && clickAction == ClickAction.SECONDARY) {
			ItemStack toInsert = slot.getItem();
			var inventory = inventoryGetter.apply(container);
			if (simulateTransfer(inventory, toInsert, null).isEmpty()) {
				ItemStack taken = slot.safeTake(toInsert.getCount(), Integer.MAX_VALUE, player);
				HopperBlockEntity.addItem(null, inventory, taken, null);
				return true;
			}
		}
		return false;
	}

	public static boolean overrideOtherStackedOnMe(
			Function<ItemStack, Container> inventoryGetter,
			boolean selfGuiOpen,
			@NotNull ItemStack container, @NotNull ItemStack toInsert,
			@NotNull ClickAction clickAction, @NotNull SlotAccess cursorAccess) {
		if (!selfGuiOpen && clickAction == ClickAction.SECONDARY) {
			var inventory = inventoryGetter.apply(container);
			if (simulateTransfer(inventory, toInsert, null).isEmpty()) {
				HopperBlockEntity.addItem(null, inventory, toInsert, null);
				cursorAccess.set(ItemStack.EMPTY);
				return true;
			}
		}
		return false;
	}

	public static void checkEmpty(ItemStack remainder) {
		if (!remainder.isEmpty()) {
			BotaniaAPI.LOGGER.warn("Remainder was not empty after insert, item may have been lost: {}", remainder);
		}
	}

	public static boolean tryToSetLastRecipe(Player player, Container inv, @Nullable List<ItemStack> lastRecipe, @Nullable SoundEvent sound) {
		if (lastRecipe == null || lastRecipe.isEmpty()) {
			return false;
		}

		int index = 0;
		boolean didAny = false;
		for (ItemStack stack : lastRecipe) {
			if (stack.isEmpty()) {
				continue;
			}

			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				ItemStack pstack = player.getInventory().getItem(i);
				if (player.isCreative() || (!pstack.isEmpty() && ItemStack.isSameItemSameComponents(stack, pstack))) {
					inv.setItem(index, player.isCreative() ? stack.copy() : pstack.split(1));
					didAny = true;
					index++;
					break;
				}
			}
		}

		if (didAny) {
			if (sound != null) {
				player.level().playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundSource.BLOCKS, 0.1F, 10F);
			}
			ServerPlayer mp = (ServerPlayer) player;
			mp.inventoryMenu.broadcastChanges();
		}
		return didAny;
	}

	public static boolean containsType(Inventory inventory, Item item) {
		for (List<ItemStack> compartment : ((InventoryAccessor) inventory).getCompartments()) {
			for (ItemStack stack : compartment) {
				if (stack.is(item)) {
					return true;
				}
			}
		}

		return false;
	}
}
