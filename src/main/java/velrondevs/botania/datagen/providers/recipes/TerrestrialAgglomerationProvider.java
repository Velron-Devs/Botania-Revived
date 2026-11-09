package velrondevs.botania.datagen.providers.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import velrondevs.botania.common.crafting.recipe.RecipeUtils;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.common.crafting.RecipeTerraPlate;
import velrondevs.botania.registry.BotaniaItems;

import java.util.concurrent.CompletableFuture;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class TerrestrialAgglomerationProvider extends BotaniaRecipeProvider {
	public TerrestrialAgglomerationProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	public String getName() {
		return "Botania Terra Plate recipes";
	}

	@Override
	protected void buildRecipes(RecipeOutput consumer) {
		consumer.accept(key(idFor("terrasteel_ingot")), new RecipeTerraPlate(ManaPoolBlockEntity.MAX_MANA / 2,
				RecipeUtils.ingredientList(Ingredient.of(BotaniaItems.manaSteel),
						Ingredient.of(BotaniaItems.manaPearl), Ingredient.of(BotaniaItems.manaDiamond)),
				new ItemStack(BotaniaItems.terrasteel)), null);
	}

	private static Identifier idFor(String s) {
		return prefix("terra_plate/" + s);
	}
}
