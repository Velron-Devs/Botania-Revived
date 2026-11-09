package velrondevs.botania.common.item;

import net.minecraft.world.item.DyeColor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntitySpawnReason;
import java.util.function.Consumer;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.corporea.CorporeaHelper;
import velrondevs.botania.common.entity.CorporeaSparkEntity;
import velrondevs.botania.common.helper.EntityHelper;
import velrondevs.botania.common.impl.corporea.DummyCorporeaNode;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.registry.BotaniaEntities;
import velrondevs.botania.registry.BotaniaItems;

import java.util.List;

public class CorporeaSparkItem extends Item {

	public CorporeaSparkItem(Properties props) {
		super(props);
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

	private static boolean canPlace(Level world, CorporeaSparkEntity spark) {
		return world.getBlockState(spark.getAttachPos()).is(BotaniaTags.Blocks.CORPOREA_SPARK_OVERRIDE)
				|| !(spark.getSparkNode() instanceof DummyCorporeaNode);
	}

	public static boolean attachSpark(Level world, BlockPos pos, ItemStack stack, ItemStack otherHandStack) {
		CorporeaSparkEntity spark = BotaniaEntities.CORPOREA_SPARK.create(world, EntitySpawnReason.TRIGGERED);
		if (stack.is(BotaniaItems.corporeaSparkMaster)) {
			spark.setMaster(true);
		}
		if (stack.is(BotaniaItems.corporeaSparkCreative)) {
			spark.setCreative(true);
		}
		spark.setPos(pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5);
		if (otherHandStack.get(DataComponents.DYE) instanceof DyeColor dyeColor) {
			otherHandStack.shrink(1);
			spark.setNetwork(dyeColor);
		}

		if (canPlace(world, spark) && !CorporeaHelper.instance().doesBlockHaveSpark(world, pos)) {
			if (!world.isClientSide()) {
				world.addFreshEntity(spark);
				stack.shrink(1);
			}
			return true;
		}
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag tooltipFlag) {
		if (stack.is(BotaniaItems.corporeaSparkCreative)) {
			tooltip.accept(Component.translatable("botaniamisc.creativeSpark").withStyle(ChatFormatting.GRAY));
		}
	}
}
