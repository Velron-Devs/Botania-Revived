package velrondevs.botania.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface AbstractFurnaceBlockEntityAccessor {
	@Accessor("items")
	NonNullList<ItemStack> botania_getItems();

	@Accessor("quickCheck")
	RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> getQuickCheck();

	@Accessor("litTimeRemaining")
	int getLitTime();

	@Accessor("litTimeRemaining")
	void setLitTime(int burnTime);

	@Accessor("cookingTimer")
	int getCookingProgress();

	@Accessor
	int getCookingTotalTime();

	@Accessor("cookingTimer")
	void setCookingProgress(int cookTime);
}
