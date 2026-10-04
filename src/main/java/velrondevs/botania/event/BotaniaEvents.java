package velrondevs.botania.event;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
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
		private final BiConsumer<Item, ResourceLocation> registry;
		private final Set<ResourceLocation> ids = new HashSet<>();

		public RegisterRunes(BiConsumer<Item, ResourceLocation> registry) {
			this.registry = registry;
		}

		public Item register(ResourceLocation id, Item rune) {
			if (!ids.add(id)) {
				throw new IllegalArgumentException("Duplicate rune id " + id);
			}
			registry.accept(rune, id);
			return rune;
		}

		public Item register(ResourceLocation id) {
			return register(id, new RuneItem(new Item.Properties()));
		}
	}

	public static class RegisterMachineRecipes extends Event {
		private final Set<ResourceLocation> existing;
		private final List<RecipeHolder<?>> added = new ArrayList<>();

		public RegisterMachineRecipes(Set<ResourceLocation> existing) {
			this.existing = new HashSet<>(existing);
		}

		public List<RecipeHolder<?>> getAdded() {
			return added;
		}

		public boolean exists(ResourceLocation id) {
			return existing.contains(id);
		}

		public void add(ResourceLocation id, Recipe<?> recipe) {
			if (!existing.add(id)) {
				throw new IllegalArgumentException("Duplicate recipe id " + id);
			}
			added.add(new RecipeHolder<>(id, recipe));
		}

		public void runicAltar(ResourceLocation id, ItemStack output, int mana, Ingredient... inputs) {
			add(id, new RunicAltarRecipe(output, mana, inputs));
		}

		public void petalApothecary(ResourceLocation id, ItemStack output, Ingredient reagent, Ingredient... inputs) {
			add(id, new PetalsRecipe(output, reagent, inputs));
		}

		public void manaInfusion(ResourceLocation id, ItemStack output, Ingredient input, int mana) {
			add(id, new ManaInfusionRecipe(output, input, mana, null, null));
		}

		public void manaInfusion(ResourceLocation id, ItemStack output, Ingredient input, int mana, StateIngredient catalyst) {
			add(id, new ManaInfusionRecipe(output, input, mana, null, catalyst));
		}

		public void elvenTrade(ResourceLocation id, ItemStack[] outputs, Ingredient... inputs) {
			add(id, new ElvenTradeRecipe(outputs, inputs));
		}

		public void terraPlate(ResourceLocation id, int mana, ItemStack output, Ingredient... inputs) {
			NonNullList<Ingredient> list = NonNullList.create();
			list.addAll(List.of(inputs));
			add(id, new RecipeTerraPlate(mana, list, output));
		}

		public void brew(ResourceLocation id, Brew brew, Ingredient... inputs) {
			add(id, new BotanicalBreweryRecipe(brew, inputs));
		}

		public void pureDaisy(ResourceLocation id, StateIngredient input, BlockState output, int time) {
			add(id, new PureDaisyRecipe(input, output, time, Optional.empty()));
		}
	}
}
