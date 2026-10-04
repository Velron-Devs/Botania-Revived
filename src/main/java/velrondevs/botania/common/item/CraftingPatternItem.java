package velrondevs.botania.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.state.BotaniaStateProperties;
import velrondevs.botania.api.state.enums.CraftyCratePattern;
import velrondevs.botania.common.block.block_entity.CraftyCrateBlockEntity;
import velrondevs.botania.registry.BotaniaBlockEntities;
import velrondevs.botania.registry.BotaniaBlocks;

public class CraftingPatternItem extends Item {
	public final CraftyCratePattern pattern;

	public CraftingPatternItem(CraftyCratePattern pattern, Properties props) {
		super(props);
		this.pattern = pattern;
	}

	@NotNull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		BlockState state = world.getBlockState(pos);

		if (state.is(BotaniaBlocks.craftCrate)) {
			if (pattern != state.getValue(BotaniaStateProperties.CRATE_PATTERN)) {
				world.setBlockAndUpdate(pos, state.setValue(BotaniaStateProperties.CRATE_PATTERN, this.pattern));
				if (!world.isClientSide) {
					world.getBlockEntity(pos, BotaniaBlockEntities.CRAFT_CRATE)
							.ifPresent(CraftyCrateBlockEntity::ejectLocked);
				}
				return InteractionResult.sidedSuccess(world.isClientSide());
			}
		}

		return InteractionResult.PASS;
	}
}
