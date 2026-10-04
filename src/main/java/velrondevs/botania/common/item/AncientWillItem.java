package velrondevs.botania.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import velrondevs.botania.api.item.AncientWillContainer;

import java.util.List;
import java.util.Locale;

public class AncientWillItem extends Item {
	public final AncientWillContainer.AncientWillType type;

	public AncientWillItem(AncientWillContainer.AncientWillType variant, Properties props) {
		super(props);
		this.type = variant;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext world, List<Component> list, TooltipFlag flag) {
		list.add(Component.translatable("botaniamisc.craftToAddWill").withStyle(ChatFormatting.GREEN));
		list.add(Component.translatable("botania.armorset.will_" + type.name().toLowerCase(Locale.ROOT) + ".shortDesc").withStyle(ChatFormatting.GRAY));
	}
}
