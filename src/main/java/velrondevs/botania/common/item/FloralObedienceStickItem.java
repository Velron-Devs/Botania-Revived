package velrondevs.botania.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import velrondevs.botania.api.block_entity.FunctionalFlowerBlockEntity;
import velrondevs.botania.api.block_entity.GeneratingFlowerBlockEntity;
import velrondevs.botania.api.mana.ManaCollector;
import velrondevs.botania.api.mana.ManaPool;
import velrondevs.botania.common.helper.MathHelper;
import velrondevs.botania.xplat.XplatAbstractions;

public class FloralObedienceStickItem extends Item {

	public FloralObedienceStickItem(Properties props) {
		super(props);
	}

	@NotNull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		return applyStick(world, pos)
				? InteractionResult.sidedSuccess(world.isClientSide())
				: InteractionResult.PASS;
	}

	public static boolean applyStick(Level world, BlockPos pos) {
		var receiver = XplatAbstractions.INSTANCE.findManaReceiver(world, pos, null);
		if (receiver instanceof ManaPool || receiver instanceof ManaCollector) {
			int range = receiver instanceof ManaPool ? FunctionalFlowerBlockEntity.LINK_RANGE : GeneratingFlowerBlockEntity.LINK_RANGE;

			for (BlockPos iterPos : BlockPos.betweenClosed(pos.offset(-range, -range, -range), pos.offset(range, range, range))) {
				if (MathHelper.distSqr(iterPos, pos) > range * range) {
					continue;
				}

				if (world.getBlockEntity(iterPos) instanceof BindableSpecialFlowerBlockEntity<?>bindable
						&& bindable.wouldBeValidBinding(pos)) {
					bindable.setBindingPos(pos);
					WandOfTheForestItem.doParticleBeamWithOffset(world, iterPos, pos);
				}
			}

			return true;
		}
		if (world.getBlockEntity(pos) instanceof BindableSpecialFlowerBlockEntity<?>bindableFlower) {
			bindableFlower.setBindingPos(null);
			return true;
		}

		return false;
	}
}
