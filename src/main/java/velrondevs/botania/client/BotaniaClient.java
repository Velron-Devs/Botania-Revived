package velrondevs.botania.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import velrondevs.botania.client.model.armor.ArmorModels;
import velrondevs.botania.common.item.block.BlockItemWithSpecialRenderer;
import velrondevs.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;

import com.google.common.base.Suppliers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.BotaniaAPIClient;
import velrondevs.botania.api.BotaniaClientCapabilities;
import velrondevs.botania.api.block.WandHUD;
import velrondevs.botania.api.mana.ManaBarTooltip;
import velrondevs.botania.client.core.handler.*;
import velrondevs.botania.client.core.helper.CoreShaders;
import velrondevs.botania.client.core.proxy.ClientProxy;
import velrondevs.botania.client.fx.BotaniaParticleProviders;
import velrondevs.botania.client.gui.HUDHandler;
import velrondevs.botania.client.gui.ManaBarTooltipComponent;
import velrondevs.botania.client.gui.TooltipHandler;
import velrondevs.botania.client.gui.bag.FlowerPouchGui;
import velrondevs.botania.client.gui.box.BaubleBoxGui;
import velrondevs.botania.client.integration.ears.EarsIntegration;
import velrondevs.botania.client.integration.ponder.BotaniaPonderIntegration;
import velrondevs.botania.client.model.BotaniaLayerDefinitions;
import velrondevs.botania.client.model.FloatingFlowerGeometry;
import velrondevs.botania.client.model.ManaBlasterGeometry;
import velrondevs.botania.client.render.BlockEntityItemRendererHelper;
import velrondevs.botania.client.render.BlockRenderLayers;
import velrondevs.botania.client.render.ColorHandler;
import velrondevs.botania.client.render.entity.EntityRenderers;
import velrondevs.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import velrondevs.botania.common.item.equipment.bauble.RingOfDexterousMotionItem;
import velrondevs.botania.registry.BotaniaBlockEntities;
import velrondevs.botania.registry.BotaniaEntities;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.xplat.ClientXplatAbstractions;
import velrondevs.botania.xplat.XplatAbstractions;
import vazkii.patchouli.api.BookDrawScreenEvent;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

@EventBusSubscriber(modid = BotaniaAPI.MODID, value = Dist.CLIENT)
public class BotaniaClient {
	@SubscribeEvent
	public static void registerMenuScreens(RegisterMenuScreensEvent e) {
		e.register(BotaniaItems.FLOWER_BAG_CONTAINER, FlowerPouchGui::new);
		e.register(BotaniaItems.BAUBLE_BOX_CONTAINER, BaubleBoxGui::new);
	}

	@SubscribeEvent
	public static void registerGuiLayers(RegisterGuiLayersEvent e) {
		e.registerAbove(VanillaGuiLayers.EXPERIENCE_BAR, prefix("hud"),
				(guiGraphics, deltaTracker) -> HUDHandler.onDrawScreenPost(guiGraphics, deltaTracker.getGameTimeDeltaPartialTick(false)));
	}

	@SubscribeEvent
	public static void clientInit(FMLClientSetupEvent evt) {

		BotaniaAPI.LOGGER.debug("Client API instances: {}",
				List.of(BotaniaAPIClient.instance(), ClientXplatAbstractions.instance()));

		BlockRenderLayers.skipPlatformBlocks = true;
		BlockRenderLayers.init(ItemBlockRenderTypes::setRenderLayer);

		evt.enqueueWork(() -> BotaniaItemProperties.init((item, id, prop) -> ItemProperties.register(item.asItem(), id, prop)));

		var bus = NeoForge.EVENT_BUS;
		bus.addListener((BookDrawScreenEvent e) -> KonamiHandler.renderBook(e.getBook(), e.getScreen(), e.getMouseX(), e.getMouseY(), e.getPartialTicks(), e.getGraphics()));
		bus.addListener((ClientTickEvent.Post e) -> {
			ClientTickHandler.clientTickEnd(Minecraft.getInstance());
			KonamiHandler.clientTick(Minecraft.getInstance());
		});
		bus.addListener((ItemTooltipEvent e) -> TooltipHandler.onTooltipEvent(e.getItemStack(), e.getFlags(), e.getToolTip()));
		bus.addListener((ScreenEvent.KeyPressed.Post e) -> CorporeaInputHandler.buttonPressed(e.getKeyCode(), e.getScanCode()));

		bus.addListener(EventPriority.HIGH, (ClientChatEvent e) -> {
			var player = Minecraft.getInstance().player;
			if (player != null && CorporeaIndexBlockEntity.ClientHandler.onChat(player, e.getMessage())) {
				e.setCanceled(true);
			}
		});
		bus.addListener((CustomizeGuiOverlayEvent.BossEventProgress e) -> {
			var result = BossBarHandler.onBarRender(e.getGuiGraphics(), e.getX(), e.getY(),
					e.getBossEvent(), true);
			result.ifPresent(increment -> {
				e.setCanceled(true);
				e.setIncrement(increment);
			});
		});
		bus.addListener((CustomizeGuiOverlayEvent.DebugText e) -> DebugHandler.onDrawDebugText(e.getLeft()));
		bus.addListener((InputEvent.Key e) -> {
			RingOfDexterousMotionItem.ClientLogic.onKeyDown();
			KonamiHandler.handleInput(e.getKey(), e.getAction(), e.getModifiers());
		});
		bus.addListener((RenderFrameEvent.Pre e) -> ClientTickHandler.renderTick(e.getPartialTick().getGameTimeDeltaPartialTick(true)));
		bus.addListener(EventPriority.LOWEST, (RenderTooltipEvent.Color e) -> {
			var manaItem = XplatAbstractions.INSTANCE.findManaItem(e.getItemStack());
			if (manaItem == null) {
				return;
			}

			int width = 0;
			ManaBarTooltipComponent manaBar = null;
			for (ClientTooltipComponent component : e.getComponents()) {
				width = Math.max(width, component.getWidth(e.getFont()));
				if (component instanceof ManaBarTooltipComponent c) {
					manaBar = c;
				}
			}
			if (manaBar != null) {
				manaBar.setContext(e.getX(), e.getY(), width);
			}
		});

		ClientProxy.initSeasonal();

		if (XplatAbstractions.INSTANCE.isModLoaded("ears")) {
			EarsIntegration.register();
		}
		if (XplatAbstractions.INSTANCE.isModLoaded("ponder")) {
			BotaniaPonderIntegration.init();
		}
	}

