package velrondevs.botania.compat.sophisticated;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.p3pp3rf1y.sophisticatedcore.api.IStashStorageItem;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IStackableContentsUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ITickableUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.mana.ManaItem;
import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.api.recipe.ManaInfusionRecipe;
import velrondevs.botania.common.proxy.Proxy;
import velrondevs.botania.registry.BotaniaRecipeTypes;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ManaPileUpgradeItem extends UpgradeItemBase<ManaPileUpgradeItem.Wrapper> {
	public static final UpgradeType<Wrapper> TYPE = new UpgradeType<>(Wrapper::new);

	public ManaPileUpgradeItem() {
		super(UpgradeLimits.of(SophisticatedConfig::pileMaxPerStorage));
	}

	@Override
	public UpgradeType<Wrapper> getType() {
		return TYPE;
	}

	@Override
	public List<IUpgradeItem.UpgradeConflictDefinition> getUpgradeConflicts() {
		return List.of();
	}

	@Override
	public int getInventoryColumnsTaken() {
		return 2;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		int mana = stack.getOrDefault(SophisticatedComponents.MANA, 0);
		tooltip.add(Component.translatable("botania.sophisticated.stored_mana", String.format("%,d", mana)).withStyle(ChatFormatting.AQUA));
	}

	public static int capacityFor(IStorageWrapper storage) {
		double multiplier = storage.getInventoryHandler().getStackSizeMultiplier();
		double capacity = SophisticatedConfig.pileBaseCapacity() * (1.0 + SophisticatedConfig.pileStackRatio() * (multiplier - 1.0));
		if (Double.isNaN(capacity) || capacity < 1) {
			return 1;
		}
		return capacity >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) capacity;
	}

	public static List<Wrapper> enabledPiles(IStorageWrapper storage) {
		List<Wrapper> piles = new ArrayList<>();
		for (Wrapper pile : storage.getUpgradeHandler().getTypeWrappers(TYPE)) {
			if (pile.isEnabled()) {
				piles.add(pile);
			}
		}
		return piles;
	}

	public static boolean hasPile(IStorageWrapper storage) {
		for (Wrapper pile : storage.getUpgradeHandler().getTypeWrappers(TYPE)) {
			if (pile.isEnabled()) {
				return true;
			}
		}
		return false;
	}

	public static long availableMana(IStorageWrapper storage) {
		long total = 0;
		for (Wrapper pile : enabledPiles(storage)) {
			total += pile.getMana();
		}
		return total;
	}

	public static long freeSpace(IStorageWrapper storage) {
		long total = 0;
		for (Wrapper pile : enabledPiles(storage)) {
			total += Math.max(0, pile.getCapacity() - pile.getMana());
		}
		return total;
	}

	public static int drainMana(IStorageWrapper storage, int amount, boolean simulate) {
		int taken = 0;
		for (Wrapper pile : enabledPiles(storage)) {
			if (taken >= amount) {
				break;
			}
			taken += pile.extractMana(amount - taken, simulate);
		}
		return taken;
	}

	public static int fillMana(IStorageWrapper storage, int amount, boolean simulate) {
		int accepted = 0;
		for (Wrapper pile : enabledPiles(storage)) {
			if (accepted >= amount) {
				break;
			}
			accepted += pile.insert(amount - accepted, simulate);
		}
		return accepted;
	}

	private static boolean isManaSource(ItemStack stack) {
		return !stack.isEmpty() && !(stack.getItem() instanceof IStashStorageItem) && XplatAbstractions.INSTANCE.findManaItem(stack) != null;
	}

	@Nullable
	private static RecipeManager recipeManager(@Nullable Level level) {
		if (level != null) {
			return level.getRecipeManager();
		}
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server != null) {
			return server.getRecipeManager();
		}
		Player player = Proxy.INSTANCE.getClientPlayer();
		return player != null ? player.level().getRecipeManager() : null;
	}

	public static class Wrapper extends UpgradeWrapperBase<Wrapper, ManaPileUpgradeItem> implements ITickableUpgrade, IStackableContentsUpgrade {
		public static final int SOURCE_SLOT = 0;
		public static final int CATALYST_SLOT = 1;
		public static final int INPUT_SLOT = 2;
		public static final int OUTPUT_SLOT = 3;
		public static final int SLOT_COUNT = 4;
		private final UpgradeSlots slot;
		private long nextCharge;
		private long nextInfusion;

		protected Wrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
			super(storageWrapper, upgrade, upgradeSaveHandler);
			slot = new UpgradeSlots(upgrade, SLOT_COUNT, Wrapper::accepts, index -> index == INPUT_SLOT || index == OUTPUT_SLOT ? 64 : 1, this::save);
		}

		public UpgradeSlots getSlotInventory() {
			return slot;
		}

		public boolean isInfusing() {
			return upgrade.getOrDefault(SophisticatedComponents.PILE_INFUSION, false);
		}

		public void setInfusing(boolean infusing) {
			if (infusing == isInfusing()) {
				return;
			}
			if (infusing) {
				upgrade.set(SophisticatedComponents.PILE_INFUSION, true);
			} else {
				upgrade.remove(SophisticatedComponents.PILE_INFUSION);
			}
			save();
		}

		public boolean isDischarging() {
			return upgrade.getOrDefault(SophisticatedComponents.PILE_DISCHARGE, false);
		}

		public void setDischarging(boolean discharging) {
			if (discharging == isDischarging()) {
				return;
			}
			if (discharging) {
				upgrade.set(SophisticatedComponents.PILE_DISCHARGE, true);
			} else {
				upgrade.remove(SophisticatedComponents.PILE_DISCHARGE);
			}
			save();
		}

		public int getMana() {
			return Math.max(0, upgrade.getOrDefault(SophisticatedComponents.MANA, 0));
		}

		public int getCapacity() {
			return capacityFor(storageWrapper);
		}

		private void setMana(int mana) {
			if (mana <= 0) {
				upgrade.remove(SophisticatedComponents.MANA);
			} else {
				upgrade.set(SophisticatedComponents.MANA, mana);
			}
			save();
		}

		public int insert(int amount, boolean simulate) {
			if (amount <= 0 || !isEnabled()) {
				return 0;
			}
			int mana = getMana();
			int accepted = (int) Math.max(0, Math.min((long) amount, (long) getCapacity() - mana));
			if (accepted > 0 && !simulate) {
				setMana(mana + accepted);
			}
			return accepted;
		}

		public int extractMana(int amount, boolean simulate) {
			return extract(amount, simulate);
		}

		public int extract(int amount, boolean simulate) {
			if (amount <= 0 || !isEnabled()) {
				return 0;
			}
			int mana = getMana();
			int taken = Math.min(amount, mana);
			if (taken > 0 && !simulate) {
				setMana(mana - taken);
			}
			return taken;
		}

		@Override
		public void tick(@Nullable Entity entity, Level level, BlockPos pos) {
			if (level.isClientSide()) {
				return;
			}
			if (level.getGameTime() >= nextInfusion) {
				nextInfusion = level.getGameTime() + SophisticatedConfig.pileInfusionInterval();
				ejectInvalidInput(level);
				if (isInfusing()) {
					infuse(level);
				}
			}
			if (isDischarging()) {
				if (SophisticatedConfig.pileChargeItems()) {
					chargeSlot(level);
				}
				return;
			}
			if (isInCooldown(level)) {
				return;
			}
			boolean fromSlot = SophisticatedConfig.pileAbsorbFromSlot();
			boolean fromItems = SophisticatedConfig.pileAbsorbFromItems();
			boolean fromPlayer = SophisticatedConfig.pilePullFromPlayer() && entity instanceof Player;
			if (!fromSlot && !fromItems && !fromPlayer) {
				return;
			}
			setCooldown(level, SophisticatedConfig.pileAbsorbInterval());
			int budget = Math.min(getCapacity() - getMana(), SophisticatedConfig.pileAbsorbAmount());
			if (budget <= 0) {
				return;
			}
			int absorbed = 0;
			if (fromSlot) {
				absorbed += absorbFromSlot(budget - absorbed);
			}
			if (fromItems && absorbed < budget) {
				absorbed += absorbFromItems(budget - absorbed);
			}
			if (fromPlayer && absorbed < budget) {
				absorbed += absorbFromPlayer((Player) entity, budget - absorbed);
			}
			if (absorbed > 0) {
				insert(absorbed, false);
			}
		}

		private static boolean accepts(int index, ItemStack stack) {
			return switch (index) {
				case SOURCE_SLOT -> isManaSource(stack);
				case CATALYST_SLOT -> stack.getItem() instanceof BlockItem;
				case INPUT_SLOT -> hasInfusionRecipe(null, stack);
				default -> false;
			};
		}

		private static boolean hasInfusionRecipe(@Nullable Level level, ItemStack stack) {
			RecipeManager manager = recipeManager(level);
			if (manager == null) {
				return false;
			}
			for (RecipeHolder<ManaInfusionRecipe> holder : manager.getAllRecipesFor(BotaniaRecipeTypes.MANA_INFUSION_TYPE)) {
				if (holder.value().matches(stack)) {
					return true;
				}
			}
			return false;
		}

		private void ejectInvalidInput(Level level) {
			ItemStack input = slot.getStackInSlot(INPUT_SLOT);
			if (input.isEmpty() || hasInfusionRecipe(level, input)) {
				return;
			}
			StationAutomation.flush(slot, INPUT_SLOT, storageWrapper.getInventoryForUpgradeProcessing());
		}

		private void infuse(Level level) {
			boolean autoFeed = SophisticatedConfig.pileInfusionAutoFeed();
			boolean autoOutput = SophisticatedConfig.pileInfusionAutoOutput();
			int maxCrafts = SophisticatedConfig.pileInfusionMaxPerInterval();
			BlockState catalyst = catalystState();
			for (int crafts = 0; crafts < maxCrafts; crafts++) {
				if (autoOutput) {
					flushOutput();
				}
				if (autoFeed) {
					refillInput();
				}
				ItemStack input = slot.getStackInSlot(INPUT_SLOT);
				if (input.isEmpty()) {
					return;
				}
				Optional<RecipeHolder<ManaInfusionRecipe>> holder = findRecipe(level, input, catalyst);
				if (holder.isEmpty()) {
					return;
				}
				ManaInfusionRecipe recipe = holder.get().value();
				int cost = Math.max(0, recipe.getManaToConsume());
				if (getMana() < cost) {
					return;
				}
				ItemStack output = recipe.getRecipeOutput(level.registryAccess(), input.copyWithCount(1));
				if (output.isEmpty()) {
					return;
				}
				ItemStack current = slot.getStackInSlot(OUTPUT_SLOT);
				ItemStack result;
				if (current.isEmpty()) {
					result = output.copy();
				} else if (ItemStack.isSameItemSameComponents(current, output)
						&& current.getCount() + output.getCount() <= Math.min(current.getMaxStackSize(), slot.getSlotLimit(OUTPUT_SLOT))) {
					result = current.copyWithCount(current.getCount() + output.getCount());
				} else {
					return;
				}
				slot.setStackInSlot(INPUT_SLOT, input.getCount() > 1 ? input.copyWithCount(input.getCount() - 1) : ItemStack.EMPTY);
				slot.setStackInSlot(OUTPUT_SLOT, result);
				if (cost > 0) {
					extract(cost, false);
				}
			}
			if (autoOutput) {
				flushOutput();
			}
			if (autoFeed) {
				refillInput();
			}
		}

		private void refillInput() {
			ItemStack input = slot.getStackInSlot(INPUT_SLOT);
			StationAutomation.refill(slot, INPUT_SLOT, input, Integer.MAX_VALUE, storageWrapper.getInventoryForUpgradeProcessing());
		}

		private void flushOutput() {
			StationAutomation.flush(slot, OUTPUT_SLOT, storageWrapper.getInventoryForUpgradeProcessing());
		}

		private BlockState catalystState() {
			ItemStack catalyst = slot.getStackInSlot(CATALYST_SLOT);
			return catalyst.getItem() instanceof BlockItem blockItem ? blockItem.getBlock().defaultBlockState() : Blocks.AIR.defaultBlockState();
		}

		private static Optional<RecipeHolder<ManaInfusionRecipe>> findRecipe(Level level, ItemStack stack, BlockState catalyst) {
			RecipeHolder<ManaInfusionRecipe> plain = null;
			for (RecipeHolder<ManaInfusionRecipe> holder : level.getRecipeManager().getAllRecipesFor(BotaniaRecipeTypes.MANA_INFUSION_TYPE)) {
				ManaInfusionRecipe recipe = holder.value();
				if (!recipe.matches(stack)) {
					continue;
				}
				if (recipe.getRecipeCatalyst() == null) {
					if (plain == null) {
						plain = holder;
					}
				} else if (recipe.getRecipeCatalyst().test(catalyst)) {
					return Optional.of(holder);
				}
			}
			return Optional.ofNullable(plain);
		}

		private void chargeSlot(Level level) {
			long time = level.getGameTime();
			if (time < nextCharge) {
				return;
			}
			nextCharge = time + SophisticatedConfig.pileChargeInterval();
			ItemStack slotCopy = slot.getStackInSlot(SOURCE_SLOT).copy();
			int demand = space(slotCopy);
			if (demand <= 0) {
				return;
			}
			int taken = extract(Math.min(Math.min(getMana(), SophisticatedConfig.pileChargeRate()), demand), false);
			if (taken <= 0) {
				return;
			}
			int added = charge(slotCopy, taken);
			if (added > 0) {
				slot.setStackInSlot(SOURCE_SLOT, slotCopy);
			}
			if (taken > added) {
				insert(taken - added, false);
			}
		}

		public int chargeStacks(List<ItemStack> targets) {
			int available = Math.min(getMana(), SophisticatedConfig.pileChargeRate());
			if (available <= 0 || targets.isEmpty()) {
				return 0;
			}
			long demand = 0;
			for (ItemStack stack : targets) {
				demand += space(stack);
			}
			if (demand <= 0) {
				return 0;
			}
			int taken = extract((int) Math.min(available, demand), false);
			if (taken <= 0) {
				return 0;
			}
			int left = taken;
			for (ItemStack stack : targets) {
				if (left <= 0) {
					break;
				}
				left -= charge(stack, left);
			}
			if (left > 0) {
				insert(left, false);
			}
			return taken - left;
		}

		private int space(ItemStack stack) {
			if (stack.isEmpty() || stack.getItem() instanceof IStashStorageItem) {
				return 0;
			}
			ManaItem target = XplatAbstractions.INSTANCE.findManaItem(stack);
			if (target == null || !target.canReceiveManaFromItem(upgrade)) {
				return 0;
			}
			return (int) Math.max(0L, (long) target.getMaxMana() - target.getMana());
		}

		private int charge(ItemStack stack, int max) {
			int amount = Math.min(max, space(stack));
			if (amount <= 0) {
				return 0;
			}
			ManaItem target = XplatAbstractions.INSTANCE.findManaItem(stack);
			int before = target.getMana();
			target.addMana(amount);
			return (int) Math.max(0L, Math.min(amount, (long) target.getMana() - before));
		}

		private int drain(ItemStack stack, int max) {
			ManaItem source = XplatAbstractions.INSTANCE.findManaItem(stack);
			if (source == null || max <= 0 || source.isNoExport() || !source.canExportManaToItem(upgrade)) {
				return 0;
			}
			int before = source.getMana();
			if (before <= 0) {
				return 0;
			}
			source.addMana(-Math.min(max, before));
			return Math.max(0, before - source.getMana());
		}

		private int absorbFromSlot(int max) {
			ItemStack stack = slot.getStackInSlot(SOURCE_SLOT);
			if (!isManaSource(stack)) {
				return 0;
			}
			ItemStack copy = stack.copy();
			int removed = drain(copy, max);
			if (removed > 0) {
				slot.setStackInSlot(SOURCE_SLOT, copy);
			}
			return removed;
		}

		private int absorbFromItems(int max) {
			InventoryHandler inventory = storageWrapper.getInventoryHandler();
			int absorbed = 0;
			for (int index = 0; index < inventory.getSlots() && absorbed < max; index++) {
				ItemStack stack = inventory.getStackInSlot(index);
				if (!isManaSource(stack)) {
					continue;
				}
				ItemStack copy = stack.copy();
				int removed = drain(copy, max - absorbed);
				if (removed > 0) {
					inventory.setStackInSlot(index, copy);
					absorbed += removed;
				}
			}
			return absorbed;
		}

		private int absorbFromPlayer(Player player, int max) {
			int absorbed = 0;
			List<ItemStack> candidates = new ArrayList<>(ManaItemHandler.instance().getManaItems(player));
			candidates.addAll(ManaItemHandler.instance().getManaAccesories(player));
			for (ItemStack stack : candidates) {
				if (absorbed >= max) {
					break;
				}
				if (isManaSource(stack)) {
					absorbed += drain(stack, max - absorbed);
				}
			}
			return absorbed;
		}

		@Override
		public int getMinimumMultiplierRequired() {
			int mana = getMana();
			int base = SophisticatedConfig.pileBaseCapacity();
			if (mana <= 0) {
				return 0;
			}
			if (mana <= base) {
				return 1;
			}
			double ratio = SophisticatedConfig.pileStackRatio();
			if (ratio <= 0) {
				return Integer.MAX_VALUE;
			}
			double needed = 1.0 + ((double) mana / base - 1.0) / ratio;
			return needed >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) Math.ceil(needed);
		}
	}
}
