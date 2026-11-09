package velrondevs.botania.integration.thaumaturge;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

final class RevealingHelmTooltip {
	private RevealingHelmTooltip() {}

	static void add(Consumer<Component> tooltip) {
		tooltip.accept(Component.translatable("botania.thaumaturge.revealing").withStyle(ChatFormatting.DARK_PURPLE));
		tooltip.accept(Component.translatable("botania.thaumaturge.vis_discount", ThaumaturgeIntegration.VIS_DISCOUNT).withStyle(ChatFormatting.DARK_PURPLE));
	}
}
