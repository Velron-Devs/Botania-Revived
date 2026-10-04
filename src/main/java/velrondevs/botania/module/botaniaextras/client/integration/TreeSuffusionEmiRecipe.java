package velrondevs.botania.module.botaniaextras.client.integration;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeHolder;

import velrondevs.botania.client.integration.emi.BotaniaEmiRecipe;
import velrondevs.botania.client.integration.emi.ManaWidget;
import velrondevs.botania.client.integration.emi.RunicAltarEmiRecipe;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;
import velrondevs.botania.module.botaniaextras.crafting.TreeSuffusionRecipe;

import java.util.List;

public class TreeSuffusionEmiRecipe extends BotaniaEmiRecipe {
	private final List<EmiIngredient> ingredients;
	private final int mana;

	public TreeSuffusionEmiRecipe(EmiRecipeCategory category, RecipeHolder<TreeSuffusionRecipe> holder) {
		super(category, holder);
		TreeSuffusionRecipe recipe = holder.value();
		this.ingredients = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
		this.input = ingredients;
		this.output = List.of(EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY)));
		this.mana = recipe.getMana();
	}

	@Override
	public int getDisplayHeight() {
		return 107;
	}

	@Override
	public int getDisplayWidth() {
		return 106;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.add(new ManaWidget(2, 100, mana, ManaPoolBlockEntity.MAX_MANA / 10));
		RunicAltarEmiRecipe.addRunicAltarWidgets(widgets, this, ingredients, EmiStack.of(BotaniaExtrasWoods.sapling), output.get(0));
	}
}
