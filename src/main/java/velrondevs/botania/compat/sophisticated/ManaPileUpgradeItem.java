package velrondevs.botania.compat.sophisticated;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.p3pp3rf1y.sophisticatedcore.api.IStashStorageItem;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.init.ModCoreDataComponents;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedcore.inventory.StatefulComponentItemHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IStackableContentsUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ITickableUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;

import velrondevs.botania.api.mana.ManaItem;
import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;
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

	public static class Wrapper extends UpgradeWrapperBase<Wrapper, ManaPileUpgradeItem> implements ITickableUpgrade, IStackableContentsUpgrade {
		private final Slot slot;

		protected Wrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
			super(storageWrapper, upgrade, upgradeSaveHandler);
			slot = new Slot();
		}

		public IItemHandlerModifiable getSlotInventory() {
			return slot;
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
		public void tick(Entity entity, Level level, BlockPos pos) {
			if (level.isClientSide() || isInCooldown(level)) {
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
			ItemStack stack = slot.getStackInSlot(0);
			if (!isManaSource(stack)) {
				return 0;
			}
			ItemStack copy = stack.copy();
			int removed = drain(copy, max);
			if (removed > 0) {
				slot.setStackInSlot(0, copy);
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

		private final class Slot extends StatefulComponentItemHandler {
			private Slot() {
				super(Wrapper.this.upgrade, ModCoreDataComponents.LENIENT_CONTAINER.get(), 1);
			}

			@Override
			public boolean isItemValid(int index, ItemStack stack) {
				return stack.isEmpty() || isManaSource(stack);
			}

			@Override
			public int getSlotLimit(int index) {
				return 1;
			}

			@Override
			protected void onContentsChanged(int index, ItemStack oldStack, ItemStack newStack) {
				save();
			}
		}
	}
}
