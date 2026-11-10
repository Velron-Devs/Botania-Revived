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
import velrondevs.botania.api.recipe.RunicAltarRecipe;
import velrondevs.botania.common.item.WandOfTheForestItem;
import velrondevs.botania.common.item.material.RuneItem;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class RunicAltarUpgradeItem extends UpgradeItemBase<RunicAltarUpgradeItem.Wrapper> {
	public static final UpgradeType<Wrapper> TYPE = new UpgradeType<>(Wrapper::new);

	public RunicAltarUpgradeItem() {
		super(UpgradeLimits.of(SophisticatedConfig::altarMaxPerStorage));
	}

	@Override
	public UpgradeType<Wrapper> getType() {
		return TYPE;
	}

	@Override
	public List<IUpgradeItem.UpgradeConflictDefinition> getUpgradeConflicts() {
		return List.of();
	}

	public static class Wrapper extends StationUpgradeWrapper<Wrapper, RunicAltarUpgradeItem> {
		public static final int INGREDIENT_SLOTS = 16;
		public static final int REAGENT_SLOT = 16;
		public static final int WAND_SLOT = 17;

		protected Wrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
			super(storageWrapper, upgrade, upgradeSaveHandler, INGREDIENT_SLOTS + 2);
		}

		public static boolean isWand(ItemStack stack) {
			return stack.getItem() instanceof WandOfTheForestItem;
		}

		@Override
		protected boolean stationEnabled() {
			return SophisticatedConfig.altarEnabled();
		}

		@Override
		protected int interval() {
			return SophisticatedConfig.altarInterval();
		}

		@Override
		protected boolean accepts(int slot, ItemStack stack) {
			if (slot == WAND_SLOT) {
				return isWand(stack);
			}
			if (isWand(stack)) {
				return false;
			}
			boolean livingrock = stack.is(BotaniaBlocks.livingrock.asItem());
			return slot == REAGENT_SLOT ? livingrock : !livingrock;
		}

		@Override
		protected int slotLimit(int slot) {
			return slot == REAGENT_SLOT ? 64 : 1;
		}

		private boolean hasWand() {
			return !SophisticatedConfig.altarRequireWand() || isWand(getInventory().getStackInSlot(WAND_SLOT));
		}

		@Override
		protected Plan plan(Level level) {
			SimpleContainer container = new SimpleContainer(INGREDIENT_SLOTS);
			List<ItemStack> returned = new ArrayList<>();
			int count = 0;
			for (int slot = 0; slot < INGREDIENT_SLOTS; slot++) {
				ItemStack stack = getInventory().getStackInSlot(slot);
				if (!stack.isEmpty()) {
					container.setItem(count++, stack.copyWithCount(1));
					if (stack.getItem() instanceof RuneItem) {
						returned.add(stack.copyWithCount(1));
					}
				}
			}
			if (count == 0) {
				return null;
			}
			ContainerRecipeInput input = new ContainerRecipeInput(container);
			Optional<RecipeHolder<RunicAltarRecipe>> holder = level.getRecipeManager().getRecipeFor(BotaniaRecipeTypes.RUNE_TYPE, input, level);
			if (holder.isEmpty()) {
				return null;
			}
			RunicAltarRecipe recipe = holder.get().value();
			if (!recipe.returnsRunes()) {
				returned.clear();
			}
			ItemStack output = recipe.assemble(input, level.registryAccess());
			ItemStack reagent = getInventory().getStackInSlot(REAGENT_SLOT);
			boolean missingReagent = reagent.isEmpty() || !recipe.getReagent().test(reagent);
			List<ItemStack> outputs = new ArrayList<>();
			outputs.add(output);
			outputs.addAll(returned);
			int cost = scaleCost(recipe.getManaUsage(), SophisticatedConfig.altarCostPercent());
			int missing = !hasWand() ? StationState.NO_WAND : missingReagent ? StationState.NO_REAGENT : 0;
			return new Plan(cost, outputs, missing, () -> {
				for (int slot = 0; slot < INGREDIENT_SLOTS; slot++) {
					if (!getInventory().getStackInSlot(slot).isEmpty()) {
						getInventory().setStackInSlot(slot, ItemStack.EMPTY);
					}
				}
				getInventory().setStackInSlot(REAGENT_SLOT, reagent.copyWithCount(reagent.getCount() - 1));
			});
		}
	}
}
