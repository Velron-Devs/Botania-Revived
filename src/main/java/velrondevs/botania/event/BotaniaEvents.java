package velrondevs.botania.event;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import velrondevs.botania.api.brew.Brew;
import velrondevs.botania.api.recipe.StateIngredient;
import velrondevs.botania.common.crafting.BotanicalBreweryRecipe;
import velrondevs.botania.common.crafting.ElvenTradeRecipe;
import velrondevs.botania.common.crafting.ManaInfusionRecipe;
import velrondevs.botania.common.crafting.PetalsRecipe;
import velrondevs.botania.common.crafting.PureDaisyRecipe;
import velrondevs.botania.common.crafting.RecipeTerraPlate;
import velrondevs.botania.common.crafting.RunicAltarRecipe;
import velrondevs.botania.common.item.material.RuneItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public final class BotaniaEvents {
	private BotaniaEvents() {}

	public static class RegisterRunes extends Event implements IModBusEvent {
		private final BiConsumer<Item, Identifier> registry;
		private final Set<Identifier> ids = new HashSet<>();

		public RegisterRunes(BiConsumer<Item, Identifier> registry) {
			this.registry = registry;
		}

		public Item register(Identifier id, Item rune) {
			if (!ids.add(id)) {
				throw new IllegalArgumentException("Duplicate rune id " + id);
			}
			registry.accept(rune, id);
			return rune;
		}

		public Item register(Identifier id) {
			return register(id, new RuneItem(new Item.Properties()));
		}
	}

	public static class RegisterMachineRecipes extends Event {
		private final Set<Identifier> existing;
		private final List<RecipeHolder<?>> added = new ArrayList<>();

		public RegisterMachineRecipes(Set<Identifier> existing) {
			this.existing = new HashSet<>(existing);
		}

		public List<RecipeHolder<?>> getAdded() {
			return added;
		}

		public boolean exists(Identifier id) {
			return existing.contains(id);
		}

		public void add(Identifier id, Recipe<?> recipe) {
			if (!existing.add(id)) {
				throw new IllegalArgumentException("Duplicate recipe id " + id);
			}
			added.add(new RecipeHolder<>(ResourceKey.create(Registries.RECIPE, id), recipe));
		}

		public void runicAltar(Identifier id, ItemStack output, int mana, Ingredient... inputs) {
			add(id, new RunicAltarRecipe(output, mana, inputs));
		}

		public void petalApothecary(Identifier id, ItemStack output, Ingredient reagent, Ingredient... inputs) {
			add(id, new PetalsRecipe(output, reagent, inputs));
		}

		public void manaInfusion(Identifier id, ItemStack output, Ingredient input, int mana) {
			add(id, new ManaInfusionRecipe(output, input, mana, null, null));
		}

		public void manaInfusion(Identifier id, ItemStack output, Ingredient input, int mana, StateIngredient catalyst) {
			add(id, new ManaInfusionRecipe(output, input, mana, null, catalyst));
		}

		public void elvenTrade(Identifier id, ItemStack[] outputs, Ingredient... inputs) {
			add(id, new ElvenTradeRecipe(outputs, inputs));
		}

		public void terraPlate(Identifier id, int mana, ItemStack output, Ingredient... inputs) {
			NonNullList<Ingredient> list = NonNullList.create();
			list.addAll(List.of(inputs));
			add(id, new RecipeTerraPlate(mana, list, output));
		}

		public void brew(Identifier id, Brew brew, Ingredient... inputs) {
			add(id, new BotanicalBreweryRecipe(brew, inputs));
		}

		public void pureDaisy(Identifier id, StateIngredient input, BlockState output, int time) {
			add(id, new PureDaisyRecipe(input, output, time, Optional.empty()));
		}
	}
}
