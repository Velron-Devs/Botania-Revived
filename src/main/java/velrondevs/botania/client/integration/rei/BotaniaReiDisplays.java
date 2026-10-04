package velrondevs.botania.client.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.api.recipe.BotanicalBreweryRecipe;
import velrondevs.botania.api.recipe.ElvenTradeRecipe;
import velrondevs.botania.api.recipe.ManaInfusionRecipe;
import velrondevs.botania.api.recipe.OrechidRecipe;
import velrondevs.botania.api.recipe.PetalApothecaryRecipe;
import velrondevs.botania.api.recipe.PureDaisyRecipe;
import velrondevs.botania.api.recipe.RunicAltarRecipe;
import velrondevs.botania.api.recipe.TerrestrialAgglomerationRecipe;
import velrondevs.botania.registry.BotaniaBlocks;

import java.util.ArrayList;
import java.util.List;

public final class BotaniaReiDisplays {
	private BotaniaReiDisplays() {}

	public static BotaniaReiDisplay manaInfusion(RecipeHolder<? extends ManaInfusionRecipe> holder) {
		ManaInfusionRecipe recipe = holder.value();
		List<EntryIngredient> extras = new ArrayList<>();
		if (recipe.getRecipeCatalyst() != null) {
			List<EntryStack<?>> catalysts = new ArrayList<>();
			for (BlockState state : recipe.getRecipeCatalyst().getDisplayed()) {
				ItemStack stack = new ItemStack(state.getBlock());
				if (!stack.isEmpty()) {
					catalysts.add(EntryStacks.of(stack));
				}
			}
			extras.add(EntryIngredient.of(catalysts));
		}
		return new BotaniaReiDisplay(BotaniaReiCategories.MANA_INFUSION,
				EntryIngredients.ofIngredients(recipe.getIngredients()), extras,
				List.of(EntryIngredients.of(recipe.getResultItem(RegistryAccess.EMPTY))),
				holder.id(), recipe.getManaToConsume(), null);
	}

	public static BotaniaReiDisplay petalApothecary(RecipeHolder<? extends PetalApothecaryRecipe> holder) {
		PetalApothecaryRecipe recipe = holder.value();
		return new BotaniaReiDisplay(BotaniaReiCategories.PETAL_APOTHECARY,
				EntryIngredients.ofIngredients(recipe.getIngredients()),
				List.of(EntryIngredients.of(Items.WATER_BUCKET), EntryIngredients.ofIngredient(recipe.getReagent())),
				List.of(EntryIngredients.of(recipe.getResultItem(RegistryAccess.EMPTY))),
				holder.id(), 0, null);
	}

	public static BotaniaReiDisplay runicAltar(RecipeHolder<? extends RunicAltarRecipe> holder) {
		RunicAltarRecipe recipe = holder.value();
		return new BotaniaReiDisplay(BotaniaReiCategories.RUNIC_ALTAR,
				EntryIngredients.ofIngredients(recipe.getIngredients()),
				List.of(EntryIngredients.of(BotaniaBlocks.livingrock)),
				List.of(EntryIngredients.of(recipe.getResultItem(RegistryAccess.EMPTY))),
				holder.id(), recipe.getManaUsage(), null);
	}

	public static BotaniaReiDisplay terrestrialAgglomeration(RecipeHolder<? extends TerrestrialAgglomerationRecipe> holder) {
		TerrestrialAgglomerationRecipe recipe = holder.value();
		return new BotaniaReiDisplay(BotaniaReiCategories.TERRESTRIAL_AGGLOMERATION,
				EntryIngredients.ofIngredients(recipe.getIngredients()), List.of(),
				List.of(EntryIngredients.of(recipe.getResultItem(RegistryAccess.EMPTY))),
				holder.id(), recipe.getMana(), null);
	}

	public static BotaniaReiDisplay elvenTrade(RecipeHolder<? extends ElvenTradeRecipe> holder) {
		ElvenTradeRecipe recipe = holder.value();
		List<EntryIngredient> outputs = new ArrayList<>();
		for (ItemStack stack : recipe.getOutputs()) {
			outputs.add(EntryIngredients.of(stack));
		}
		return new BotaniaReiDisplay(BotaniaReiCategories.ELVEN_TRADE,
				EntryIngredients.ofIngredients(recipe.getIngredients()), List.of(), outputs,
				holder.id(), 0, null);
	}

	public static BotaniaReiDisplay botanicalBrewery(RecipeHolder<? extends BotanicalBreweryRecipe> holder, ItemStack container) {
		BotanicalBreweryRecipe recipe = holder.value();
		ResourceLocation containerId = BuiltInRegistries.ITEM.getKey(container.getItem());
		ResourceLocation id = holder.id().withSuffix("/" + containerId.getNamespace() + "/" + containerId.getPath());
		return new BotaniaReiDisplay(BotaniaReiCategories.BOTANICAL_BREWERY,
				EntryIngredients.ofIngredients(recipe.getIngredients()),
				List.of(EntryIngredients.of(container)),
				List.of(EntryIngredients.of(recipe.getOutput(container.copy()))),
				id, 0, null);
	}

	public static BotaniaReiDisplay pureDaisy(RecipeHolder<? extends PureDaisyRecipe> holder) {
		PureDaisyRecipe recipe = holder.value();
		List<EntryStack<?>> inputs = new ArrayList<>();
		for (BlockState state : recipe.getInput().getDisplayed()) {
			if (!state.getFluidState().isEmpty()) {
				inputs.add(EntryStacks.of(state.getFluidState().getType()));
			} else {
				ItemStack stack = new ItemStack(state.getBlock());
				if (!stack.isEmpty()) {
					inputs.add(EntryStacks.of(stack));
				}
			}
		}
		return new BotaniaReiDisplay(BotaniaReiCategories.PURE_DAISY,
				List.of(EntryIngredient.of(inputs)), List.of(),
				List.of(EntryIngredients.of(new ItemStack(recipe.getOutputState().getBlock()))),
				holder.id(), 0, null);
	}

	public static BotaniaReiDisplay orechid(CategoryIdentifier<BotaniaReiDisplay> category,
			RecipeHolder<? extends OrechidRecipe> holder) {
		OrechidRecipe recipe = holder.value();
		List<EntryStack<?>> inputs = new ArrayList<>();
		for (BlockState state : recipe.getInput().getDisplayed()) {
			ItemStack stack = new ItemStack(state.getBlock());
			if (!stack.isEmpty()) {
				inputs.add(EntryStacks.of(stack));
			}
		}
		List<EntryStack<?>> outputs = new ArrayList<>();
		for (BlockState state : recipe.getOutput().getDisplayed()) {
			ItemStack stack = new ItemStack(state.getBlock());
			if (!stack.isEmpty()) {
				outputs.add(EntryStacks.of(stack));
			}
		}
		return new BotaniaReiDisplay(category, List.of(EntryIngredient.of(inputs)), List.of(),
				List.of(EntryIngredient.of(outputs)), holder.id(), 0, recipe);
	}
}
