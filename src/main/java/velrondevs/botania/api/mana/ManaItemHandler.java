package velrondevs.botania.api.mana;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.ServiceUtil;

import java.util.Collections;
import java.util.List;

public interface ManaItemHandler {
	ManaItemHandler INSTANCE = ServiceUtil.findService(ManaItemHandler.class, () -> new ManaItemHandler() {});

	static ManaItemHandler instance() {
		return INSTANCE;
	}

	default List<ItemStack> getManaItems(Player player) {
		return Collections.emptyList();
	}

	default List<ItemStack> getManaAccesories(Player player) {
		return Collections.emptyList();
	}

	default int requestMana(ItemStack stack, Player player, int manaToGet, boolean remove) {
		return 0;
	}

	default boolean requestManaExact(ItemStack stack, Player player, int manaToGet, boolean remove) {
		return false;
	}

	default int dispatchMana(ItemStack stack, Player player, int manaToSend, boolean add) {
		return 0;
	}

	default boolean dispatchManaExact(ItemStack stack, Player player, int manaToSend, boolean add) {
		return false;
	}

	default int requestManaForTool(ItemStack stack, Player player, int manaToGet, boolean remove) {
		return 0;
	}

	default boolean requestManaExactForTool(ItemStack stack, Player player, int manaToGet, boolean remove) {
		return false;
	}

	default int getInvocationCountForTool(ItemStack stack, Player player, int manaToGet) {
		return 0;
	}

	default float getFullDiscountForTools(Player player, ItemStack tool) {
		return 0;
	}

	default boolean hasProficiency(Player player, ItemStack manaItem) {
		return false;
	}
}
