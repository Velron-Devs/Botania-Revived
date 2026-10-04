package velrondevs.botania.client.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.recipe.OrechidRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BotaniaReiDisplay extends BasicDisplay {
	private final CategoryIdentifier<BotaniaReiDisplay> category;
	private final List<EntryIngredient> ingredients;
	private final List<EntryIngredient> extras;
	private final int mana;
	@Nullable
	private final OrechidRecipe orechid;

	public BotaniaReiDisplay(CategoryIdentifier<BotaniaReiDisplay> category, List<EntryIngredient> ingredients,
			List<EntryIngredient> extras, List<EntryIngredient> outputs, @Nullable ResourceLocation id, int mana,
			@Nullable OrechidRecipe orechid) {
		super(concat(ingredients, extras), outputs, Optional.ofNullable(id));
		this.category = category;
		this.ingredients = ingredients;
		this.extras = extras;
		this.mana = mana;
		this.orechid = orechid;
	}

	private static List<EntryIngredient> concat(List<EntryIngredient> first, List<EntryIngredient> second) {
		List<EntryIngredient> all = new ArrayList<>(first);
		all.addAll(second);
		return all;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return category;
	}

	public List<EntryIngredient> ingredients() {
		return ingredients;
	}

	public List<EntryIngredient> extras() {
		return extras;
	}

	public EntryIngredient output() {
		return outputs.isEmpty() ? EntryIngredient.empty() : outputs.get(0);
	}

	public int mana() {
		return mana;
	}

	@Nullable
	public OrechidRecipe orechid() {
		return orechid;
	}
}
