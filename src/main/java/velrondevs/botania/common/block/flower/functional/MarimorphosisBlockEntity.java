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
	public static final int DEFAULT_COST = 12;
	public static final int DEFAULT_DELAY = 2;
	public static final int DEFAULT_RANGE = 8;
	public static final int DEFAULT_RANGE_Y = 5;
	public static final int MAX_MANA = 1000;

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
		return BotaniaConfig.common().marimorphosisInterval();
	}

	@Override
	public int getCost() {
		return BotaniaConfig.common().marimorphosisCost();
	}

	@Override
	public int getRange() {
		return BotaniaConfig.common().marimorphosisRange();
	}

	@Override
	public int getRangeY() {
		return BotaniaConfig.common().marimorphosisRangeY();
	}

	@Override
	public int getColor() {
		return 0x769897;
	}

	@Override
	public int getMaxMana() {
		return MAX_MANA;
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
