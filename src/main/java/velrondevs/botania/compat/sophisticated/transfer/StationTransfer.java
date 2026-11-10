package velrondevs.botania.compat.sophisticated.transfer;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import velrondevs.botania.api.recipe.BotaniaRecipe;
import velrondevs.botania.compat.sophisticated.UpgradeSlots;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.brew.BrewItem;
import velrondevs.botania.api.recipe.BotanicalBreweryRecipe;
import velrondevs.botania.api.recipe.ElvenTradeRecipe;
import velrondevs.botania.api.recipe.ManaInfusionRecipe;
import velrondevs.botania.api.recipe.StateIngredient;
import velrondevs.botania.api.recipe.RunicAltarRecipe;
import velrondevs.botania.api.recipe.PetalApothecaryRecipe;
import velrondevs.botania.api.recipe.TerrestrialAgglomerationRecipe;
import velrondevs.botania.compat.sophisticated.BreweryUpgradeItem;
import velrondevs.botania.compat.sophisticated.ElvenPortalUpgradeItem;
import velrondevs.botania.compat.sophisticated.PetalApothecaryUpgradeItem;
import velrondevs.botania.compat.sophisticated.TerraPlateUpgradeItem;
import velrondevs.botania.compat.sophisticated.ManaPileUpgradeContainer;
import velrondevs.botania.compat.sophisticated.ManaPileUpgradeItem;
import velrondevs.botania.compat.sophisticated.RunicAltarUpgradeItem;
import velrondevs.botania.compat.sophisticated.StationUpgradeContainer;
import velrondevs.botania.registry.BotaniaBrews;
import velrondevs.botania.registry.BotaniaItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Predicate;

public final class StationTransfer {
	public static final int MAX_ROUNDS = 64;

	private StationTransfer() {}

	public record Need(int slot, List<Predicate<ItemStack>> options, int view, boolean keep) {}

	public record Plan(boolean ok, List<Integer> missingViews, ItemStack[] slots, int[] takes, boolean[] kept) {}

	public static Optional<UpgradeContainerBase<?, ?>> find(StorageContainerMenuBase<?> menu, StationKind kind) {
		Optional<UpgradeContainerBase<?, ?>> open = menu.getOpenContainer();
		if (open.isPresent() && kind.containerClass().isInstance(open.get())) {
			return open;
		}
		UpgradeContainerBase<?, ?> fallback = null;
		for (Map.Entry<Integer, UpgradeContainerBase<?, ?>> entry : new TreeMap<>(menu.getUpgradeContainers()).entrySet()) {
			if (!kind.containerClass().isInstance(entry.getValue())) {
				continue;
			}
			UpgradeContainerBase<?, ?> container = entry.getValue();
			if (menu.getUpgradeEnabled(entry.getKey())) {
				return Optional.of(container);
			}
			if (fallback == null) {
				fallback = container;
			}
		}
		return Optional.ofNullable(fallback);
	}

	public static UpgradeSlots inventory(UpgradeContainerBase<?, ?> container) {
		if (container instanceof ManaPileUpgradeContainer pile) {
			return pile.getUpgradeWrapper().getSlotInventory();
		}
		return ((StationUpgradeContainer<?, ?>) container).getUpgradeWrapper().getInventory();
	}

	public static boolean[] managed(StationKind kind, List<Need> needs, int slots) {
		boolean[] managed = new boolean[slots];
		if (kind != StationKind.MANA_INFUSION) {
			Arrays.fill(managed, true);
			for (int tool : toolSlots(kind)) {
				if (tool < slots) {
					managed[tool] = false;
				}
			}
			return managed;
		}
		for (Need need : needs) {
			if (need.slot() >= 0 && need.slot() < slots) {
				managed[need.slot()] = true;
			}
		}
		return managed;
	}

	private static int[] toolSlots(StationKind kind) {
		return switch (kind) {
			case RUNIC_ALTAR -> new int[] {RunicAltarUpgradeItem.Wrapper.WAND_SLOT};
			case ELVEN_PORTAL -> new int[] {ElvenPortalUpgradeItem.Wrapper.PYLON_SLOT_A, ElvenPortalUpgradeItem.Wrapper.PYLON_SLOT_B};
			case TERRA_PLATE -> new int[] {TerraPlateUpgradeItem.Wrapper.SPARK_SLOT};
			default -> new int[0];
		};
	}

	public static List<Slot> sources(StorageContainerMenuBase<?> menu, Player player) {
		List<Slot> result = new ArrayList<>();
		for (Slot slot : menu.slots) {
			boolean playerSlot = slot.container == player.getInventory() && slot.getContainerSlot() >= 0 && slot.getContainerSlot() < Inventory.INVENTORY_SIZE;
			if ((playerSlot || menu.isStorageInventorySlot(slot.index)) && slot.isActive() && slot.mayPickup(player)) {
				result.add(slot);
			}
		}
		return result;
	}

