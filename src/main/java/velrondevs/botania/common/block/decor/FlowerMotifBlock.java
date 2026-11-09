package velrondevs.botania.common.block.decor;

import java.util.function.Consumer;

import net.minecraft.world.item.component.TooltipDisplay;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.FlowerBlock;

import velrondevs.botania.common.item.CustomCreativeTabContents;

import java.util.List;

public class FlowerMotifBlock extends FlowerBlock implements CustomCreativeTabContents {
	private final boolean hidden;

	public FlowerMotifBlock(Holder<MobEffect> effect, int effectDuration, Properties properties, boolean hidden) {
		super(effect, effectDuration, properties);
		this.hidden = hidden;
	}

	public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flags) {
		if (hidden) {
			tooltip.accept(Component.translatable("block.botania.daybloom_motif.description").withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.accept(Component.translatable("block.botania.hydroangeas_motif.description").withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
		if (!this.hidden) {
			output.accept(me);
		}
	}
}
