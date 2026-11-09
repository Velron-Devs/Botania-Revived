package velrondevs.botania.common.item;

import net.minecraft.world.item.DyeColor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.entity.ManaSparkEntity;
import velrondevs.botania.common.helper.EntityHelper;
import velrondevs.botania.xplat.XplatAbstractions;

public class ManaSparkItem extends Item {

	public ManaSparkItem(Properties builder) {
		super(builder);
	}

	@NotNull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		ItemStack otherHandStack = ItemStack.EMPTY;
		if (ctx.getPlayer() != null) {
			otherHandStack = ctx.getPlayer().getItemInHand(EntityHelper.otherHand(ctx.getHand()));
			if (ctx.getPlayer().isCreative()) {
				otherHandStack = otherHandStack.copy();
			}
		}
		return attachSpark(ctx.getLevel(), ctx.getClickedPos(), ctx.getItemInHand(), otherHandStack)
				? InteractionResult.SUCCESS
				: InteractionResult.PASS;
	}

	public static boolean attachSpark(Level world, BlockPos pos, ItemStack stack, ItemStack otherHandStack) {
		var attach = XplatAbstractions.INSTANCE.findSparkAttachable(world, pos, world.getBlockState(pos), world.getBlockEntity(pos), Direction.UP);
		if (attach != null) {
			if (attach.canAttachSpark(stack) && attach.getAttachedSpark() == null) {
				if (!world.isClientSide()) {
					stack.shrink(1);
					ManaSparkEntity spark = new ManaSparkEntity(world);
					spark.setPos(pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5);
					if (otherHandStack.get(DataComponents.DYE) instanceof DyeColor dyeColor) {
						otherHandStack.shrink(1);
						spark.setNetwork(dyeColor);
					}
					world.addFreshEntity(spark);
					attach.attachSpark(spark);
				}
				return true;
			}
		}
		return false;
	}
}
