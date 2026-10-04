package velrondevs.botania.integration.kubejs;

import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.util.ID;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.HideFromJS;

import net.minecraft.resources.ResourceLocation;

import velrondevs.botania.event.BotaniaEvents;

import java.util.ArrayList;
import java.util.List;

public class BotaniaMachineRecipesKubeEvent implements KubeEvent {
	private final BotaniaEvents.RegisterMachineRecipes event;
	private final List<BotaniaKubeRecipe<?>> recipes = new ArrayList<>();
	private int added;

	public BotaniaMachineRecipesKubeEvent(BotaniaEvents.RegisterMachineRecipes event) {
		this.event = event;
	}

	public int getAdded() {
		return added;
	}

	public boolean exists(Object id) {
		return event.exists(ID.kjs(id));
	}

	public BotaniaKubeRecipe.RunicAltar runicAltar(Context cx, Object output, Object inputs) {
		return add(new BotaniaKubeRecipe.RunicAltar(cx, output, inputs));
	}

	public BotaniaKubeRecipe.PetalApothecary petalApothecary(Context cx, Object output, Object inputs) {
		return add(new BotaniaKubeRecipe.PetalApothecary(cx, output, inputs));
	}

	public BotaniaKubeRecipe.ManaInfusion manaInfusion(Context cx, Object output, Object input) {
		return add(new BotaniaKubeRecipe.ManaInfusion(cx, output, input));
	}

	public BotaniaKubeRecipe.ElvenTrade elvenTrade(Context cx, Object outputs, Object inputs) {
		return add(new BotaniaKubeRecipe.ElvenTrade(cx, outputs, inputs));
	}

	public BotaniaKubeRecipe.TerraPlate terraPlate(Context cx, Object output, Object inputs) {
		return add(new BotaniaKubeRecipe.TerraPlate(cx, output, inputs));
	}

	public BotaniaKubeRecipe.BrewRecipe brew(Context cx, Object brew, Object inputs) {
		return add(new BotaniaKubeRecipe.BrewRecipe(cx, brew, inputs));
	}

	public BotaniaKubeRecipe.PureDaisy pureDaisy(Context cx, Object input, Object output) {
		return add(new BotaniaKubeRecipe.PureDaisy(cx, input, output));
	}

	private <T extends BotaniaKubeRecipe<?>> T add(T recipe) {
		recipes.add(recipe);
		return recipe;
	}

	@HideFromJS
	public void register() {
		for (var recipe : recipes) {
			if (!recipe.isFailed() && recipe.getId() != null) {
				register(recipe, recipe.getId());
			}
		}
		for (var recipe : recipes) {
			if (!recipe.isFailed() && recipe.getId() == null) {
				register(recipe, autoId(recipe));
			}
		}
		recipes.clear();
	}

	private void register(BotaniaKubeRecipe<?> recipe, ResourceLocation id) {
		try {
			event.add(id, recipe.build());
			added++;
		} catch (Exception e) {
			recipe.error("Failed to add " + recipe.describe() + (recipe.getId() == null ? " " + id : ""), e);
		}
	}

	private ResourceLocation autoId(BotaniaKubeRecipe<?> recipe) {
		String base = "botania_" + recipe.getType() + "/" + recipe.autoIdPath();
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath("kubejs", base);
		for (int i = 2; event.exists(id); i++) {
			id = ResourceLocation.fromNamespaceAndPath("kubejs", base + "_" + i);
		}
		return id;
	}
}
