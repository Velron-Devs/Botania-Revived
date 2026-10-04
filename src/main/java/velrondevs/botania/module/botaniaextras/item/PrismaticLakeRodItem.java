package velrondevs.botania.module.botaniaextras.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.item.PhantomInkable;
import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.block.ManaFlashBlockEntity;

import java.util.List;

public class PrismaticLakeRodItem extends Item implements PhantomInkable {
	public static final int COST = 100;
	private static final String TAG_PHANTOM_INK = "phantomInk";

	public PrismaticLakeRodItem(Properties props) {
		super(props);
	}

	@NotNull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level level = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		Player player = ctx.getPlayer();
		ItemStack stack = ctx.getItemInHand();
		Direction side = ctx.getClickedFace();
		BlockState state = level.getBlockState(pos);

		if (state.is(BotaniaExtrasBlocks.rainbowManaFlash)) {
			if (!level.isClientSide) {
				level.removeBlock(pos, false);
				level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.3F, level.random.nextFloat() * 0.4F + 0.8F);
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		if (player == null || !ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, false)) {
			return InteractionResult.PASS;
		}
		BlockPos target = pos.relative(side);
		BlockState targetState = level.getBlockState(target);
		if (!(targetState.isAir() || targetState.canBeReplaced()) || !player.mayUseItemAt(target, side, stack)) {
			return InteractionResult.PASS;
		}
		if (!level.isClientSide) {
			var fluid = level.getFluidState(target);
			boolean water = fluid.isSource() && fluid.is(FluidTags.WATER);
			level.setBlockAndUpdate(target, BotaniaExtrasBlocks.rainbowManaFlash.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, water));
			if (level.getBlockEntity(target) instanceof ManaFlashBlockEntity flash) {
				flash.setInvisible(hasPhantomInk(stack));
			}
			level.playSound(null, target, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.3F, level.random.nextFloat() * 0.4F + 0.8F);
			ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, true);
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
		if (hasPhantomInk(stack)) {
			tooltip.add(Component.translatable("botaniamisc.hasPhantomInk").withStyle(ChatFormatting.AQUA));
		}
	}

	@Override
	public boolean hasPhantomInk(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_PHANTOM_INK, false);
	}

	@Override
	public void setPhantomInk(ItemStack stack, boolean ink) {
		ItemNBTHelper.setBoolean(stack, TAG_PHANTOM_INK, ink);
	}
}
