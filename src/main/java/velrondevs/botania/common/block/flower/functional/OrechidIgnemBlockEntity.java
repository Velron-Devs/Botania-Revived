package velrondevs.botania.common.block.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.api.recipe.OrechidRecipe;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaRecipeTypes;
import velrondevs.botania.xplat.BotaniaConfig;

public class OrechidIgnemBlockEntity extends OrechidBlockEntity {
	private static final int COST = 120;
	private static final int RANGE = 3;
	private static final int RANGE_Y = 1;

	public OrechidIgnemBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.ORECHID_IGNEM, pos, state);
	}

	@Override
	public boolean canOperate() {
		if (getLevel().dimensionType().hasCeiling()) {
			return true;
		}
		var config = BotaniaConfig.common();
		return config == null || config.orechidIgnemAnyDimension();
	}

	@Override
	public RecipeType<? extends OrechidRecipe> getRecipeType() {
		return BotaniaRecipeTypes.ORECHID_IGNEM_TYPE;
	}

	@Override
	public int getCost() {
		return configOr(COST, BotaniaConfig.ConfigAccess::orechidIgnemCost);
	}

	@Override
	public int getRange() {
		return configOr(RANGE, BotaniaConfig.ConfigAccess::orechidIgnemRange);
	}

	@Override
	public int getRangeY() {
		return configOr(RANGE_Y, BotaniaConfig.ConfigAccess::orechidIgnemRangeY);
	}

	@Override
	public int getColor() {
		return 0xAE3030;
	}

}
