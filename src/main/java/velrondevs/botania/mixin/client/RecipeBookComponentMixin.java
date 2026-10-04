package velrondevs.botania.mixin.client;

import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import velrondevs.botania.client.core.RecipeBookAccess;

@Mixin(RecipeBookComponent.class)
public class RecipeBookComponentMixin implements RecipeBookAccess {
	@Unique
	private ItemStack hoveredGhostRecipeStack;

	@Override
	public ItemStack getHoveredGhostRecipeStack() {
		return hoveredGhostRecipeStack;
	}

	@ModifyVariable(method = "renderGhostRecipeTooltip", at = @At("RETURN"), ordinal = 0, require = 0)
	private ItemStack captureHoveredGhostStack(ItemStack stack) {
		hoveredGhostRecipeStack = stack;
		return stack;
	}
}
