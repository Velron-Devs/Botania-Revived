package velrondevs.botania.compat.sophisticated;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;

import velrondevs.botania.api.recipe.ContainerRecipeInput;
import velrondevs.botania.api.recipe.PetalApothecaryRecipe;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class PetalApothecaryUpgradeItem extends UpgradeItemBase<PetalApothecaryUpgradeItem.Wrapper> {
	public static final UpgradeType<Wrapper> TYPE = new UpgradeType<>(Wrapper::new);

	public PetalApothecaryUpgradeItem() {
		super(UpgradeLimits.of(SophisticatedConfig::apothecaryMaxPerStorage), new Item.Properties());
	}

	@Override
	public UpgradeType<Wrapper> getType() {
		return TYPE;
	}

	@Override
	public List<IUpgradeItem.UpgradeConflictDefinition> getUpgradeConflicts() {
		return List.of();
	}

	public static class Wrapper extends StationUpgradeWrapper<Wrapper, PetalApothecaryUpgradeItem> {
		public static final int INGREDIENT_SLOTS = 16;
		public static final int REAGENT_SLOT = 16;
		public static final int WATER_SLOT = 17;

		protected Wrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
			super(storageWrapper, upgrade, upgradeSaveHandler, INGREDIENT_SLOTS + 2);
		}

		public static boolean isWater(ItemStack stack) {
			return stack.is(Items.WATER_BUCKET);
		}

		@Override
		protected boolean stationEnabled() {
			return SophisticatedConfig.apothecaryEnabled();
		}

		@Override
		protected int interval() {
			return SophisticatedConfig.apothecaryInterval();
		}

		@Override
		protected boolean autoFeed() {
			return SophisticatedConfig.stationAutoFeed() && SophisticatedConfig.apothecaryAutoFeed();
		}

		@Override
		protected boolean accepts(int slot, ItemStack stack) {
			if (slot == WATER_SLOT) {
				return isWater(stack);
			}
			if (isWater(stack)) {
				return false;
			}
			return slot == REAGENT_SLOT || !stack.is(BotaniaTags.Items.SEED_APOTHECARY_REAGENT);
		}

		@Override
		protected int slotLimit(int slot) {
			return slot == REAGENT_SLOT ? 64 : 1;
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
			ContainerRecipeInput input = new ContainerRecipeInput(container);
			Optional<RecipeHolder<PetalApothecaryRecipe>> holder = BotaniaRecipeTypes.getRecipeFor(level, BotaniaRecipeTypes.PETAL_TYPE, (RecipeInput) input);
			if (holder.isEmpty()) {
				return null;
			}
			PetalApothecaryRecipe recipe = holder.get().value();
			ItemStack output = recipe.assemble(input, level.registryAccess());
			ItemStack water = getInventory().getStackInSlot(WATER_SLOT);
			ItemStack reagent = getInventory().getStackInSlot(REAGENT_SLOT);
			boolean consumeWater = SophisticatedConfig.apothecaryConsumeWater();
			int missing = !isWater(water) ? StationState.NO_WATER
					: reagent.isEmpty() || !recipe.getReagent().test(reagent) ? StationState.NO_SEED : 0;
			List<ItemStack> outputs = new ArrayList<>();
			outputs.add(output);
			if (consumeWater) {
				outputs.add(new ItemStack(Items.BUCKET));
			}
			return new Plan(0, outputs, missing, () -> {
				for (int slot = 0; slot < INGREDIENT_SLOTS; slot++) {
					if (!getInventory().getStackInSlot(slot).isEmpty()) {
						getInventory().setStackInSlot(slot, ItemStack.EMPTY);
					}
				}
				getInventory().setStackInSlot(REAGENT_SLOT, reagent.copyWithCount(reagent.getCount() - 1));
				if (consumeWater) {
					getInventory().setStackInSlot(WATER_SLOT, ItemStack.EMPTY);
				}
			});
		}
	}
}
