package velrondevs.botania.datagen.providers.botaniaextras;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import velrondevs.botania.common.crafting.PetalsRecipe;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasFlowers;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasModule;
import velrondevs.botania.registry.BotaniaItems;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaExtrasFlowerRecipes {
	private BotaniaExtrasFlowerRecipes() {}

	private static Ingredient petal(String color) {
		return Ingredient.of(TagKey.create(Registries.ITEM, prefix("petals/" + color)));
	}

	public static void build(RecipeOutput output) {
		Ingredient orange = petal("orange");
		Ingredient black = petal("black");
		Ingredient lightBlue = petal("light_blue");
		output.accept(prefix(BotaniaExtrasModule.ID + "/petal_apothecary/crysanthermum"),
				new PetalsRecipe(new ItemStack(BotaniaExtrasFlowers.crysanthermum), Ingredient.of(BotaniaTags.Items.SEED_APOTHECARY_REAGENT),
						orange, orange, black, lightBlue, lightBlue, Ingredient.of(BotaniaItems.runeWinter), Ingredient.of(BotaniaItems.runeSummer)),
				null);
	}
}
