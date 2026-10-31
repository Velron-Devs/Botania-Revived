package velrondevs.botania.compat.sophisticated.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.util.Lazy;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlockEntity;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.client.render.BackpackItemStackRenderer;
import net.p3pp3rf1y.sophisticatedbackpacks.compat.curios.BackpackCurioRenderer;
import net.p3pp3rf1y.sophisticatedcore.util.WorldHelper;

import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import velrondevs.botania.compat.sophisticated.backpack.BotaniaBackpackRegistry;

public final class BotaniaBackpackClient {
	private BotaniaBackpackClient() {}

	public static void init(IEventBus modBus) {
		modBus.addListener(BotaniaBackpackClient::registerExtensions);
		modBus.addListener(BotaniaBackpackClient::registerBlockColors);
		modBus.addListener(BotaniaBackpackClient::registerItemColors);
		if (ModList.get().isLoaded("curios")) {
			modBus.addListener((FMLClientSetupEvent event) -> event.enqueueWork(CuriosHook::register));
		}
	}

	private static void registerExtensions(RegisterClientExtensionsEvent event) {
		event.registerItem(new IClientItemExtensions() {
			private final Lazy<BlockEntityWithoutLevelRenderer> renderer = Lazy.of(() ->
					new BackpackItemStackRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return renderer.get();
			}
		}, BotaniaBackpackRegistry.items().toArray(new Item[0]));
	}

	private static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		event.register((state, level, pos, tintIndex) -> {
			if (tintIndex < 0 || tintIndex > 1 || pos == null) {
				return -1;
			}
			return WorldHelper.getBlockEntity(level, pos, BackpackBlockEntity.class)
					.map(be -> tintIndex == 0 ? be.getMainColor() : be.getAccentColor())
					.orElse(tintIndex == 0 ? BackpackWrapper.DEFAULT_MAIN_COLOR : BackpackWrapper.DEFAULT_ACCENT_COLOR);
		}, BotaniaBackpackRegistry.blocks().toArray(new Block[0]));
	}

	private static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		event.register((stack, layer) -> {
			if (layer > 1 || !(stack.getItem() instanceof BackpackItem)) {
				return -1;
			}
			return layer == 0 ? BackpackItem.getMainColor(stack) : BackpackItem.getAccentColor(stack);
		}, BotaniaBackpackRegistry.items().toArray(new Item[0]));
	}

	private static final class CuriosHook {
		static void register() {
			for (BackpackItem item : BotaniaBackpackRegistry.items()) {
				CuriosRendererRegistry.register(item, BackpackCurioRenderer::new);
			}
		}
	}
}
