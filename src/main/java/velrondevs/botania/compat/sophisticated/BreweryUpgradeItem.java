package velrondevs.botania.compat.sophisticated;

import net.minecraft.world.item.Item;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;

import velrondevs.botania.api.brew.BrewContainer;
import velrondevs.botania.api.brew.BrewItem;
import velrondevs.botania.api.recipe.BotanicalBreweryRecipe;
import velrondevs.botania.api.recipe.ContainerRecipeInput;
import velrondevs.botania.registry.BotaniaBrews;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class BreweryUpgradeItem extends UpgradeItemBase<BreweryUpgradeItem.Wrapper> {
	public static final UpgradeType<Wrapper> TYPE = new UpgradeType<>(Wrapper::new);

	public BreweryUpgradeItem() {
		super(UpgradeLimits.of(SophisticatedConfig::breweryMaxPerStorage), new Item.Properties());
	}

	@Override
	public UpgradeType<Wrapper> getType() {
		return TYPE;
	}

	@Override
	public List<IUpgradeItem.UpgradeConflictDefinition> getUpgradeConflicts() {
		return List.of();
	}

	public static class Wrapper extends StationUpgradeWrapper<Wrapper, BreweryUpgradeItem> {
		public static final int CONTAINER_SLOT = 0;
		public static final int INGREDIENT_SLOTS = 6;

		protected Wrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
			super(storageWrapper, upgrade, upgradeSaveHandler, INGREDIENT_SLOTS + 1);
		}

		@Override
		protected boolean stationEnabled() {
			return SophisticatedConfig.breweryEnabled();
		}

		@Override
		protected int interval() {
			return SophisticatedConfig.breweryInterval();
		}

		@Override
		protected boolean accepts(int slot, ItemStack stack) {
			if (slot == CONTAINER_SLOT) {
				return stack.getItem() instanceof BrewContainer;
			}
			if (stack.getItem() instanceof BrewContainer) {
				return false;
			}
			return !(stack.getItem() instanceof BrewItem brew && brew.getBrew(stack) != null && brew.getBrew(stack) != BotaniaBrews.fallbackBrew);
		}

		@Override
		protected Plan plan(Level level) {
			ItemStack vessel = getInventory().getStackInSlot(CONTAINER_SLOT);
			if (vessel.isEmpty() || !(vessel.getItem() instanceof BrewContainer brewContainer)) {
				return null;
			}
			SimpleContainer container = new SimpleContainer(INGREDIENT_SLOTS + 1);
			container.setItem(0, vessel.copyWithCount(1));
			int count = 1;
			for (int slot = 1; slot <= INGREDIENT_SLOTS; slot++) {
				ItemStack stack = getInventory().getStackInSlot(slot);
				if (!stack.isEmpty()) {
					container.setItem(count++, stack.copyWithCount(1));
				}
			}
			if (count == 1) {
				return null;
			}
			Optional<RecipeHolder<BotanicalBreweryRecipe>> holder = BotaniaRecipeTypes.getRecipeFor(level, BotaniaRecipeTypes.BREW_TYPE, (RecipeInput) new ContainerRecipeInput(container));
			if (holder.isEmpty()) {
				return null;
			}
			BotanicalBreweryRecipe recipe = holder.get().value();
			ItemStack output = recipe.getOutput(vessel);
			int cost = scaleCost(brewContainer.getManaCost(recipe.getBrew(), vessel), SophisticatedConfig.breweryCostPercent());
			return new Plan(cost, List.of(output), false, () -> {
				for (int slot = 0; slot <= INGREDIENT_SLOTS; slot++) {
					if (!getInventory().getStackInSlot(slot).isEmpty()) {
						getInventory().setStackInSlot(slot, ItemStack.EMPTY);
					}
				}
			});
		}
	}
}
