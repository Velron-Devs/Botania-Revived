package velrondevs.botania.common.block.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.api.recipe.OrechidRecipe;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaRecipeTypes;
import velrondevs.botania.xplat.BotaniaConfig;

public class OrechidIgnemBlockEntity extends OrechidBlockEntity {
	public static final int DEFAULT_COST = 20000;

	public OrechidIgnemBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.ORECHID_IGNEM, pos, state);
	}

	@Override
	public boolean canOperate() {
		return BotaniaConfig.common().orechidIgnemAnyDimension() || getLevel().dimensionType().hasCeiling();
	}

	@Override
	public RecipeType<? extends OrechidRecipe> getRecipeType() {
		return BotaniaRecipeTypes.ORECHID_IGNEM_TYPE;
	}

	@Override
	public int getCost() {
		return BotaniaConfig.common().orechidIgnemCost();
	}

	@Override
	public int getRange() {
		return BotaniaConfig.common().orechidIgnemRange();
	}

	@Override
	public int getRangeY() {
		return BotaniaConfig.common().orechidIgnemRangeY();
	}

	@Override
	public int getColor() {
		return 0xAE3030;
	}

}
