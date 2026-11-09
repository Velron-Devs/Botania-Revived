package velrondevs.botania.integration.thaumaturge;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.entity.BlockEntity;

import velrondevs.botania.api.mana.ManaBarTooltip;
import velrondevs.botania.api.mana.ManaItem;
import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.item.CustomCreativeTabContents;

import java.util.List;
import java.util.Optional;

public class ManaInkwellItem extends Item implements CustomCreativeTabContents {
	public static final int MAX_INK = 100;
	public static final int MANA_PER_INK = 500;
	public static final int MAX_MANA = MAX_INK * MANA_PER_INK;

	private static final String TAG_PARTIAL_MANA = "partialMana";

	public ManaInkwellItem(Properties props) {
		super(props);
	}

	public static int getInk(ItemStack stack) {
		return Math.max(0, stack.getMaxDamage() - stack.getDamageValue());
	}

	public static int getMana(ItemStack stack) {
		int ink = getInk(stack);
		int partial = ink >= stack.getMaxDamage() ? 0 : ItemNBTHelper.getInt(stack, TAG_PARTIAL_MANA, 0);
		return ink * MANA_PER_INK + partial;
	}

	public static void setMana(ItemStack stack, int mana) {
		int max = stack.getMaxDamage() * MANA_PER_INK;
		int clamped = Mth.clamp(mana, 0, max);
		stack.setDamageValue(stack.getMaxDamage() - clamped / MANA_PER_INK);
		int partial = clamped % MANA_PER_INK;
		if (partial > 0) {
			ItemNBTHelper.setInt(stack, TAG_PARTIAL_MANA, partial);
		} else {
			ItemNBTHelper.removeEntry(stack, TAG_PARTIAL_MANA);
		}
	}

	@Override
	public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
		output.accept(this);
		ItemStack full = new ItemStack(this);
		setMana(full, MAX_MANA);
		output.accept(full);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
		tooltip.add(Component.translatable("botania.thaumaturge.inkwell", getInk(stack), stack.getMaxDamage()).withStyle(ChatFormatting.GRAY));
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		return Optional.of(ManaBarTooltip.fromManaItem(stack));
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return getMana(stack) < stack.getMaxDamage() * MANA_PER_INK;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return Math.round(13F * getMana(stack) / Math.max(1, stack.getMaxDamage() * MANA_PER_INK));
	}

	@Override
	public int getBarColor(ItemStack stack) {
		float fraction = (float) getMana(stack) / Math.max(1, stack.getMaxDamage() * MANA_PER_INK);
		return Mth.hsvToRgb(fraction / 3.0F, 1.0F, 1.0F);
	}

	public static class ManaItemImpl implements ManaItem {
		private final ItemStack stack;

		public ManaItemImpl(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public int getMana() {
			return ManaInkwellItem.getMana(stack);
		}

		@Override
		public int getMaxMana() {
			return stack.getMaxDamage() * MANA_PER_INK;
		}

		@Override
		public void addMana(int mana) {
			setMana(stack, getMana() + mana);
		}

		@Override
		public boolean canReceiveManaFromPool(BlockEntity pool) {
			return true;
		}

		@Override
		public boolean canReceiveManaFromItem(ItemStack otherStack) {
			return true;
		}

		@Override
		public boolean canExportManaToPool(BlockEntity pool) {
			return false;
		}

		@Override
		public boolean canExportManaToItem(ItemStack otherStack) {
			return false;
		}

		@Override
		public boolean isNoExport() {
			return true;
		}
	}
}
