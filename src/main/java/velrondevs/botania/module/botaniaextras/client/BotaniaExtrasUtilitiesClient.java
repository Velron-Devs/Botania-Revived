package velrondevs.botania.module.botaniaextras.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import velrondevs.botania.api.BotaniaClientCapabilities;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasUtilities;
import velrondevs.botania.module.botaniaextras.block.LivingwoodFunnelBlockEntity;
import velrondevs.botania.module.botaniaextras.entity.ManasealCreeperRenderer;
import velrondevs.botania.module.botaniaextras.item.ClericalColorizerItem;

public final class BotaniaExtrasUtilitiesClient {
	private BotaniaExtrasUtilitiesClient() {}

	public static void init(IEventBus modBus) {
		modBus.addListener(BotaniaExtrasUtilitiesClient::itemColors);
		modBus.addListener(BotaniaExtrasUtilitiesClient::renderers);
		modBus.addListener(BotaniaExtrasUtilitiesClient::capabilities);
		modBus.addListener((FMLClientSetupEvent e) -> ItemBlockRenderTypes.setRenderLayer(BotaniaExtrasUtilities.livingwoodFunnel, RenderType.cutoutMipped()));
		ToolbeltClient.init();
	}

	private static void itemColors(RegisterColorHandlersEvent.Item e) {
		e.register((stack, tint) -> tint == 0 ? FastColor.ARGB32.opaque(ClericalColorizerItem.getColor(stack)) : -1, BotaniaExtrasUtilities.clericalColorizer);
	}

	private static void renderers(EntityRenderersEvent.RegisterRenderers e) {
		e.registerEntityRenderer(BotaniaExtrasUtilities.MANASEAL_CREEPER, ManasealCreeperRenderer::new);
	}

	private static void capabilities(RegisterCapabilitiesEvent e) {
		e.registerBlockEntity(BotaniaClientCapabilities.WAND_HUD, BotaniaExtrasUtilities.FUNNEL, (be, ctx) -> new LivingwoodFunnelBlockEntity.WandHud(be));
	}
}
