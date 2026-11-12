package velrondevs.botania.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import velrondevs.botania.api.BotaniaRegistries;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin {
	@Shadow
	private static CreativeModeTab selectedTab;

	@Inject(method = "renderLabels", at = @At("TAIL"))
	private void botania$renderBotaniaTitle(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
		CreativeModeTab tab = selectedTab;
		if (tab != null && BotaniaRegistries.BOTANIA_TAB_KEY.equals(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).orElse(null))) {
			graphics.drawString(Minecraft.getInstance().font, tab.getDisplayName(), 8, 6, tab.getLabelColor(), false);
		}
	}
}