	public static List<ItemStack> snapshot(List<Slot> slots) {
		List<ItemStack> result = new ArrayList<>(slots.size());
		for (Slot slot : slots) {
			result.add(slot.getItem().copy());
		}
		return result;
	}

	@Nullable
	public static List<Need> needs(StationKind kind, Recipe<?> recipe, Item preferred) {
		List<Ingredient> ingredients = recipe instanceof BotaniaRecipe botaniaRecipe ? botaniaRecipe.getIngredients() : List.of();
		List<Need> needs = new ArrayList<>();
		switch (kind) {
			case MANA_INFUSION -> {
				if (!(recipe instanceof ManaInfusionRecipe infusion) || ingredients.isEmpty()) {
					return null;
				}
				needs.add(new Need(ManaPileUpgradeItem.Wrapper.INPUT_SLOT, List.of(ingredients.get(0)), 0, false));
				StateIngredient catalyst = infusion.getRecipeCatalyst();
				if (catalyst != null) {
					needs.add(new Need(ManaPileUpgradeItem.Wrapper.CATALYST_SLOT,
							List.of(stack -> stack.getItem() instanceof BlockItem block && catalyst.test(block.getBlock().defaultBlockState())), -1, true));
				}
			}
			case RUNIC_ALTAR -> {
				if (!(recipe instanceof RunicAltarRecipe altar) || ingredients.isEmpty() || ingredients.size() > RunicAltarUpgradeItem.Wrapper.INGREDIENT_SLOTS) {
					return null;
				}
				for (int i = 0; i < ingredients.size(); i++) {
					needs.add(new Need(i, List.of(ingredients.get(i)), i + 1, false));
				}
				needs.add(new Need(RunicAltarUpgradeItem.Wrapper.REAGENT_SLOT, List.of(altar.getReagent()), 0, true));
			}
			case PETAL_APOTHECARY -> {
				if (!(recipe instanceof PetalApothecaryRecipe petals) || ingredients.isEmpty() || ingredients.size() > PetalApothecaryUpgradeItem.Wrapper.INGREDIENT_SLOTS) {
					return null;
				}
				for (int i = 0; i < ingredients.size(); i++) {
					needs.add(new Need(i, List.of(ingredients.get(i)), i + 2, false));
				}
				needs.add(new Need(PetalApothecaryUpgradeItem.Wrapper.REAGENT_SLOT, List.of(petals.getReagent()), 1, true));
				needs.add(new Need(PetalApothecaryUpgradeItem.Wrapper.WATER_SLOT, List.of(PetalApothecaryUpgradeItem.Wrapper::isWater), 0, true));
			}
			case TERRA_PLATE -> {
				if (!(recipe instanceof TerrestrialAgglomerationRecipe) || ingredients.isEmpty() || ingredients.size() > TerraPlateUpgradeItem.Wrapper.INGREDIENT_SLOTS) {
					return null;
				}
				for (int i = 0; i < ingredients.size(); i++) {
					needs.add(new Need(i, List.of(ingredients.get(i)), i, false));
				}
			}
			case ELVEN_PORTAL -> {
				if (!(recipe instanceof ElvenTradeRecipe trade) || trade.isReturnRecipe() || ingredients.isEmpty()) {
					return null;
				}
				for (int i = 0; i < ingredients.size(); i++) {
					needs.add(new Need(-1, List.of(ingredients.get(i)), i, false));
				}
			}
			case BREWERY -> {
				if (!(recipe instanceof BotanicalBreweryRecipe brew) || ingredients.isEmpty() || ingredients.size() > BreweryUpgradeItem.Wrapper.INGREDIENT_SLOTS) {
					return null;
				}
				List<Item> containers = new ArrayList<>();
				if (preferred != Items.AIR) {
					containers.add(preferred);
				}
				for (Item item : new Item[] {BotaniaItems.vial, BotaniaItems.flask, BotaniaItems.incenseStick, BotaniaItems.bloodPendant}) {
					if (!containers.contains(item)) {
						containers.add(item);
					}
				}
				List<Predicate<ItemStack>> options = new ArrayList<>();
				for (Item item : containers) {
					options.add(stack -> stack.is(item) && isUnbrewed(stack) && !brew.getOutput(stack.copyWithCount(1)).isEmpty());
				}
				needs.add(new Need(BreweryUpgradeItem.Wrapper.CONTAINER_SLOT, options, 0, false));
				for (int i = 0; i < ingredients.size(); i++) {
					needs.add(new Need(i + 1, List.of(ingredients.get(i)), i + 1, false));
				}
			}
		}
		return needs;
	}

