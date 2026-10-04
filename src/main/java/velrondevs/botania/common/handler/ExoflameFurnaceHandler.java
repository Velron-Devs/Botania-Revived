package velrondevs.botania.common.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.block.ExoflameHeatable;
import velrondevs.botania.mixin.AbstractFurnaceBlockEntityAccessor;
import velrondevs.botania.xplat.XplatAbstractions;

public class ExoflameFurnaceHandler {
	public static boolean canSmeltRecipe(AbstractFurnaceBlockEntity furnace, RecipeHolder<?> recipe) {
		var items = ((AbstractFurnaceBlockEntityAccessor) furnace).botania_getItems();
		return XplatAbstractions.INSTANCE.canFurnaceBurn(furnace, recipe, items, furnace.getMaxStackSize());
	}

	public static boolean canSmelt(AbstractFurnaceBlockEntity furnace) {
		if (furnace.getItem(0).isEmpty()) {
			return false;
		}
		try {
			var qc = ((AbstractFurnaceBlockEntityAccessor) furnace).getQuickCheck();
			var currentRecipe = qc.getRecipeFor(new SingleRecipeInput(furnace.getItem(0)), furnace.getLevel());
			return currentRecipe.isPresent() && ExoflameFurnaceHandler.canSmeltRecipe(furnace, currentRecipe.get());
		} catch (Throwable t) {
			BotaniaAPI.LOGGER.error("Failed to determine if furnace TE can smelt", t);
			return false;
		}
	}

	public static class FurnaceExoflameHeatable implements ExoflameHeatable {
		private final AbstractFurnaceBlockEntity furnace;

		public FurnaceExoflameHeatable(AbstractFurnaceBlockEntity furnace) {
			this.furnace = furnace;
		}

		@Override
		public boolean canSmelt() {
			return ExoflameFurnaceHandler.canSmelt(furnace);
		}

		@Override
		public int getBurnTime() {
			return ((AbstractFurnaceBlockEntityAccessor) furnace).getLitTime();
		}

		@Override
		public void boostBurnTime() {
			if (getBurnTime() == 0) {
				Level world = furnace.getLevel();
				BlockPos pos = furnace.getBlockPos();
				world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(BlockStateProperties.LIT, true));
			}
			int burnTime = ((AbstractFurnaceBlockEntityAccessor) furnace).getLitTime();
			((AbstractFurnaceBlockEntityAccessor) furnace).setLitTime(burnTime + 200);
		}

		@Override
		public void boostCookTime() {
			int cookTime = ((AbstractFurnaceBlockEntityAccessor) furnace).getCookingProgress();
			int cookTimeTotal = ((AbstractFurnaceBlockEntityAccessor) furnace).getCookingTotalTime();
			((AbstractFurnaceBlockEntityAccessor) furnace).setCookingProgress(Math.min(cookTimeTotal - 1, cookTime + 1));
		}
	}
}
