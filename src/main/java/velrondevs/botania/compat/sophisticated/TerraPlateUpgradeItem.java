package velrondevs.botania.compat.sophisticated;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;

import velrondevs.botania.api.recipe.ContainerRecipeInput;
import velrondevs.botania.api.recipe.TerrestrialAgglomerationRecipe;
import velrondevs.botania.common.item.ManaSparkItem;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class TerraPlateUpgradeItem extends UpgradeItemBase<TerraPlateUpgradeItem.Wrapper> {
	public static final UpgradeType<Wrapper> TYPE = new UpgradeType<>(Wrapper::new);

	public TerraPlateUpgradeItem() {
		super(UpgradeLimits.of(SophisticatedConfig::terraPlateMaxPerStorage));
	}

	@Override
	public UpgradeType<Wrapper> getType() {
		return TYPE;
	}

	@Override
	public List<IUpgradeItem.UpgradeConflictDefinition> getUpgradeConflicts() {
		return List.of();
	}

	public static class Wrapper extends StationUpgradeWrapper<Wrapper, TerraPlateUpgradeItem> {
		public static final int INGREDIENT_SLOTS = 20;
		public static final int SPARK_SLOT = 20;

		protected Wrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
			super(storageWrapper, upgrade, upgradeSaveHandler, INGREDIENT_SLOTS + 1);
		}

		public static boolean isSpark(ItemStack stack) {
			return stack.getItem() instanceof ManaSparkItem;
		}

		@Override
		protected boolean stationEnabled() {
			return SophisticatedConfig.terraPlateEnabled();
		}

		@Override
		protected int interval() {
			return SophisticatedConfig.terraPlateInterval();
		}

		@Override
		protected boolean autoFeed() {
			return SophisticatedConfig.stationAutoFeed() && SophisticatedConfig.terraPlateAutoFeed();
		}

		@Override
		protected boolean accepts(int slot, ItemStack stack) {
			return (slot == SPARK_SLOT) == isSpark(stack);
		}

		private boolean hasSpark() {
			return !SophisticatedConfig.terraPlateRequireSpark() || isSpark(getInventory().getStackInSlot(SPARK_SLOT));
		}

		@Override
		protected Plan plan(Level level) {
			SimpleContainer container = new SimpleContainer(INGREDIENT_SLOTS);
			int count = 0;
			for (int slot = 0; slot < INGREDIENT_SLOTS; slot++) {
				ItemStack stack = getInventory().getStackInSlot(slot);
				if (!stack.isEmpty()) {
					container.setItem(count++, stack.copyWithCount(1));
				}
			}
			if (count == 0) {
				return null;
			}
			if (!hasSpark()) {
				return new Plan(0, List.of(), StationState.NO_SPARK, () -> {});
			}
			ContainerRecipeInput input = new ContainerRecipeInput(container);
			Optional<RecipeHolder<TerrestrialAgglomerationRecipe>> holder = level.getRecipeManager().getRecipeFor(BotaniaRecipeTypes.TERRA_PLATE_TYPE, input, level);
			if (holder.isEmpty()) {
				return null;
			}
			TerrestrialAgglomerationRecipe recipe = holder.get().value();
			ItemStack output = recipe.assemble(input, level.registryAccess());
			int cost = scaleCost(recipe.getMana(), SophisticatedConfig.terraPlateCostPercent());
			return new Plan(cost, List.of(output), 0, () -> {
				for (int slot = 0; slot < INGREDIENT_SLOTS; slot++) {
					if (!getInventory().getStackInSlot(slot).isEmpty()) {
						getInventory().setStackInSlot(slot, ItemStack.EMPTY);
					}
				}
			});
		}
	}
}
