package velrondevs.botania.mixin;

import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.NormalCraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NormalCraftingRecipe.class)
public interface NormalCraftingRecipeAccessor {
	@Accessor("commonInfo")
	Recipe.CommonInfo botania$getCommonInfo();

	@Accessor("bookInfo")
	CraftingRecipe.CraftingBookInfo botania$getBookInfo();
}
