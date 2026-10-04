package velrondevs.botania.common.crafting.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

public class WaterBottleMatchingRecipe extends ShapedRecipe {
	public static final RecipeSerializer<WaterBottleMatchingRecipe> SERIALIZER = RecipeUtils.shapedWrapper(WaterBottleMatchingRecipe::new);

	private final NonNullList<Ingredient> displayIngredients;

	public WaterBottleMatchingRecipe(ShapedRecipe recipe) {
		super(recipe.getGroup(), recipe.category(), recipe.pattern, recipe.getResultItem(RegistryAccess.EMPTY), recipe.showNotification());
		this.displayIngredients = NonNullList.of(Ingredient.EMPTY, recipe.getIngredients().stream().map(i -> {
			if (i.test(new ItemStack(Items.POTION))) {
				return Ingredient.of(PotionContents.createItemStack(Items.POTION, Potions.WATER));
			}
			return i;
		}).toArray(Ingredient[]::new));
	}

	@NotNull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return displayIngredients;
	}

	@Override
	public boolean matches(@NotNull CraftingInput craftingContainer, @NotNull Level level) {
		if (!super.matches(craftingContainer, level)) {
			return false;
		}
		for (int i = 0; i < craftingContainer.size(); i++) {
			var item = craftingContainer.getItem(i);
			if (item.is(Items.POTION) && !item.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
