package velrondevs.botania.common.block.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.block.FloatingFlower.IslandType;
import velrondevs.botania.common.item.GrassSeedsItem;
import velrondevs.botania.network.EffectType;
import velrondevs.botania.network.clientbound.BotaniaEffectPacket;
import velrondevs.botania.xplat.XplatAbstractions;

public class GrassSeedsBehavior extends OptionalDispenseItemBehavior {
	@NotNull
	@Override
	public ItemStack execute(BlockSource source, ItemStack stack) {
		ServerLevel world = source.level();
		BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
		IslandType islandType = ((GrassSeedsItem) stack.getItem()).getIslandType(stack);

		setSuccess(((GrassSeedsItem) stack.getItem()).applySeeds(world, pos, stack).consumesAction());

		if (isSuccess()) {
			XplatAbstractions.INSTANCE.sendToNear(world, pos,
					new BotaniaEffectPacket(EffectType.GRASS_SEED_PARTICLES,
							pos.getX(), pos.getY(), pos.getZ(),
							GrassSeedsItem.getColor(islandType)));
			return stack;
		}

		return super.execute(source, stack);
	}
}
