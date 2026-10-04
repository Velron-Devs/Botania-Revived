package velrondevs.botania.common.item.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.block.flower.generating.HydroangeasBlockEntity;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.xplat.BotaniaConfig;

import java.util.List;

public class SpecialFlowerBlockItem extends BlockItem {

	public SpecialFlowerBlockItem(Block block1, Properties props) {
		super(block1, props);
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, TooltipContext world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {

		if (BotaniaConfig.client() != null) {
			if (stack.is(BotaniaTags.Items.GENERATING_SPECIAL_FLOWERS)
					|| stack.is(BotaniaTags.Items.GENERATING_FLOATING_FLOWERS)) {
				tooltip.add(Component.translatable("botania.flowerType.generating").withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE));
			} else if (stack.is(BotaniaTags.Items.FUNCTIONAL_SPECIAL_FLOWERS)
					|| stack.is(BotaniaTags.Items.FUNCTIONAL_FLOATING_FLOWERS)) {
				tooltip.add(Component.translatable("botania.flowerType.functional").withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE));
			} else if (stack.is(BotaniaTags.Items.MISC_SPECIAL_FLOWERS)
					|| stack.is(BotaniaTags.Items.MISC_FLOATING_FLOWERS)) {
				tooltip.add(Component.translatable("botania.flowerType.misc").withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE));
			}

			if (BotaniaConfig.client().referencesEnabled()) {
				String key = getDescriptionId() + ".reference";
				if (key.contains("floating_") && !Language.getInstance().has(key)) {
					key = key.replace("floating_", "");
				}
				MutableComponent lore = Component.translatable(key);
				if (!lore.getString().equals(key)) {
					tooltip.add(lore.withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
				}
			}
		}
	}

	@Nullable
	private static CompoundTag getBlockEntityTag(ItemStack stack) {
		CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
		return data == null ? null : data.copyTag();
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		CompoundTag tag = getBlockEntityTag(stack);
		return tag != null && tag.contains(HydroangeasBlockEntity.TAG_PASSIVE_DECAY_TICKS);
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		CompoundTag tag = getBlockEntityTag(stack);
		if (tag != null) {
			float frac = 1 - tag.getInt(HydroangeasBlockEntity.TAG_PASSIVE_DECAY_TICKS) / (float) HydroangeasBlockEntity.DECAY_TIME;
			return Math.round(13F * frac);
		}
		return 0;
	}

	@Override
	public int getBarColor(ItemStack stack) {
		CompoundTag tag = getBlockEntityTag(stack);
		if (tag != null) {
			float frac = 1 - tag.getInt(HydroangeasBlockEntity.TAG_PASSIVE_DECAY_TICKS) / (float) HydroangeasBlockEntity.DECAY_TIME;
			return Mth.hsvToRgb(frac / 3.0F, 1.0F, 1.0F);
		}
		return 0;
	}
}
