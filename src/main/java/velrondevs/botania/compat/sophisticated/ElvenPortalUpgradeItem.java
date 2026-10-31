package velrondevs.botania.compat.sophisticated;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;

import velrondevs.botania.api.recipe.ElvenTradeRecipe;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class ElvenPortalUpgradeItem extends UpgradeItemBase<ElvenPortalUpgradeItem.Wrapper> {
	public static final UpgradeType<Wrapper> TYPE = new UpgradeType<>(Wrapper::new);

	public ElvenPortalUpgradeItem() {
		super(UpgradeLimits.of(SophisticatedConfig::portalMaxPerStorage));
	}

	@Override
	public UpgradeType<Wrapper> getType() {
		return TYPE;
	}

	@Override
	public List<IUpgradeItem.UpgradeConflictDefinition> getUpgradeConflicts() {
		return List.of();
	}

	public static class Wrapper extends StationUpgradeWrapper<Wrapper, ElvenPortalUpgradeItem> {
		public static final int INPUT_SLOTS = 8;

		protected Wrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
			super(storageWrapper, upgrade, upgradeSaveHandler, INPUT_SLOTS);
		}

		@Override
		protected boolean stationEnabled() {
			return SophisticatedConfig.portalEnabled();
		}

		@Override
		protected int interval() {
			return SophisticatedConfig.portalInterval();
		}

		@Override
		protected boolean accepts(int slot, ItemStack stack) {
			return true;
		}

		@Override
		protected int slotLimit(int slot) {
			return 64;
		}

		@Override
		protected Plan plan(Level level) {
			List<ItemStack> units = new ArrayList<>();
			Map<ItemStack, Integer> origin = new IdentityHashMap<>();
			for (int slot = 0; slot < INPUT_SLOTS; slot++) {
				ItemStack stack = getInventory().getStackInSlot(slot);
				for (int i = 0; i < stack.getCount(); i++) {
					ItemStack unit = stack.copyWithCount(1);
					units.add(unit);
					origin.put(unit, slot);
				}
			}
			if (units.isEmpty()) {
				return null;
			}
			for (RecipeHolder<ElvenTradeRecipe> holder : level.getRecipeManager().getAllRecipesFor(BotaniaRecipeTypes.ELVEN_TRADE_TYPE)) {
				ElvenTradeRecipe recipe = holder.value();
				if (recipe.isReturnRecipe()) {
					continue;
				}
				Optional<List<ItemStack>> match = recipe.match(units);
				if (match.isEmpty()) {
					continue;
				}
				List<ItemStack> consumed = match.get();
				List<ItemStack> outputs = new ArrayList<>();
				for (ItemStack output : recipe.getOutputs(consumed)) {
					outputs.add(output.copy());
				}
				int[] used = new int[INPUT_SLOTS];
				for (ItemStack unit : consumed) {
					Integer slot = origin.get(unit);
					if (slot != null) {
						used[slot]++;
					}
				}
				return new Plan(SophisticatedConfig.portalCostPerTrade(), outputs, false, () -> {
					for (int slot = 0; slot < INPUT_SLOTS; slot++) {
						if (used[slot] > 0) {
							ItemStack stack = getInventory().getStackInSlot(slot);
							getInventory().setStackInSlot(slot, stack.copyWithCount(stack.getCount() - used[slot]));
						}
					}
				});
			}
			return null;
		}
	}
}
