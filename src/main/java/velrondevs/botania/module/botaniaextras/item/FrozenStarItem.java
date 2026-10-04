package velrondevs.botania.module.botaniaextras.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.item.CustomCreativeTabContents;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasItems;
import velrondevs.botania.module.botaniaextras.IridescentColors;
import velrondevs.botania.module.botaniaextras.block.FrozenStarBlockEntity;

import java.util.List;
import java.util.Locale;

public class FrozenStarItem extends BlockItem implements CustomCreativeTabContents {
	public static final int RAINBOW = -1;
	public static final float DEFAULT_SIZE = 0.05F;
	private static final String TAG_COLOR = "color";
	private static final String TAG_SIZE = "size";

	public FrozenStarItem(Block block, Properties properties) {
		super(block, properties);
	}

	public static int colorOf(@Nullable DyeColor color) {
		return color == null ? RAINBOW : IridescentColors.rgb(color);
	}

	public static ItemStack forColor(int color) {
		ItemStack stack = new ItemStack(BotaniaExtrasItems.frozenStar);
		setColor(stack, color);
		return stack;
	}

	public static ItemStack forDye(@Nullable DyeColor color) {
		return forColor(colorOf(color));
	}

	public static void setColor(ItemStack stack, int color) {
		ItemNBTHelper.setInt(stack, TAG_COLOR, color);
	}

	public static int getColor(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COLOR, RAINBOW);
	}

	public static void setSize(ItemStack stack, float size) {
		if (size != DEFAULT_SIZE) {
			ItemNBTHelper.setFloat(stack, TAG_SIZE, size);
		}
	}

	public static float getSize(ItemStack stack) {
		return ItemNBTHelper.getFloat(stack, TAG_SIZE, DEFAULT_SIZE);
	}

	@Override
	protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
		boolean ret = super.updateCustomBlockEntityTag(pos, level, player, stack, state);
		if (!level.isClientSide && level.getBlockEntity(pos) instanceof FrozenStarBlockEntity star) {
			star.setStar(getColor(stack), getSize(stack));
		}
		return ret;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		int color = getColor(stack);
		Component name = null;
		if (color == RAINBOW) {
			name = IridescentColors.displayName(null);
		} else {
			for (DyeColor dye : DyeColor.values()) {
				if (IridescentColors.rgb(dye) == color) {
					name = IridescentColors.displayName(dye);
					break;
				}
			}
		}
		if (name == null) {
			name = Component.literal("#" + String.format(Locale.ROOT, "%06X", color & 0xFFFFFF));
		}
		tooltip.add(name.copy().withStyle(ChatFormatting.GRAY));
		float size = getSize(stack);
		if (size != DEFAULT_SIZE) {
			tooltip.add(Component.translatable("botaniamisc.botania_extras.customSize", String.format(Locale.ROOT, "%.1f", size / 0.1F)).withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
		for (DyeColor color : DyeColor.values()) {
			output.accept(forDye(color));
		}
		output.accept(forDye(null));
	}
}
