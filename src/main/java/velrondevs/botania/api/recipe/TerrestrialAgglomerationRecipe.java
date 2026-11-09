package velrondevs.botania.api.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;

public interface TerrestrialAgglomerationRecipe extends BotaniaRecipe {
	Identifier TERRA_PLATE_ID = Identifier.fromNamespaceAndPath(BotaniaAPI.MODID, "terra_plate");
	Identifier TYPE_ID = TERRA_PLATE_ID;

	int getMana();

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	default RecipeType<? extends TerrestrialAgglomerationRecipe> getType() {
		return (RecipeType<? extends TerrestrialAgglomerationRecipe>) BuiltInRegistries.RECIPE_TYPE.getValue(TYPE_ID);
	}

	@NotNull
	@Override
	default ItemStack getToastSymbol() {
		return BuiltInRegistries.ITEM.getOptional(TERRA_PLATE_ID).map(ItemStack::new).orElse(ItemStack.EMPTY);
	}
}
