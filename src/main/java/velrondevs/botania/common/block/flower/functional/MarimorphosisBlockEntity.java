package velrondevs.botania.common.block.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.api.recipe.OrechidRecipe;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaRecipeTypes;
import velrondevs.botania.xplat.BotaniaConfig;

public class MarimorphosisBlockEntity extends OrechidBlockEntity {
	private static final int COST = 25;
	private static final int DELAY = 10;
	private static final int RANGE = 4;
	private static final int RANGE_Y = 2;

	private static final int RANGE_MINI = 2;
	private static final int RANGE_Y_MINI = 1;

	protected MarimorphosisBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public MarimorphosisBlockEntity(BlockPos pos, BlockState state) {
		this(BotaniaFlowerBlocks.MARIMORPHOSIS, pos, state);
	}

	@Override
	protected void playSound(BlockPos coords) {

	}

	@Override
	public RecipeType<? extends OrechidRecipe> getRecipeType() {
		return BotaniaRecipeTypes.MARIMORPHOSIS_TYPE;
	}

	@Override
	public int getDelay() {
		return configOr(DELAY, BotaniaConfig.ConfigAccess::marimorphosisDelay);
	}

	@Override
	public int getCost() {
		return configOr(COST, BotaniaConfig.ConfigAccess::marimorphosisCost);
	}

	@Override
	public int getRange() {
		return configOr(RANGE, BotaniaConfig.ConfigAccess::marimorphosisRange);
	}

	@Override
	public int getRangeY() {
		return configOr(RANGE_Y, BotaniaConfig.ConfigAccess::marimorphosisRangeY);
	}

	@Override
	public int getColor() {
		return 0x769897;
	}

	@Override
	public int getMaxMana() {
		return Math.max(1000, getCost());
	}

	public static class Mini extends MarimorphosisBlockEntity {
		public Mini(BlockPos pos, BlockState state) {
			super(BotaniaFlowerBlocks.MARIMORPHOSIS_CHIBI, pos, state);
		}

		@Override
		public int getRange() {
			return RANGE_MINI;
		}

		@Override
		public int getRangeY() {
			return RANGE_Y_MINI;
		}
	}

}
