package velrondevs.botania.integration.thaumaturge;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

final class RevealingHelmTooltip {
	private RevealingHelmTooltip() {}

	static void add(List<Component> list) {
		list.add(Component.translatable("botania.thaumaturge.revealing").withStyle(ChatFormatting.DARK_PURPLE));
		list.add(Component.translatable("botania.thaumaturge.vis_discount", ThaumaturgeIntegration.VIS_DISCOUNT).withStyle(ChatFormatting.DARK_PURPLE));
	}
}