	@SubscribeEvent
	public static void registerTooltipComponent(RegisterClientTooltipComponentFactoriesEvent e) {
		e.register(ManaBarTooltip.class, ManaBarTooltipComponent::new);
	}

	@SubscribeEvent
	public static void registerKeys(RegisterKeyMappingsEvent e) {
		ClientProxy.initKeybindings(e::register);
	}

	private static final Supplier<Map<BlockEntityType<?>, Function<BlockEntity, WandHUD>>> WAND_HUD = Suppliers.memoize(() -> {
		var ret = new IdentityHashMap<BlockEntityType<?>, Function<BlockEntity, WandHUD>>();
		BotaniaBlockEntities.registerWandHudCaps((factory, types) -> {
			for (var type : types) {
				ret.put(type, factory);
			}
		});
		BotaniaFlowerBlocks.registerWandHudCaps((factory, types) -> {
			for (var type : types) {
				ret.put(type, factory);
			}
		});
		return Collections.unmodifiableMap(ret);
	});

	private static final Supplier<Map<EntityType<?>, Function<Entity, WandHUD>>> ENTITY_WAND_HUD = Suppliers.memoize(() -> {
		var ret = new IdentityHashMap<EntityType<?>, Function<Entity, WandHUD>>();
		BotaniaEntities.registerWandHudCaps((factory, types) -> {
			for (var type : types) {
				ret.put(type, factory);
			}
		});
		return Collections.unmodifiableMap(ret);
	});

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent e) {
		WAND_HUD.get().forEach((type, factory) -> e.registerBlockEntity(BotaniaClientCapabilities.WAND_HUD, type, (be, ctx) -> factory.apply(be)));
		ENTITY_WAND_HUD.get().forEach((type, factory) -> e.registerEntity(BotaniaClientCapabilities.ENTITY_WAND_HUD, type, (entity, ctx) -> factory.apply(entity)));
	}

	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent e) {
		for (Item item : BuiltInRegistries.ITEM) {
			if (item instanceof BlockItemWithSpecialRenderer) {
				e.registerItem(BlockEntityItemRendererHelper.PROPS, item);
			}
			if (item instanceof ManasteelArmorItem) {
				e.registerItem(new IClientItemExtensions() {
					@Override
					public HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> defaultModel) {
						return ArmorModels.get(stack);
					}
				}, item);
			}
		}
	}

	@SubscribeEvent
	public static void registerModelLoader(ModelEvent.RegisterGeometryLoaders evt) {
		evt.register(ClientXplatAbstractions.FLOATING_FLOWER_MODEL_LOADER_ID,
				FloatingFlowerGeometry.Loader.INSTANCE);
		evt.register(ClientXplatAbstractions.MANA_GUN_MODEL_LOADER_ID,
				ManaBlasterGeometry.Loader.INSTANCE);
	}

	@SubscribeEvent
	public static void onModelRegister(ModelEvent.RegisterAdditional evt) {
		var resourceManager = Minecraft.getInstance().getResourceManager();
		MiscellaneousModels.INSTANCE.onModelRegister(resourceManager, rl -> evt.register(ModelResourceLocation.standalone(rl)));
	}

	@SubscribeEvent
	public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions evt) {
		BotaniaLayerDefinitions.init(evt::registerLayerDefinition);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers evt) {
		EntityRenderers.registerBlockEntityRenderers(evt::registerBlockEntityRenderer);
		EntityRenderers.registerEntityRenderers(evt::registerEntityRenderer);
	}

	@SubscribeEvent
	public static void registerParticleFactories(RegisterParticleProvidersEvent evt) {
		BotaniaParticleProviders.registerFactories(new BotaniaParticleProviders.Consumer() {
			@Override
			public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> constructor) {
				evt.registerSpriteSet(type, constructor::apply);
			}
		});
	}

	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block evt) {
		ColorHandler.submitBlocks(evt::register);
	}

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item evt) {
		ColorHandler.submitItems((handler, items) -> evt.register((stack, tintIndex) -> FastColor.ARGB32.opaque(handler.getColor(stack, tintIndex)), items));
	}

	@SubscribeEvent
	public static void initAuxiliaryRender(EntityRenderersEvent.AddLayers evt) {
		for (var playerModelType : evt.getSkins()) {
			if (evt.getSkin(playerModelType) instanceof PlayerRenderer renderer) {
				EntityRenderers.addAuxiliaryPlayerRenders(renderer, renderer::addLayer);
			}
		}
	}

	@SubscribeEvent
	public static void registerShaders(RegisterShadersEvent evt) {
		CoreShaders.init((id, vertexFormat, onLoaded) -> {
			try {
				evt.registerShader(
						new ShaderInstance(evt.getResourceProvider(), id, vertexFormat),
						onLoaded
				);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
	}

	@SubscribeEvent
	public static void onModelBake(ModelEvent.ModifyBakingResult evt) {
		MiscellaneousModels.INSTANCE.onModelBake(evt.getModels());
	}

}