	private static boolean isUnbrewed(ItemStack stack) {
		return !(stack.getItem() instanceof BrewItem item) || item.getBrew(stack) == null || item.getBrew(stack) == BotaniaBrews.fallbackBrew;
	}

	public static Plan plan(List<Need> needs, List<ItemStack> sources, UpgradeSlots target, boolean multi, boolean max, boolean includeTarget) {
		boolean[] all = new boolean[target.getSlots()];
		Arrays.fill(all, true);
		return plan(needs, sources, target, multi, max, includeTarget, all);
	}

	public static Plan plan(List<Need> needs, List<ItemStack> sources, UpgradeSlots target, boolean multi, boolean max, boolean includeTarget, boolean[] managed) {
		int slots = target.getSlots();
		boolean[] kept = new boolean[slots];
		for (Need need : needs) {
			if (need.keep() && need.slot() >= 0 && need.slot() < slots) {
				ItemStack current = target.getStackInSlot(need.slot());
				kept[need.slot()] = !current.isEmpty() && matches(need, current);
			}
		}
		List<ItemStack> pool = new ArrayList<>(sources.size() + slots);
		for (ItemStack stack : sources) {
			pool.add(stack.copy());
		}
		if (includeTarget) {
			for (int slot = 0; slot < slots; slot++) {
				if (managed[slot] && !kept[slot] && !target.getStackInSlot(slot).isEmpty()) {
					pool.add(target.getStackInSlot(slot).copy());
				}
			}
		}
		int[] initial = new int[pool.size()];
		for (int i = 0; i < initial.length; i++) {
			initial[i] = pool.get(i).getCount();
		}
		int[] avail = initial.clone();
		ItemStack[] out = emptySlots(slots);
		int limit = multi && max ? MAX_ROUNDS : 1;
		for (int round = 0; round < limit; round++) {
			int[] availBackup = avail.clone();
			ItemStack[] outBackup = copySlots(out);
			List<Integer> missing = new ArrayList<>();
			for (Need need : needs) {
				if (need.slot() >= 0 && need.slot() < slots && kept[need.slot()]) {
					continue;
				}
				int found = find(need, pool, avail, target);
				if (found < 0 || !place(out, target, need.slot(), pool.get(found).copyWithCount(1))) {
					missing.add(need.view());
					continue;
				}
				avail[found]--;
			}
			if (!missing.isEmpty()) {
				if (round == 0) {
					return new Plan(false, missing, out, new int[sources.size()], kept);
				}
				avail = availBackup;
				out = outBackup;
				break;
			}
		}
		int[] takes = new int[pool.size()];
		for (int i = 0; i < takes.length; i++) {
			takes[i] = initial[i] - avail[i];
		}
		return new Plan(true, List.of(), out, takes, kept);
	}

	private static boolean matches(Need need, ItemStack stack) {
		for (Predicate<ItemStack> option : need.options()) {
			if (option.test(stack)) {
				return true;
			}
		}
		return false;
	}

	private static int find(Need need, List<ItemStack> pool, int[] avail, UpgradeSlots target) {
		for (Predicate<ItemStack> option : need.options()) {
			for (int i = 0; i < pool.size(); i++) {
				ItemStack stack = pool.get(i);
				if (avail[i] > 0 && option.test(stack) && fits(target, need.slot(), stack)) {
					return i;
				}
			}
		}
		return -1;
	}

	private static boolean fits(UpgradeSlots target, int slot, ItemStack stack) {
		if (slot >= 0) {
			return slot < target.getSlots() && target.isItemValid(slot, stack);
		}
		for (int i = 0; i < target.getSlots(); i++) {
			if (target.isItemValid(i, stack)) {
				return true;
			}
		}
		return false;
	}

	private static boolean place(ItemStack[] out, UpgradeSlots target, int slot, ItemStack unit) {
		if (slot >= 0) {
			if (slot >= out.length || !out[slot].isEmpty()) {
				return false;
			}
			out[slot] = unit;
			return true;
		}
		for (int i = 0; i < out.length; i++) {
			if (!out[i].isEmpty() && ItemStack.isSameItemSameComponents(out[i], unit)
					&& out[i].getCount() < Math.min(target.getSlotLimit(i), unit.getMaxStackSize())) {
				out[i].grow(1);
				return true;
			}
		}
		for (int i = 0; i < out.length; i++) {
			if (out[i].isEmpty() && target.isItemValid(i, unit)) {
				out[i] = unit;
				return true;
			}
		}
		return false;
	}

	private static ItemStack[] emptySlots(int count) {
		ItemStack[] result = new ItemStack[count];
		Arrays.fill(result, ItemStack.EMPTY);
		return result;
	}

	private static ItemStack[] copySlots(ItemStack[] source) {
		ItemStack[] result = new ItemStack[source.length];
		for (int i = 0; i < source.length; i++) {
			result[i] = source[i].copy();
		}
		return result;
	}
}
